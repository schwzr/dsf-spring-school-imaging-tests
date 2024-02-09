[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • [Exercise 3](exercise-3.md) • **Exercise 4** • [Exercise 5](exercise-5.md)
___

# Exercise 4 - Exclusive Gateways
Different execution paths in a process based on the state of process variables can be achieved using Exclusive Gateways. In Exercise 4 we will examine how this can be implemented by modifying the `dsfdev_helloDic` process.

In order to solve this exercise, you should have solved Exercise 3 and read the topics on
[Exclusive Gateways](basic-concepts-and-lessons.md#exclusive-gateways)
and [Conditions](basic-concepts-and-lessons.md#conditions).

Solutions to this exercise are found on the branch `solutions/exercise-4`.

## Exercise Tasks
1. In the `HelloDic` class, write an algorithm which decides whether the `dsfdev_helloCos` process should be started based on the start Task's input parameter `tutorial-input`.
2. Add a boolean variable to the process execution variables storing the decision.
3. Add an exclusive gateway to the `dsfdev_helloDic` process model and two outgoing sequence flows - the first starting process `dsfdev_helloDic`, the second stopping process `dsfdev_helloDic` without starting process `dsfdev_helloCos`.
4. Add condition expressions to each outgoing sequence flow based on the previously stored execution variable.

## Solution Verification
### Maven Build and Automated Tests
Execute a maven build of the `dsf-process-tutorial` parent module via:

```
mvn clean install -Pexercise-4
```

Verify that the build was successful and no test failures occurred.

### Process Execution and Manual Tests
To verify the `dsfdev_helloDic` and `dsfdev_helloCos` processes can be executed successfully, we need to deploy them into DSF instances and execute the `dsfdev_helloDic` process. The maven `install` build is configured to create a process jar file with all necessary resources and copy the jar to the appropriate locations of the docker dev setup.

1. Start the DSF FHIR server for the `Test_DIC` organization in a console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up dic-fhir
   ```
   Verify the DSF FHIR server started successfully.

2. Start the DSF BPE server for the `Test_DIC` organization in a second console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up dic-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `dsfdev_helloDic` process.

3. Start the DSF FHIR server for the `Test_COS` organization in a third at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up cos-fhir
   ```
   Verify the DSF FHIR server started successfully.

4. Start the DSF BPE server for the `Test_COS` organization in a fourth console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up cos-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `dsfdev_helloCos` process. 

5. Start the `dsfdev_helloDic` process by posting a specific FHIR [Task](http://hl7.org/fhir/R4/task.html) resource to the DSF FHIR server of the `Test_DIC` organization:
   Execute therefore the `main` method of the `dev.dsf.process.tutorial.TutorialExampleStarter` class to create the [Task](http://hl7.org/fhir/R4/task.html) resource needed to start the `dsfdev_helloDic` process.

   Verify that the `dsfdev_helloDic` process was executed successfully by the `Test_DIC` DSF BPE server and possibly the `dsfdev_helloCos` process by the `Test_COS` DSF BPE server, depending on whether decision of your algorithm based on the input parameter allowed to start the `dsfdev_helloDic` process.

___
[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • [Exercise 3](exercise-3.md) • **Exercise 4** • [Exercise 5](exercise-5.md)