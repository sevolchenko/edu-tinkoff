FROM openjdk:17

ARG GITHUB_REPO
LABEL org.opencontainers.image.source=https://github.com/${GITHUB_REPO}

COPY target/bot-1.0-SNAPSHOT.jar /bot.jar

ENTRYPOINT ["java", "--enable-preview", "-jar", "bot.jar"]
