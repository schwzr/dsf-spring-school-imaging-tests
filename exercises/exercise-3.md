[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • **Exercise 3** • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md) • [Exercise 6](exercise-6.md)
___

# Exercise 3 - DSF User Authentication and Authorization

In [Exercise 1](exercise-1.md), you added a client certificate to your browser in order to be allowed to access the DIC FHIR
server. In later exercises we will also use some of the DSF installations like the COS or HRP. You could add a 
client certificate to your browser for each one, or you could configure yourself a specific DSF user
with access to all DSF installations using [OpenID Connect](https://openid.net/developers/how-connect-works/).  
The tutorial project provides a keycloak instance for this purpose with the administration console accessible under https://keycloak:8443.
Credentials for administrator access are `username: admin` and `password: admin`.

In order to solve this exercise, you need to have read the documentation on [Access Control](https://dsf.dev/stable/maintain/fhir/access-control.html) 
and [ActivityDefinitions](basic-concepts-and-guides.md#activitydefinition).

## Exercise Tasks

1. In the Keycloak administrator console, create a new realm role called `tutorial` in the `cos`, `dic` and `hrp` realms.
2. In the Keycloak administrator console, create a new user in the `cos`, `dic` and `hrp` realms with the new `tutorial` role. 
   This will be your credentials to access all DSF FHIR server instances.
3. Add a new role to the `DEV_DSF_FHIR_SERVER_ROLECONFIG` for all FHIR server instances in [docker-compose.yml](../dev-setup/docker-compose.yml). It should match any user with `token-role` equal to
   `tutorial` and have `dsf-roles` `CREATE`, `READ`, `UPDATE`, `DELETE`, `SEARCH` and `HISTORY`. Finally, the role should also have the practitioner role `DSF_ADMIN`.
4. Change the `requester` element in the ActivityDefinition `hello-dic.xml` to allow all local clients with a practitioner role of `DSF_ADMIN` to request `helloDic` messages.
   

## Solution Verification
### Maven Build and Automated Tests
Execute a maven build of the `dsf-process-tutorial` parent module via:

```
mvn clean install -Pexercise-3
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

3. Start the `dsfdev_helloDic` process by posting an appropriate FHIR [Task](http://hl7.org/fhir/R4/task.html) resource to the DSF FHIR server of the `Test_DIC` organization using either cURL or the DSF FHIR server's web interface. Check out [Starting A Process Via Task Resources](basic-concepts-and-guides.md#starting-a-process-via-task-resources) again if you are unsure.

   Verify that the `dsfdev_helloDic` process was executed by the DSF BPE server. The BPE server should:
    * Print a message showing that the process was started.
    * If logging is enabled - print the log message and the value of the input parameter you added to the `HelloDic`
      implementation.
    * Print a message showing that the process finished.

Check that you can disable logging of you message by modifying the `docker-compose.yml` file and configuring your environment variable with the value `"false"` or removing the environment variable.  
_Note: Changes to environment variable require recreating the docker container._

Also check that modification to the [Task](http://hl7.org/fhir/R4/task.html) input parameter specified in the `TutorialExampleStarter` class, have the appropriate effect on your log message.

___
[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • **Exercise 3** • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md) • [Exercise 6](exercise-6.md)