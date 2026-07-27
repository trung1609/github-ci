# 🚀 user-service — Demo Project cho Khóa học CI/CD [BE-105] DevOps

Dự án **Spring Boot Microservice** được thiết kế để thực hành trực tiếp các bài học CI/CD.

---

## 🏗️ Kiến trúc dự án

```
user-service/
├── src/
│   ├── main/
│   │   ├── java/com/devops/demo/userservice/
│   │   │   ├── UserServiceApplication.java    ← Điểm khởi động
│   │   │   ├── controller/
│   │   │   │   ├── UserController.java        ← REST API endpoints
│   │   │   │   └── InfoController.java        ← Health & Info endpoints
│   │   │   ├── service/
│   │   │   │   └── UserService.java           ← Business Logic
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java        ← Tương tác Database
│   │   │   ├── entity/
│   │   │   │   └── User.java                  ← Database Entity
│   │   │   ├── dto/
│   │   │   │   ├── UserDto.java               ← Request/Response DTOs
│   │   │   │   └── ApiResponse.java           ← Chuẩn hóa API response
│   │   │   └── exception/
│   │   │       ├── ResourceNotFoundException.java
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       ├── application.properties         ← Cấu hình local
│   │       └── data.sql                       ← Dữ liệu mẫu
│   └── test/
│       ├── java/
│       │   ├── UserServiceApplicationTests.java   ← Context load test
│       │   ├── service/UserServiceTest.java        ← Unit Tests (Mockito)
│       │   └── controller/UserControllerTest.java  ← Integration Tests (MockMvc)
│       └── resources/application-test.properties
├── .github/workflows/
│   ├── basic-flow.yml        ← Workflow cơ bản (Session 07)
│   └── ci-cd-docker.yml      ← Workflow đầy đủ + Docker (Session 08)
├── Dockerfile                ← Multi-stage build
├── docker-compose.yml        ← Chạy local với Docker Compose
├── pom.xml
└── README.md
```

---

## 🛠️ Yêu cầu môi trường

| Tool | Phiên bản |
|------|-----------|
| JDK  | 17+ (Temurin/OpenJDK) |
| Maven | 3.9+ |
| Docker | 24+ |
| Git | 2.40+ |

---

## ▶️ Cách chạy

### 1. Chạy bằng Maven (chỉ cần JDK)

```bash
# Clone dự án
git clone <your-repo-url>
cd user-service

# Chạy tests
mvn test

# Build và chạy ứng dụng
mvn spring-boot:run
```

Ứng dụng chạy tại: **http://localhost:8080**

### 2. Chạy bằng Docker Compose

```bash
# Build image và khởi động container
docker compose up --build

# Chạy ở background
docker compose up -d --build

# Xem logs
docker compose logs -f user-service

# Tắt
docker compose down
```

### 3. Build Docker image thủ công

```bash
# Build
docker build -t user-service:local .

# Chạy
docker run -p 8080:8080 user-service:local

# Xem kích thước image
docker images user-service
```

---

## 📡 API Endpoints

| Method | URL | Mô tả |
|--------|-----|-------|
| `GET` | `/api/v1/users` | Lấy tất cả users |
| `GET` | `/api/v1/users/{id}` | Lấy chi tiết user |
| `POST` | `/api/v1/users` | Tạo user mới |
| `PUT` | `/api/v1/users/{id}` | Cập nhật user |
| `DELETE` | `/api/v1/users/{id}` | Xóa mềm user |
| `GET` | `/api/v1/users/search?q=nguyen` | Tìm kiếm user |
| `GET` | `/api/v1/users/role/STUDENT` | Lọc theo role |
| `GET` | `/api/v1/health` | Health check |
| `GET` | `/api/v1/info` | Thông tin service |
| `GET` | `/h2-console` | H2 Database UI (local only) |

### Ví dụ cURL

```bash
# Lấy danh sách users
curl http://localhost:8080/api/v1/users

# Tạo user mới
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Sinh viên Mới","email":"new@student.edu.vn","role":"STUDENT"}'

# Tìm kiếm user
curl "http://localhost:8080/api/v1/users/search?q=Nguyen"

# Health check
curl http://localhost:8080/api/v1/health
```

---

## 🔬 Chạy Tests

```bash
# Chạy tất cả tests
mvn test

# Chạy một test class cụ thể
mvn test -Dtest=UserServiceTest

# Xem báo cáo test
# → target/surefire-reports/*.txt
```

---

## 🔗 Tích hợp CI/CD

Dự án đã có sẵn 2 workflow GitHub Actions:

| Workflow | File | Khi nào kích hoạt |
|----------|------|-------------------|
| Basic CI | `.github/workflows/basic-flow.yml` | Push vào `main`, `develop` |
| Full CI/CD + Docker | `.github/workflows/ci-cd-docker.yml` | Push + Tag `v*.*.*` |

Để dùng workflow Docker, cần thêm repository vào GitHub và push lên.
Docker image sẽ được publish lên **GitHub Container Registry (ghcr.io)** tự động.
