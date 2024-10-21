FROM openjdk:21-slim
ARG JAR_FILE=taget/*.jar
COPY ./target/ShopKoi-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]