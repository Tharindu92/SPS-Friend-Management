FROM gizmotronic/oracle-java
# Copy local code to the container image.
WORKDIR /app
COPY friend-management-0.0.1-SNAPSHOT.jar /friend-management.jar

# Run the web service on container startup.
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-Dserver.port=${PORT}","-jar","/friend-management.jar"]


