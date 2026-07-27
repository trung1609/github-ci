package com.devops.demo.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Chuẩn hóa format phản hồi API theo chuẩn RESTful chuẩn doanh nghiệp
 * Tất cả response đều bọc trong đối tượng này để client dễ xử lý
 *
 * Ví dụ response thành công:
 * {
 *   "success": true,
 *   "message": "Tạo user thành công",
 *   "data": { ... },
 *   "timestamp": "2024-01-01T10:00:00"
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các field null trong JSON output
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    // Factory method: tạo response thành công kèm data
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Factory method: tạo response thành công không có data (ví dụ: DELETE)
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Factory method: tạo response lỗi
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
