package com.devops.demo.userservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/pay")
public class PaymentController {

    @GetMapping("/process")
    public String processPayment() {
        log.info("Người dùng bắt đầu quá trình thanh toán...");

        try {
            simulateError();
        } catch (Exception e) {
            log.error("Lỗi xảy ra trong lúc thanh toán: {}", e.getMessage());
        }

        return "Xử lý thanh toán hoàn tất (xem log trong thư mục logs)";
    }

    private void simulateError() {
        log.info("Đang kết nối đến ngân hàng...");
        throw new NullPointerException("Không tìm thấy thông tin tài khoản");
    }
}
