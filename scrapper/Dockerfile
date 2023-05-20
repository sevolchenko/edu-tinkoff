FROM openjdk:17

ARG GITHUB_REPO
LABEL org.opencontainers.image.source=https://github.com/${GITHUB_REPO}

COPY target/scrapper-1.0-SNAPSHOT.jar /scrapper.jar

ENTRYPOINT ["java", "--enable-preview", "-jar", "scrapper.jar"]
