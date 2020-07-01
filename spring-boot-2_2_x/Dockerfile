# build stage
FROM openjdk:14.0.1-jdk-slim as builder

ARG VERSION
ARG MODULE_NAME

LABEL maintainer="opklnm102" version=${VERSION}

ENV APP_HOME=/home/app
WORKDIR ${APP_HOME}

RUN echo "${MODULE_NAME}, ${VERSION}"

## dependencies
COPY build.gradle settings.gradle gradlew ${APP_HOME}/
COPY gradle ${APP_HOME}/gradle

RUN ./gradlew build || return 0

## build binary
COPY ${MODULE_NAME} ${APP_HOME}/${MODULE_NAME}
RUN ./gradlew :${MODULE_NAME}:build


# runtime stage
FROM openjdk:14.0.1-jdk-slim

ARG MODULE_NAME
ENV APP_HOME=/home/app
RUN useradd --create-home app
USER app
WORKDIR ${APP_HOME}

COPY --from=builder --chown="app:app" ${APP_HOME}/${MODULE_NAME}/build/libs/*.jar ./app.jar
COPY --from=builder --chown="app:app" ${APP_HOME}/${MODULE_NAME}/docker-entrypoint.sh ./docker-entrypoint.sh

EXPOSE 8080
ENTRYPOINT ["/home/app/docker-entrypoint.sh"]
