FROM quay.io/wildfly/wildfly:27.0.1.Final-jdk17

LABEL description="Imixs-Process-Manager"
LABEL maintainer="ralph.soika@imixs.com"

# Copy EclipseLink and Postgres Driver
COPY ./docker/configuration/wildfly/modules/ /opt/jboss/wildfly/modules/

# Setup configuration
COPY ./docker/configuration/wildfly/*.properties /opt/jboss/wildfly/standalone/configuration/
COPY ./docker/configuration/wildfly/standalone.xml /opt/jboss/wildfly/standalone/configuration/

# Deploy artefact
ADD ./target/*.war /opt/jboss/wildfly/standalone/deployments/
WORKDIR /opt/jboss/wildfly
# Run with management interface
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
