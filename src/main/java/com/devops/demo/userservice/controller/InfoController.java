package com.devops.demo.userservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller cung cấp thông tin về service đang chạy
 * Hữu ích để demo khi slide giảng dạy về Health Check và Observability
 */
@RestController
@RequestMapping("/api/v1")
public class InfoController {

    @Value("${spring.application.name:user-service}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String version;

    @Value("${app.description:User Management Microservice}")
    private String description;

    /**
     * Health check endpoint
     * GET /api/v1/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("service", appName);
        response.put("version", version);
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    /**
     * Info endpoint
     * GET /api/v1/info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("name", appName);
        response.put("version", version);
        response.put("description", description);
        response.put("environment",
                System.getenv().getOrDefault("SPRING_PROFILES_ACTIVE", "local"));
        response.put("java_version", System.getProperty("java.version"));
        response.put("endpoints", Map.of(
                "list_users",  "GET    /api/v1/users",
                "get_user",    "GET    /api/v1/users/{id}",
                "create_user", "POST   /api/v1/users",
                "update_user", "PUT    /api/v1/users/{id}",
                "delete_user", "DELETE /api/v1/users/{id}",
                "search",      "GET    /api/v1/users/search?q={keyword}",
                "filter_role", "GET    /api/v1/users/role/{ADMIN|LECTURER|STUDENT}",
                "health",      "GET    /api/v1/health"
        ));
        return ResponseEntity.ok(response);
    }
}
