package com.devops.demo.userservice.controller;

import com.devops.demo.userservice.dto.ApiResponse;
import com.devops.demo.userservice.dto.UserDto;
import com.devops.demo.userservice.entity.User;
import com.devops.demo.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller cho User API
 *
 * Base URL: /api/v1/users
 *
 * Endpoints:
 *  GET    /api/v1/users              -> Lấy danh sách tất cả users
 *  GET    /api/v1/users/{id}         -> Lấy chi tiết một user
 *  POST   /api/v1/users              -> Tạo user mới
 *  PUT    /api/v1/users/{id}         -> Cập nhật thông tin user
 *  DELETE /api/v1/users/{id}         -> Xóa mềm user
 *  GET    /api/v1/users/search?q=... -> Tìm kiếm user theo tên
 *  GET    /api/v1/users/role/{role}  -> Lọc user theo role
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // Cho phép CORS từ mọi nguồn (chỉ dùng cho demo)
public class UserController {

    private final UserService userService;

    // ==========================
    // GET ALL USERS
    // ==========================
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto.Response>>> getAllUsers() {
        log.info("GET /api/v1/users - Lấy danh sách users");
        List<UserDto.Response> users = userService.getAllUsers();
        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách users thành công", users));
    }

    // ==========================
    // GET USER BY ID
    // ==========================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto.Response>> getUserById(@PathVariable Long id) {
        log.info("GET /api/v1/users/{} - Lấy thông tin user", id);
        UserDto.Response user = userService.getUserById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Tìm thấy user", user));
    }

    // ==========================
    // CREATE USER
    // ==========================
    @PostMapping
    public ResponseEntity<ApiResponse<UserDto.Response>> createUser(
            @Valid @RequestBody UserDto.CreateRequest request) {

        log.info("POST /api/v1/users - Tạo user mới: {}", request.getEmail());
        UserDto.Response created = userService.createUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo user thành công", created));
    }

    // ==========================
    // UPDATE USER
    // ==========================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto.Response>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto.UpdateRequest request) {

        log.info("PUT /api/v1/users/{} - Cập nhật user", id);
        UserDto.Response updated = userService.updateUser(id, request);
        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật user thành công", updated));
    }

    // ==========================
    // DELETE USER (Soft Delete)
    // ==========================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/v1/users/{} - Xóa user", id);
        userService.deleteUser(id);
        return ResponseEntity.ok(
                ApiResponse.success("Xóa user thành công"));
    }

    // ==========================
    // SEARCH USERS BY NAME
    // ==========================
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserDto.Response>>> searchUsers(
            @RequestParam("q") String keyword) {

        log.info("GET /api/v1/users/search?q={}", keyword);
        List<UserDto.Response> results = userService.searchUsers(keyword);
        return ResponseEntity.ok(
                ApiResponse.success("Tìm kiếm hoàn tất (" + results.size() + " kết quả)", results));
    }

    // ==========================
    // FILTER BY ROLE
    // ==========================
    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponse<List<UserDto.Response>>> getUsersByRole(
            @PathVariable User.Role role) {

        log.info("GET /api/v1/users/role/{} - Lọc users theo role", role);
        List<UserDto.Response> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(
                ApiResponse.success("Lọc users theo role " + role + " thành công", users));
    }
}
