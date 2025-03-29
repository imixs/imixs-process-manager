# Open Liberty

This folder contains configuration files used to build a docker container running Open Liberty.

To build the Docker Image run:

    $ mvn clean install -Pdocker-openliberty

After you have build the image you can start the corresponding Docker-Stack with:

    $ docker-compose -f docker-compose-openliberty.yaml up

and open the application in your browser:

    http://localhost:9080/

## Configuration

OpenLiberty is mainly configured by config file `server.xml` which is part of the Docker image. The file defines the Jakarta EE 10 feature.

### Database Configuration

The database pools needed by Imixs-Workflow are configured in the `<dataSource>` sections. Note that there are two datasources defined, one for the Imixs Workflow engine and one for the `EJBTimerDatabaseStore`. The later is required for the EJB TimerService to schedule workflow tasks.

The `persistentExecutor` defines a initialPollDelay of 30 seconds for a clean startup procedure.

### Application Security

OpenLiberty can be configured with various security domains. In the example setup we use a basic reigisry with userIDs and passwords defined directly in the `server.xml` file. For production you can choose any other security setup as Imixs-Workflow supports all Jakarta EE security standards.

### Mailing

Imixs-Workflow supports a feature to send E-Mail messages during a processing life-cycle through a SMTP gateway. For this feature you can configure a mailSession in OpenLiberty. See the section `<mailSession>` in the `server.xml` file for details.
