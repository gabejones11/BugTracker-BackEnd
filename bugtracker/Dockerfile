FROM adoptopenjdk/openjdk18:ubi as run
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn

COPY pom.xml .

#RUN ./mvnw dependency:go-offline -B
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B


COPY src src

RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM juniorjbn/openjdk18-openshift

#ARG DEPENDENCY=/app/target/dependency
#
#COPY --from=build $DEPENDENCY/BOOT-INF/lib /app/lib
#COPY --from=build $DEPENDENCY/META-INF /app/META-INF
#COPY --from=build $DEPENDENCY/BOOT-INF/classes /app

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "dev.gabriel.bugtracker.BugTrackerApplication"]