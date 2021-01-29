FROM openjdk:11

MAINTAINER Ralph Soika <ralph.soika@imixs.com>

USER root


# Set the working directory
WORKDIR /opt

# set environments 
# https://download.jboss.org/wildfly/22.0.0.Alpha1/wildfly-preview-22.0.0.Alpha1.tar.gz
ENV WILDFLY_VERSION 22.0.0.Alpha1
ENV WILDFLY_HOME=/opt/wildfly
ENV WILDFLY_DEPLOYMENT=$WILDFLY_HOME/standalone/deployments
ENV WILDFLY_CONFIG=$WILDFLY_HOME/standalone/configuration
ENV DEBUG=false


# Add the glassfish distribution to /opt
#RUN curl -L https://www.eclipse.org/downloads/download.php?file=/ee4j/glassfish/glassfish-6.0.0-M1.zip&mirror_id=1285 | unzip
RUN wget https://www.eclipse.org/downloads/download.php?file=/ee4j/glassfish/glassfish-6.0.0-M1.zip&mirror_id=1285 
	
	
# unzip file.zip -d destination_folder



# Expose the ports we're interested in
EXPOSE 8080 4848

#CMD ["/opt/wildfly/wildfly_start.sh"]