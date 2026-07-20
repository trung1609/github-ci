package com.devops.demo.userservice.dto;

import com.devops.demo.userservice.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// ========================== REQUEST DTOs ==========================

public class UserDto {

    /**
     * DTO dùng để nhận dữ liệu khi tạo user mới (POST /api/users)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {

        @NotBlank(message = "Tên không được để trống")
        @Size(min = 2, max = 100, message = "Tên phải từ 2 đến 100 ký tự")
        private String name;

        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không hợp lệ")
        private String email;

        private User.Role role;
    }

    /**
     * DTO dùng để nhận dữ liệu khi cập nhật user (PUT /api/users/{id})
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {

        @Size(min = 2, max = 100, message = "Tên phải từ 2 đến 100 ký tự")
        private String name;

        private User.Role role;

        private Boolean active;
    }

    // ========================== RESPONSE DTOs ==========================

    /**
     * DTO dùng để trả dữ liệu ra client (GET)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private String email;
        private User.Role role;
        private boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        // Mapper: chuyển đổi từ Entity sang Response DTO
        public static Response from(User user) {
            return Response.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .active(user.isActive())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build();
        }
    }
}
