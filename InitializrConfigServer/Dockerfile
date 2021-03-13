FROM bellsoft/liberica-openjdk-alpine:11 as build
WORKDIR /scratch
COPY . .
RUN ./gradlew --no-daemon build

FROM bellsoft/liberica-openjdk-alpine:11
WORKDIR /srv
COPY --from=build /scratch/build/libs/Steeltoe.InitializrConfigServer-*.jar Steeltoe.InitializrConfigServer.jar
EXPOSE 8888
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "Steeltoe.InitializrConfigServer.jar"]
