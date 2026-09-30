# Stage 1: Build file JAR bằng Gradle Wrapper có sẵn trong project
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copy toàn bộ code vào container
COPY . .

# Cấp quyền và chạy lệnh build JAR (bỏ qua chạy test để build nhanh hơn)
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon -x test

# Stage 2: Chạy ứng dụng
FROM amazoncorretto:17-alpine
WORKDIR /app

# Gradle xuất file JAR vào thư mục build/libs/
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Xmx300m", "-jar", "app.jar"]
