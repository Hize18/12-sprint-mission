FROM --platform=$BUILDPLATFORM amazoncorretto:17 AS builder
#https://docs.docker.com/build/building/multi-platform/

WORKDIR /app

#COPY . .

COPY gradle ./gradle
COPY gradlew ./gradlew

COPY build.gradle settings.gradle ./

RUN ./gradlew dependencies

COPY src ./src
RUN ./gradlew build -x test # 테스트 제외

EXPOSE 80

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

CMD ["sh", "-c", "java $JVM_OPTS -jar /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]