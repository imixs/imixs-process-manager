# process-manager

In order to successfully implement a business process, you essentially need two things:

 - a process description which you ideally create using the BPMN 2.0 standard
 - a runtime environment which allows you to execute and to persist your process instances

The Open Source project [Imixs-Workflow](http://www.imixs.org) provides you with a powerful workflow management platform that combines the design and the execution of business processes in a highly scalable and easy to use environment.

With the Imixs Process Manager you can start quickly and develop and test your own models. And of course you can customize and extend this platform and use it for development as well as for production. Imixs-Workflow is based on [Jakarta EE](href="https://jakarta.ee/) providing you with a highly scalable server platform for modern microservice architecture.

You can define your own business process models using the [Imixs-BPMN modeller tool](https://www.imixs.org/doc/modelling/index.html) and you can upload and execute your models directly within Imixs Process Manager. General information about how to model can be found [here](https://www.imixs.org/doc/modelling/howto.html). 



## Process Design

The Imixs Process Manager allows you to define custom forms for your business process without programming. By defining an XML template, you can store forms directly in a BPMN 2.0 model.

Example:

	<?xml version="1.0"?>
	<imixs-form>
	  <imixs-form-section label="Order">
	    <item name="_orderid" type="text" label="Order ID:" />
	    <item name="_orderdate" type="date" label="Order Date:" />
	  </imixs-form-section>
	</imixs-form>

You can create and change your models at runtime without interrupting your workflow instance.

