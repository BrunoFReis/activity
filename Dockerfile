FROM eclipse-temurin:17-jre-focal

COPY build/libs/*.jar /opt/app/

CMD java -jar /opt/app/application.jar