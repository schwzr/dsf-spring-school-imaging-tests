[Prerequisites](prerequisites.md) • **Exercise 1** • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • [Exercise 2.1](exercise-2-1.md) • [Exercise 3](exercise-3.md) • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md)
___
## Disclaimer
The concept of `Tasks` exists in both the FHIR and BPMN domains. For this tutorial `Task resource` always refers
to [FHIR Tasks](https://www.hl7.org/fhir/R4/task.html) and `Service Task` always means the BPMN concept.
# Exercise 1 - Simple Process
The first exercise focuses on setting up the development environment used in this tutorial and shows how to implement and execute a simple
BPMN process.

With this exercise we will take a look at the general setup of the tutorial code base, modify a service class and execute 
the service within a simple demo process.  

We recommend you take a quick glance at all the topics in [Basic Concepts and Guides](basic-concepts-and-guides.md) to get a 
feel for the scope of this Tutorial.  
In order to solve this exercise, you need to have read the topics on [FHIR Task](basic-concepts-and-guides.md#task), 
[Tutorial Project](basic-concepts-and-guides.md#tutorial-project), [The Process Plugin Definition](basic-concepts-and-guides.md#the-process-plugin-definition), 
[Spring Integration](basic-concepts-and-guides.md#spring-integration), [Service Tasks](basic-concepts-and-guides.md#service-tasks), 
[Service Delegates](basic-concepts-and-guides.md#service-delegates),
[BPMN Process Execution](basic-concepts-and-guides.md#bpmn-process-execution), [BPMN Process Variables](basic-concepts-and-guides.md#bpmn-process-variables), 
[Accessing BPMN Process Variables](basic-concepts-and-guides.md#accessing-bpmn-process-variables),
[Process Access Control](basic-concepts-and-guides.md#process-access-control) 
and [Starting a Process via Task Resources](basic-concepts-and-guides.md#starting-a-process-via-task-resources).

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
5. In order to start your process you need to either create a regular [Task](basic-concepts-and-guides.md#task) resource
    or a [Draft Task Resource](basic-concepts-and-guides.md#draft-task-resources). Based on whether you would like
    to use cURL or the DSF FHIR server's web interface for starting processes you can do one of the following
    assignments (although we invite you to do both):
   * Create a [Task](basic-concepts-and-guides.md#task) resource in `tutorial-process/src/main/resources/fhir/example-task.xml` based on the [Task](basic-concepts-and-guides.md#task)
     profile `tutorial-process/src/main/resources/fhir/StructureDefinition/task-hello-dic.xml`.  
     You will need it to start your process via cURL.
   
        <details>
        <summary>Don't know how to create Task resources?</summary>

        Take a look at [this guide](basic-concepts-and-guides.md#creating-task-resources-based-on-a-definition).
        </details>
   * Create a [Draft Task Resource](basic-concepts-and-guides.md#draft-task-resources). You will need to be able
    to create [Task](basic-concepts-and-guides.md#task) resources as a prerequisite. If you don't know how to do this, 
    we recommend checking out the cURL method first and revisiting this assignment after that.

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

3. Start the `dsfdev_helloDic` process by posting an appropriate [FHIR Task](http://hl7.org/fhir/R4/task.html) resource to the DSF FHIR server using either cURL or the DSF FHIR server's web interface. Check out [Starting A Process Via Task Resources](basic-concepts-and-guides.md#starting-a-process-via-task-resources) again if you are unsure.  
	
    Verify that the  [FHIR Task](http://hl7.org/fhir/R4/task.html) resource could be created at the DSF FHIR server. Either look at your docker container log for the DIC FHIR server or find your [Task](basic-concepts-and-guides.md#task) resource in the list of all [Task](basic-concepts-and-guides.md#task) resources under https://dic/fhir/Task/. 
	
    Verify that the `dsfdev_helloDic` process was executed by the DSF BPE server. The BPE server should print a message showing that the process was started, print the log message you added to the `HelloDic` class and end with a message showing that the process finished.

___
[Prerequisites](prerequisites.md) • **Exercise 1** • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • [Exercise 2.1](exercise-2-1.md) • [Exercise 3](exercise-3.md) • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md)
