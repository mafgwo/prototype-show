FROM openjdk:8-alpine
ADD target/prototype-show.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--prototype-show.work-dir=/data"]
