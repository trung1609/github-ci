package com.devops.demo.userservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Application Context Test
 * Kiểm tra toàn bộ Spring ApplicationContext khởi động thành công
 * Đây là "bộ lọc tầng 1" trong CI pipeline: nếu test này fail -> đang có lỗi cấu hình nghiêm trọng
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Application Context Load Test")
class UserServiceApplicationTests {

    @Test
    @DisplayName("Spring ApplicationContext phải khởi động thành công")
    void contextLoads() {
        // Test thành công nếu không có exception được ném ra khi load context
    }
}
