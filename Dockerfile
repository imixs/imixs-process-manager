FROM imixs/wildfly:1.2.9

# Imixs-Process-Manager
MAINTAINER ralph.soika@imixs.com

# add configuration files
COPY ./src/docker/configuration/wildfly/*.properties ${WILDFLY_CONFIG}/
COPY ./src/docker/configuration/wildfly/standalone.xml ${WILDFLY_CONFIG}/

# Copy sample application
COPY ./target/*.war $WILDFLY_DEPLOYMENT  

