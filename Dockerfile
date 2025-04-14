# 1. JDK 17이 포함된 가벼운 베이스 이미지 사용
FROM arm64v8/eclipse-temurin:17-jdk-jammy

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 시간대를 Asia/Seoul로 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 4. 빌드된 JAR 파일을 지정된 경로에서 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 5. 애플리케이션이 사용하는 포트 노출
EXPOSE 8080

# 6. 컨테이너 실행 시 명령어
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]
