FROM openjdk:17
ARG JAR_FILE=build/libs/linkhub.jar
COPY ${JAR_FILE} ./linkhub.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "./linkhub.jar"]