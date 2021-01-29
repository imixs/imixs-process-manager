# Build Wildfly Prerelease Image


	$ docker build --tag=imixs/wildfly-jakarta-ee9 .


Run in debug mode:


	$ docker run --name="wildfly" 
			-p 8080:8080 -p 8787:8787 -p 9990:9990 \
			-e WILDFLY_PASS="admin_password" \
			-e DEBUG=true \
			imixs/wildfly-jakarta-ee9
	