FROM openjdk:8-jdk-alpine
RUN apk add --no-cache curl unzip
RUN mkdir /opt/sonar-scanner
RUN curl --location --silent https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.6.2.2472.zip -o /tmp/sonar-scanner.zip && \
    unzip /tmp/sonar-scanner.zip -d /opt/ && \
    mv /opt/sonar-scanner-4.6.2.2472 /opt/sonar-scanner && \
    rm /tmp/sonar-scanner.zip
ENV PATH "$PATH:/opt/sonar-scanner/bin"
WORKDIR /app
