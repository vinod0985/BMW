ARG BASE_OS_IMAGE_URL=armdocker.rnd.ericsson.se/proj-ldc/common_base_os_release/sles
ARG BASE_OS_VERSION=5.7.0-34
ARG USER_ID=


# -------------------------------------------------------------------------------------------------
FROM ${BASE_OS_IMAGE_URL}:${BASE_OS_VERSION} AS baseimage-java
ARG BASE_OS_IMAGE_REPO_URL=arm.sero.gic.ericsson.se/artifactory/proj-ldc-repo-rpm-local/common_base_os/sles
ARG BASE_OS_VERSION
ARG CONTAINER_NAME

# Labels - Distribution Agent(DA)
ARG IMAGE_NAME
ARG RSTATE
ARG BUILD_DATE
ARG COMMIT_HASH
ARG IMAGE_NUMBER
ARG VERSION
ARG BASE_IMAGE_NUMBER
ARG BASE_IMAGE_VERSION

LABEL com.ericsson.product-number=$IMAGE_NUMBER \
      com.ericsson.product-revision=$RSTATE \
      org.opencontainers.image.title=$IMAGE_NAME \
      org.opencontainers.image.created=$BUILD_DATE \
      org.opencontainers.image.revision=$COMMIT_HASH \
      org.opencontainers.image.vendor="Ericsson" \
      org.opencontainers.image.version=$VERSION \
	  com.ericsson.base-image.product-name="Common Base OS SLES IMAGE" \
	  com.ericsson.base-image.product-number=$BASE_IMAGE_NUMBER \
	  com.ericsson.base-image.product-version=$BASE_IMAGE_VERSION
	  
	  
RUN zypper addrepo --gpgcheck-strict -f https://${BASE_OS_IMAGE_REPO_URL}/${BASE_OS_VERSION} COMMON_BASE_OS_SLES_REPO \
    && zypper --gpg-auto-import-keys refresh \
    && zypper install -l -y sudo java-17-openjdk-headless openssh-clients curl unzip wget catatonit  \
    && zypper removerepo COMMON_BASE_OS_SLES_REPO \
    && zypper clean --all \
    && zypper rm -y zypper > /dev/null \
    && rm -f /var/log/zypper.log

ENV JAVA_HOME /usr/lib64/jvm/java-17-openjdk-17


# -------------------------------------------------------------------------------------------------
FROM baseimage-java AS builder
ENV DA_HOME="/data/distribution_agent"
RUN mkdir -p /data/swgw_download_dir

ARG JAR_FILE=eric-swgwda-0.1.0-1.jar
COPY ${JAR_FILE} $DA_HOME/main/eric-swgwda-0.1.0-1.jar

WORKDIR $DA_HOME/

ENTRYPOINT ["java","-jar","main/eric-swgwda-0.1.0-1.jar"]