FROM amazoncorretto:21
COPY target/*.jar shareit.jar
ENTRYPOINT ["java", "-jar", "/shareit.jar"]