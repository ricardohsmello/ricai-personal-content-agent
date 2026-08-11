FROM maven:3.9-eclipse-temurin-21 AS build

RUN apt-get update && apt-get install -y git && rm -rf /var/lib/apt/lists/*

# Build Spring AI com o fix da PR
WORKDIR /spring-ai

RUN git clone --depth 1 \
    --branch mongo-chat-memory-ordering \
    https://github.com/MacAlsandair/spring-ai.git .

RUN ./mvnw \
    -pl :spring-ai-bom,\
:spring-ai-starter-model-chat-memory-repository-mongodb,\
:spring-ai-starter-model-openai,\
:spring-ai-starter-vector-store-mongodb-atlas,\
:spring-ai-vector-store-advisor \
    -am clean install -DskipTests

# Build da sua aplicação
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]