FROM gradle:jdk11 as resolved-dependencies

ENV GRADLE_USER_HOME /home/gradle/cache

WORKDIR /home/gradle/app/
RUN mkdir -p ${GRADLE_USER_HOME}
COPY ./build.gradle.kts /home/gradle/app/build.gradle.kts
RUN gradle downloadDependencies
