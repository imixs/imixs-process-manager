# Glassfish / Payara

This folder contains configuration files used to build a docker container running Payara 4.1.2.

To build the Docker Image for Payara run:

	$ mvn clean install -Pdocker-build-payara
	
After you have build the payara image with the sample application you can start the corresponding Docker-Stack with:

	$ docker-compose -f docker-compose-payara.yml up
	
and run the sample applciation at:

	http://localhost:8080/
	
## Payara Web Console

You can open the payara web console on:

	https://localhost:4848/
	
	# userid: admin
	# password: admin


## Configuration

You can find the configuration details for the payara server in the config directory

	/src/docker/configuration/payara/

	