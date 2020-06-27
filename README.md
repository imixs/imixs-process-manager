# The Imixs Process Manager


The digitization of business processes has become a key challenge for modern application design.
Essentially, you only need two things to do this.

 - a process description - which you ideally create using the BPMN 2.0 standard
 - a runtime environment - which allows you to execute and persist your process instances in a secure way

The Open Source project [Imixs-Workflow](http://www.imixs.org) provides you with an open and powerful workflow management platform that combines the design and the execution of business processes in a highly scalable and easy to use environment.

With the *Imixs Process Manager* you can start quickly and develop and test your own business process. And of course you can customize and extend this platform and use it for development as well as for production.


## How to Install

The *Imixs Process Manager* comes with a Docker profile which enables you to start within  seconds. If you haven't already installed Docker, follow the instructions [here](https://docs.docker.com/get-docker/).

Download the [docker-compose.yml]() file 

	version: "3.6"
	services:
	
	  imixs-db:
	    image: postgres:9.6.1
	    environment:
	      POSTGRES_PASSWORD: adminadmin
	      POSTGRES_DB: workflow-db
	    volumes: 
	      - dbdata:/var/lib/postgresql/data
	  
	  imixs-app:
	    image: imixs/imixs-process-manager
	    environment:
	      WILDFLY_PASS: adminadmin
	      DEBUG: "true"
	      POSTGRES_USER: "postgres"
	      POSTGRES_PASSWORD: "adminadmin"
	      POSTGRES_CONNECTION: "jdbc:postgresql://imixs-db/workflow-db"
	    ports:
	      - "8080:8080"
	
	volumes:
	  dbdata: 

and run

	$ docker-compose up
	
After a few seconds the Imixs Process Manager is up and running. You cann acces it from your web browser at: [http://localhost:8080/](http://localhost:8080/)


<img src="./screen-001.png" />





## Authentication and Authorization

Imixs-Workflow is a human-centric workflow engine which means that each actor need to authenticate against the service to interact. 

The default setup of the Imixs Process Manager provides a set of predefined users which can be used for testing purpose. The test users are stored in a separate user and roles properties file named 'sampleapp-roles.roperties' and 'sampleapp-users.properties'. The property files are configured in a file based security domain within the server configuration. See the following list of predefined test user accounts:

| User | Role | Password | |---------|------------------------|----------| | admin | IMIXS-WORKFLOW-Manager | adminadmin | | manfred | IMIXS-WORKFLOW-Manager | password | | alex | IMIXS-WORKFLOW-Manager | password | | anna | IMIXS-WORKFLOW-Author | password | | marty | IMIXS-WORKFLOW-Author | password | | melman | IMIXS-WORKFLOW-Author | password | | gloria | IMIXS-WORKFLOW-Author | password | | skipper | IMIXS-WORKFLOW-Author | password | | kowalski| IMIXS-WORKFLOW-Author | password | | private | IMIXS-WORKFLOW-Author | password | | rico | IMIXS-WORKFLOW-Author | password |

You can add additional accounts or change the default account later, by updated the files "_sampleapp-roles.properties_" and "_sampleapp-users.properties_". You can also configure a different custom security realm (e.g. LDAP or Database).

You will find more information about the security concept in the [Imixs-Workflow Deployent guide](https://www.imixs.org/doc/deployment/index.html).



	
	
## Process Design

You can define your own business process models using the [Imixs-BPMN modeller tool](https://www.imixs.org/doc/modelling/index.html) and you can upload and execute your models directly within Imixs Process Manager. General information about how to model can be found [here](https://www.imixs.org/doc/modelling/howto.html). 

The Imixs Process Manager allows you to define custom forms for your business process without programming. By defining an XML template, you can store forms directly in a BPMN 2.0 model.

<img src="https://raw.githubusercontent.com/imixs/imixs-process-manager/master/src/main/webapp/pages/model-example.png" />

Example:

	<?xml version="1.0"?>
	<imixs-form>
	  <imixs-form-section label="Order">
	    <item name="_orderid" type="text" label="Order ID:" />
	    <item name="_orderdate" type="date" label="Order Date:" />
	  </imixs-form-section>
	</imixs-form>

You can create and change your models at runtime without interrupting your workflow instance.

