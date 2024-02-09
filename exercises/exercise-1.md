[Prerequisites](prerequisites.md) • **Exercise 1** • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • [Exercise 3](exercise-3.md) • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md)
___
## Disclaimer
The concept of `Tasks` exists in both the FHIR and BPMN domains. For this tutorial `Task resource` always refers
to [FHIR Tasks](https://www.hl7.org/fhir/R4/task.html) and `Service Task` always means the BPMN concept.
# Exercise 1 - Simple Process
The first exercise focuses on setting up the development environment used in this tutorial and shows how to implement and execute a simple
BPMN process.

With this exercise we will take a look at the general setup of the tutorial code base, modify a service class and execute 
the service within a simple demo process.  

We recommend you take a quick glance at all the topics in [Basic Concepts and Lessons](basic-concepts-and-lessons.md) to get a 
feel for the scope of this Tutorial.  
In order to solve this exercise, you need to have read the topics on [FHIR Task](basic-concepts-and-lessons.md#task), 
[Tutorial Project](basic-concepts-and-lessons.md#tutorial-project), [The Process Plugin Definition](basic-concepts-and-lessons.md#the-process-plugin-definition), 
[Spring Integration](basic-concepts-and-lessons.md#spring-integration), [Service Tasks](basic-concepts-and-lessons.md#service-tasks), 
[Service Delegates](basic-concepts-and-lessons.md#service-delegates),
[BPMN Process Execution](basic-concepts-and-lessons.md#bpmn-process-execution), [BPMN Process Variables](basic-concepts-and-lessons.md#bpmn-process-variables), 
[Accessing BPMN Process Variables](basic-concepts-and-lessons.md#accessing-bpmn-process-variables) and [Process Access Control](basic-concepts-and-lessons.md#process-access-control).

Solutions to this exercise are found on the branch `solutions/exercise-1`.

## Exercise Tasks
1. Add a log message to the `HelloDic#doExecute` method that logs the recipient organization identifier from the start [FHIR Task](http://hl7.org/fhir/R4/task.html) resource.

    <details>
        <summary>Don't know where to get a logger?</summary>
    
    This project uses slf4j. So use `LoggerFactory` to get yourself a logger instance.
    </details>
    
    <details>
        <summary>Can't find a way to get the start task?</summary>
    
    The `doExecute` method provides a `Variables` instance. Try it through this one.
    </details>
    
    <details>
        <summary>Don't know where to look for the identifier?</summary>
    
    Take a look at the official [FHIR Task](https://www.hl7.org/fhir/R4/task.html) resource, find elements that have a recipient and manoeuvre your way to those elements using the right getters. Then test which of them has the correct value.
    </details>

2. Register the `HelloDic` class as a prototype bean in the `TutorialConfig` class.
3. Set the `HelloDic` class as the service implementation of the appropriate service task within the `hello-dic.bpmn` process model.
4. Modify the ActivityDefinition for the `dsfdev_helloDic` process to only allow local clients to instantiate the process via a `helloDic` message.

    <details>
        <summary>Can't find the right code?</summary>
    
    Take a look at the [dsf-process-authorization](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem/dsf-process-authorization-1.0.0.xml) CodeSystem.
    </details>

## Solution Verification
### Maven Build and Automated Tests
Execute a maven build of the `dsf-process-tutorial` parent module via:
```
mvn clean install -Pexercise-1
```
Verify that the build was successful and no test failures occurred.

### Process Execution and Manual Tests
To verify the `dsfdev_helloDic` process can be executed successfully, we need to deploy it into a DSF instance and execute the process. The maven `install` build is configured to create a process jar file with all necessary resources and to copy the jar to the appropriate locations of the docker dev setup.

1. Start the DSF FHIR server for the `Test_DIC` organization in a console at location `.../dsf-process-tutorial/dev-setup`:
	```
	docker-compose up dic-fhir
	```
	Verify the DSF FHIR server started successfully. You can access the webservice of the DSF FHIR server at https://dic/fhir.  
	The DSF FHIR server uses a server certificate that was generated during the first maven install build. 
    To authenticate yourself to the server you can use the client certificate located at `.../dsf-process-tutorial/test-data-generator/cert/dic-client/dic-client_certificate.p12` (Password: `password`). 
    Add the certificate and the generated Root CA located at `.../dsf-process-tutorial/test-data-generator/cert/ca/testca_certificate.pem` to your browser certificate store.
	
	**Caution:** __If you add the generated Root CA to your browsers certificate store as a trusted Root CA, make sure you are 
    the only one with access to the private key at `.../dsf-process-tutorial/test-data-generator/cert/ca/testca_private-key.pem`.__

2. Start the DSF BPE server for the `Test_DIC` organization in a second console at location `.../dsf-process-tutorial/dev-setup`:
	```
	docker-compose up dic-bpe
	```
	Verify the DSF BPE server started successfully and deployed the `dsfdev_helloDic` process. 
    The DSF BPE server should print a message that the process was deployed. The DSF FHIR server should now have a new ActivityDefinition resource. Go to `https://dic/fhir/ActivityDefinition` to check if the expected resource was created by the BPE while deploying the process. The returned FHIR Bundle should contain a single ActivityDefinition. Also, go to `https://dic/fhir/StructureDefinition?url=http://dsf.dev/fhir/StructureDefinition/task-hello-dic` to check if the expected [FHIR Task](http://hl7.org/fhir/R4/task.html) profile was created.

3. Start the `dsfdev_helloDic` process by posting an appropriate [FHIR Task](http://hl7.org/fhir/R4/task.html) resource to the DSF FHIR server:

    The [FHIR Task](http://hl7.org/fhir/R4/task.html) resource is used to tell the DSF BPE server via the DSF FHIR server that a specific organization wants to start (or continue) one process instance at a specified organization. The needed [FHIR Task](http://hl7.org/fhir/R4/task.html) resource can be generated and posted to the DSF FHIR server by executing the `main` method of the `dev.dsf.process.tutorial.TutorialExampleStarter` class.
   
    For the TutorialExampleStarter to work the location of the client certificate and its password need to be specified:
	* Either specify the location and password via program arguments:
		1. location of the client certificate (`.../dsf-process-tutorial/test-data-generator/cert/dic-client/dic-client_certificate.p12`),
  		2. password for the client certificate (`password`)
    	* Or set the environment variables `DSF_CLIENT_CERTIFICATE_PATH` and `DSF_CLIENT_CERTIFICATE_PASSWORD` with the appropriate values.
	
    Verify that the  [FHIR Task](http://hl7.org/fhir/R4/task.html) resource could be created at the DSF FHIR server. Either look at your docker container log for the DIC FHIR server or find your [Task](basic-concepts-and-lessons.md#task) resource in the list of all [Task](basic-concepts-and-lessons.md#task) resources under https://dic/fhir/Task/. 
	
    Verify that the `dsfdev_helloDic` process was executed by the DSF BPE server. The BPE server should print a message showing that the process was started, print the log message you added to the `HelloDic` class and end with a message showing that the process finished.

___
[Prerequisites](prerequisites.md) • **Exercise 1** • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • [Exercise 3](exercise-3.md) • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md)
