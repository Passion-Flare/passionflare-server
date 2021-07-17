# build stage
FROM maven:3-adoptopenjdk-11 as build-stage
COPY . .
RUN mvn clean package -P prod

# production stage
FROM adoptopenjdk:11 as production-stage
COPY --from=build-stage ./target/passion-0.0.1.jar app.jar
EXPOSE 8081
CMD mkdir passion-flare \
    && cd passion-flare \
    && mkdir Files \
    && cd Files \
    && mkdir historicaldata \
    && mkdir historicaldatabyweek \
    && cd / \
    && java -jar app.jar
