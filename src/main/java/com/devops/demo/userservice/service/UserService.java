package com.devops.demo.userservice.service;

import com.devops.demo.userservice.dto.UserDto;
import com.devops.demo.userservice.entity.User;
import com.devops.demo.userservice.exception.ResourceNotFoundException;
import com.devops.demo.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Lớp Service: chứa toàn bộ business logic của ứng dụng.
 * Controller không được trực tiếp gọi Repository mà phải đi qua Service.
 *
 * Nguyên tắc:
 * - Service @Transactional: đảm bảo tính toàn vẹn của dữ liệu
 * - Slf4j: ghi log tất cả các thao tác quan trọng để debug CI/CD dễ hơn
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * Lấy tất cả users đang active trong hệ thống
     */
    @Transactional(readOnly = true)
    public List<UserDto.Response> getAllUsers() {
        log.info("Đang lấy danh sách tất cả users...");
        return userRepository.findByActiveTrue()
                .stream()
                .map(UserDto.Response::from)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin một user theo ID
     */
    @Transactional(readOnly = true)
    public UserDto.Response getUserById(Long id) {
        log.info("Tìm kiếm user với ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy user với ID: " + id));
        return UserDto.Response.from(user);
    }

    /**
     * Tạo user mới trong hệ thống
     * Business Rule: email phải là duy nhất trong toàn hệ thống
     */
    public UserDto.Response createUser(UserDto.CreateRequest request) {
        log.info("Đang tạo user mới với email: {}", request.getEmail());

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Email '" + request.getEmail() + "' đã được sử dụng bởi tài khoản khác!");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(request.getRole() != null ? request.getRole() : User.Role.STUDENT)
                .build();

        User savedUser = userRepository.save(user);
        log.info("Tạo user thành công với ID: {}", savedUser.getId());
        return UserDto.Response.from(savedUser);
    }

    /**
     * Cập nhật thông tin user
     * Chỉ cập nhật những field được cung cấp trong request (partial update)
     */
    public UserDto.Response updateUser(Long id, UserDto.UpdateRequest request) {
        log.info("Đang cập nhật user với ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy user với ID: " + id));

        // Partial update: chỉ cập nhật field khác null
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }

        User updatedUser = userRepository.save(user);
        log.info("Cập nhật user thành công: {}", updatedUser.getId());
        return UserDto.Response.from(updatedUser);
    }

    /**
     * Xóa mềm user (Soft Delete) - chỉ đặt active = false, không xóa khỏi DB
     * Đây là best practice: không bao giờ xóa cứng dữ liệu người dùng
     */
    public void deleteUser(Long id) {
        log.info("Đang xóa (soft delete) user với ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy user với ID: " + id));

        user.setActive(false);
        userRepository.save(user);
        log.info("Đã xóa mềm user với ID: {}", id);
    }

    /**
     * Tìm kiếm user theo tên (không phân biệt hoa thường)
     */
    @Transactional(readOnly = true)
    public List<UserDto.Response> searchUsers(String keyword) {
        log.info("Tìm kiếm users với từ khóa: '{}'", keyword);
        return userRepository.searchByName(keyword)
                .stream()
                .map(UserDto.Response::from)
                .collect(Collectors.toList());
    }

    /**
     * Lọc user theo role
     */
    @Transactional(readOnly = true)
    public List<UserDto.Response> getUsersByRole(User.Role role) {
        log.info("Lọc users theo role: {}", role);
        return userRepository.findByRole(role)
                .stream()
                .map(UserDto.Response::from)
                .collect(Collectors.toList());
    }
}
