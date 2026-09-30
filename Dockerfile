# ==========================================
# Stage 1: Build ứng dụng (Sử dụng JDK)
# ==========================================
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Step 1: Copy các file cấu hình Gradle trước để tận dụng Docker Cache
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Sửa lỗi ký tự xuống dòng Windows (CRLF -> LF) và tải trước thư viện
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

# Step 2: Copy toàn bộ mã nguồn và đóng gói JAR
COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# ==========================================
# Stage 2: Môi trường chạy app (Sử dụng JRE siêu nhẹ)
# ==========================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy duy nhất file JAR đã build từ Stage 1
COPY --from=build /app/build/libs/*.jar app.jar

# TỐI ƯU CHO RENDER FREE TIER (512MB RAM):
# Giới hạn Heap Memory khoảng 352MB để dành 160MB còn lại cho JVM Metaspace & OS,
# tránh bị Render kill tiến trình (Lỗi Exit Code 137 / OOM).
ENV JAVA_OPTS="-Xms256m -Xmx352m -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
