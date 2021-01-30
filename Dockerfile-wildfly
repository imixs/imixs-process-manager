FROM jboss/wildfly:20.0.1.Final

LABEL description="Imixs-Process-Manager"
LABEL maintainer="ralph.soika@imixs.com"

# Copy EclipseLink
COPY ./docker/configuration/wildfly/modules/ /opt/jboss/wildfly/modules/

# Setup configuration
COPY ./docker/configuration/wildfly/*.properties /opt/jboss/wildfly/standalone/configuration/
COPY ./docker/configuration/wildfly/standalone.xml /opt/jboss/wildfly/standalone/configuration/

# Deploy artefact
ADD ./target/*.war /opt/jboss/wildfly/standalone/deployments/

# Run with management interface
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
