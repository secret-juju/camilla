FROM openjdk:12-alpine
COPY ./build/libs/*.jar camillia.jar
ENTRYPOINT ["java", "-Xmx200m", "-jar", "-Duser.timezone=Asia/Seoul", "/camillia.jar"]