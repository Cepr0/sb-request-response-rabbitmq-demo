FROM openjdk:11-jre-slim
VOLUME /tmp
WORKDIR /service
COPY target/*.jar service.jar
EXPOSE 8080
ENTRYPOINT exec java \
-server \
-noverify \
-XX:TieredStopAtLevel=1 \
-Dspring.jmx.enabled=false \
$JAVA_OPTS \
-jar service.jar
