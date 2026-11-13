FROM quay.io/wildfly/wildfly:39.0.1.Final-2-jdk21

# Raise JDK serialization filter limits (Imixs stores documents incl. file content as serialized maps)
ENV JDK_SERIAL_FILTER="maxbytes=1073741824;maxdepth=128;maxarray=1073741824;maxrefs=10000000"

LABEL description="Imixs-Process-Manager"
LABEL maintainer="ralph.soika@imixs.com"

# Copy EclipseLink and Postgres Driver
COPY ./docker/configuration/wildfly/modules/ /opt/jboss/wildfly/modules/

# Setup configuration
COPY ./docker/configuration/wildfly/*.properties /opt/jboss/wildfly/standalone/configuration/
COPY ./docker/configuration/wildfly/standalone.xml /opt/jboss/wildfly/standalone/configuration/

# Create Index directory
USER root
RUN mkdir -p /opt/jboss/lucene \
 && chown -R jboss:jboss /opt/jboss/lucene
USER jboss

# Deploy artefact
ADD ./target/*.war /opt/jboss/wildfly/standalone/deployments/
WORKDIR /opt/jboss
# Run with management interface
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
