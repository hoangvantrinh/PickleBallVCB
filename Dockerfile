# Stage 1: Build dự án bằng Docker Image cài sẵn Gradle (Không cần wrapper/gradlew)
FROM gradle:8-jdk17-alpine AS build
WORKDIR /app

# Copy toàn bộ mã nguồn vào container
COPY . .

# Build bằng lệnh gradle chính thức
RUN gradle bootJar --no-daemon -x test

# Stage 2: Môi trường chạy siêu nhẹ cho Render
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy file JAR từ Stage 1
COPY --from=build /app/build/libs/*.jar app.jar

# Tối ưu RAM cho gói Free của Render (512MB)
ENV JAVA_OPTS="-Xms256m -Xmx352m -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
