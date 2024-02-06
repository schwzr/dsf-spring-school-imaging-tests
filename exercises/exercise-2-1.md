[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • **Exercise 2.1** • [Exercise 3](exercise-3.md) • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md)
___

# Exercise 2 - DSF User Authentication

In [Exercise 1](exercise-1.md), you added a client certificate to your browser in order to be allowed to access the DSF FHIR
server. In later exercises we will also use some of the DSF installations like the COS or HRP. You could generate a 
client certificate for each one, which is very cumbersome, or you could configure yourself a specific DSF user
with access to all DSF installations using [OpenID Connect](https://openid.net/developers/how-connect-works/).

In order to solve this 

## Exercise Tasks

## Solution Verification
### Maven Build and Automated Tests
Execute a maven build of the `dsf-process-tutorial` parent module via:

```
mvn clean install -Pexercise-2
```

Verify that the build was successful and no test failures occurred.

### Process Execution and Manual Tests
To verify the `dsfdev_helloDic` process can be executed successfully, we need to deploy it into a DSF instance and execute the process. The maven `install` build is configured to create a process jar file with all necessary resources and copy the jar to the appropriate locations of the docker dev setup.

1. Start the DSF FHIR server for the `Test_DIC` organization in a console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up dic-fhir
   ```
   Verify the DSF FHIR server started successfully.

2. Start the DSF BPE server for the `Test_DIC` organization in second console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up dic-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `dsfdev_helloDic` process.

3. Start the `dsfdev_helloDic` process by posting an appropriate FHIR [Task](http://hl7.org/fhir/R4/task.html) resource to the DSF FHIR server of the `Test_DIC` organization:
   Execute the `main` method of the `dev.dsf.process.tutorial.TutorialExampleStarter` class as in [exercise 1](exercise-1.md) to create the [Task](http://hl7.org/fhir/R4/task.html) resource needed to start the `dsfdev_helloDic` process.

   Verify that the `dsfdev_helloDic` process was executed by the DSF BPE server. The BPE server should:
    * Print a message showing that the process was started.
    * If logging is enabled - print the log message and the value of the input parameter you added to the `HelloDic`
      implementation.
    * Print a message showing that the process finished.

Check that you can disable logging of you message by modifying the `docker-compose.yml` file and configuring your environment variable with the value `"false"` or removing the environment variable.  
_Note: Changes to environment variable require recreating the docker container._

Also check that modification to the [Task](http://hl7.org/fhir/R4/task.html) input parameter specified in the `TutorialExampleStarter` class, have the appropriate effect on your log message.

___
[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • **Exercise 2.1** • [Exercise 3](exercise-3.md) • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md)