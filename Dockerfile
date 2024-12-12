FROM openjdk:17-jdk-alpine
COPY target/*.jar employee-management-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "employee-management-service.jar"]
