# ====================================================
# GIAI ĐOẠN 1: BUILD (Biên dịch và đóng gói ứng dụng)
# ====================================================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Copy pom.xml trước để cache dependencies layer riêng
# → Nếu chỉ sửa code Java mà không sửa pom.xml, Docker bỏ qua tải thư viện (rất nhanh!)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code và build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ====================================================
# GIAI ĐOẠN 2: RUNTIME (Chạy ứng dụng Production)
# ====================================================
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Nguyen Cong Huong <huong@devops.edu.vn>"
LABEL description="User Service Microservice - BE-105 DevOps"
LABEL version="1.0.0"

WORKDIR /app

# Biến môi trường cho JVM (tự điều chỉnh theo RAM container)
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Copy duy nhất file JAR từ stage builder → không có source code, không có Maven
COPY --from=builder /build/target/user-service.jar app.jar

# Tạo user không có quyền root để tăng bảo mật (Principle of Least Privilege)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser:appgroup

# Khai báo port mà ứng dụng lắng nghe (để Docker biết)
EXPOSE 8080

# Health check: Docker sẽ tự kiểm tra container còn sống không mỗi 30 giây
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD wget -qO- http://localhost:8080/api/v1/health || exit 1

# Câu lệnh chạy ứng dụng
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
