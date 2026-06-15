FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw || true

RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["java","-jar","target/calmora-app-0.0.1-SNAPSHOT.jar"]