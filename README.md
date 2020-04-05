# SPS Friend Management

SPS Friend Management System is a list of exposed REST services to manage friendship between users.

## Before Testing

Before testing the requested APIs you need to register emails(users) to the system. For that need to call following api. Can register any number of users at once. 

```
url: https://friends-wre35pereq-de.a.run.app/friends/register
request boday: { "friends": [ "testuser1@example.com", "testuser2@example.com", "testuser3@example.com"] }
```

## Accessing the Product

Exposed APIs
```
1. https://friends-wre35pereq-de.a.run.app/friends/add
2. https://friends-wre35pereq-de.a.run.app/friends/list
3. https://friends-wre35pereq-de.a.run.app/friends/common
4. https://friends-wre35pereq-de.a.run.app/friends/subscribe
5. https://friends-wre35pereq-de.a.run.app/friends/block
6. https://friends-wre35pereq-de.a.run.app/friends/notify
```
To access the swagger documentation go to [https://friends-wre35pereq-de.a.run.app/swagger-ui.html](https://friends-wre35pereq-de.a.run.app/swagger-ui.html)

## Database Setup

For this I decided to use MYSql database and used Cloud SQL service provided by Google Cloud Platform (GCP). Created a new MYSql instance and created a schema named as friends.

```mysql
CREATE databse friends;
```
 Created a table for users. 
```mysql
CREATE TABLE `users` (
  `iduser` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`iduser`),
  UNIQUE KEY `username_UNIQUE` (`username`)
)
```
Created a table for friendships.
```mysql
CREATE TABLE `friendships` (
  `iduser` int(11) NOT NULL,
  `idfriend` int(11) NOT NULL,
  `blocked` bit(1) DEFAULT b'0',
  `idfriendship` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`idfriendship`),
  KEY `iduser_idx` (`iduser`),
  KEY `idfriend_idx` (`idfriend`),
  CONSTRAINT `idfriend` FOREIGN KEY (`idfriend`) REFERENCES `users` (`iduser`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `iduser` FOREIGN KEY (`iduser`) REFERENCES `users` (`iduser`) ON DELETE CASCADE ON UPDATE CASCADE
)
```

## Setting up a spring boot application
When creating the spring boot application I added following dependencies. 

To create database connection using JPA with mysql following dependencies were used.

```pom
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
<!-- MySQL -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>

```
In application.properties file add following properties
```properties
spring.datasource.url=jdbc:mysql://[DB_URL]/[SCHEMA_NAME]

spring.datasource.username=[USERNAME]
spring.datasource.password=[PASSWORD]

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL57Dialect
spring.jpa.generate-ddl=true
spring.jpa.show-sql=true

spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.initialization-mode=always
```
Following Java code is a sample code to create database object
```java
@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Column(nullable = false, unique = true)
    private String username;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer iduser;

    public User(String username) {
        this.username = username;
    }

}
```

To create web services used following spring web dependency.

```pom
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```
To create the swagger document used following dependencies and plugin
```pom
        <dependency>
            <groupId>io.swagger.core.v3</groupId>
            <artifactId>swagger-jaxrs2</artifactId>
            <version>2.1.1</version>
        </dependency>
        <dependency>
            <groupId>javax.ws.rs</groupId>
            <artifactId>javax.ws.rs-api</artifactId>
            <version>2.1</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.9.2</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.9.2</version>
        </dependency>

        <plugin>
            <groupId>io.swagger.core.v3</groupId>
            <artifactId>swagger-maven-plugin</artifactId>
            <version>2.1.1</version>
            <configuration>
                <resourcePackages>
                    <package>com.api.friendmanagement.controllers</package>
                </resourcePackages>
                <outputPath>${project.build.directory}</outputPath>
                <outputFileName>swagger</outputFileName>
                <outputFormat>JSONANDYAML</outputFormat>
                <prettyPrint>true</prettyPrint>
            </configuration>
            <executions>
                <execution>
                    <phase>compile</phase>
                    <goals>
                        <goal>resolve</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>       

```
A sample rest API code with swagger documentation is as follow.
```java
@ApiOperation(value = "API to create a friend connection between two email addresses", response = GeneralMessage.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully create friendship between 2 existing users"),
            @ApiResponse(code = 400, message = MessageConstant.BAD_REQUEST),
            @ApiResponse(code = 401, message = MessageConstant.UNAUTHORIZED),
            @ApiResponse(code = 500, message = MessageConstant.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = "/add")
    public ResponseEntity<GeneralMessage> addFriendships(@RequestBody Friends friends) {
            return new ResponseEntity<>(friendshipService.addFriendship(friend, friendWith), HttpStatus.OK);
    }
```

To automatically create get methods and set methods used lombok dependency.

```pom
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
```
For unit testing used following dependencies
```pom 
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
```

## Deploying the Product
To deploy the product used the Cloud Run service provided by GCP.
To deploy into Cloud Run need to create a docker image. So for that need to have a Dockerfile. The docker file is as follows.
```dockerfile
# Use the official maven/Java 8 image to create a build artifact.
# https://hub.docker.com/_/maven
FROM maven:3.5-jdk-8-alpine as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests

# Use AdoptOpenJDK for base image.
# It's important to use OpenJDK 8u191 or above that has container support enabled.
# https://hub.docker.com/r/adoptopenjdk/openjdk8
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
FROM adoptopenjdk/openjdk8:jdk8u202-b08-alpine-slim

# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/target/friend-management-*.jar /friend-management.jar

# Run the web service on container startup.
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-Dserver.port=${PORT}","-jar","/friend-management.jar"]
``` 
To deploy the product to Cloud Run need to install Google Cloud SDK and configure to the project. 

To build the docker image and deploy on Cloud Run following commands are executed
```bash
gcloud builds submit --tag gcr.io/[PROJECTID]/[DOCKER_IMAGE_NAME]

gcloud run deploy friend  --image gcr.io/[PROJECTID]/[DOCKER_IMAGE_NAME]

``` 
