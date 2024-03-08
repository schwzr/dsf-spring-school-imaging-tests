# Basic Concepts and Guides

This is a centralized write-up of all concepts and guides you will need to solve the tutorial exercises.
Exercises will tell you which concepts or guides you need to be familiar with to be able to solve them 
but may also provide additional information concerning that concept to solve that specific exercise.  
Make sure you have read the [prerequisites](prerequisites.md).

***

## Disclaimer
The concept of `Tasks` exists in both the FHIR and BPMN domains. For this tutorial `Task resource` always refers
to [FHIR Tasks](https://www.hl7.org/fhir/R4/task.html) and `Service Task` always means the BPMN concept.

***

## DSF

This section will include all the DSF-specific functionality required to create basic process plugins.

### Tutorial Project

The tutorial project consists of three parts: 
1. A `test-data-generator` project used to generate X.509 certificates and FHIR resources
during the maven build of the project. The certificates and FHIR resources are needed to start DSF instances which simulate
installations at three different organizations. 
2. The DSF instances are created using [Docker](https://www.docker.com/) and configured using
a `docker-compose.yml` file in the `dev-setup` folder. The docker-compose dev setup uses a single PostgreSQL database server,
a single nginx reverse proxy, a keycloak instance as well as three separate DSF FHIR server instances and 3 separate DSF BPE server instances.
3. The `tutorial-process` project contains all resources (FHIR resources, BPMN models and Java code) for the actual
DSF process plugin. Java code for the `tutorial-process` project is located at `src/main/java`, FHIR resources and
BPMN process models at `src/main/resources` as well as prepared JUnit tests to verify your solution at `src/test/java`.

FHIR resources used in the DSF come formatted as XML. You can find them in the `tutorial-process/src/main/resources/fhir` directory.
When creating your own FHIR resources for DSF process plugins you also want to put them in a fitting subdirectory of `tutorial-process/src/main/resources/fhir`.
You may also use JSON instead of XML.

***

### Draft Task Resources

[Task](basic-concepts-and-guides.md#task) resources with status `draft` are used to create the DSF FHIR server's functionality
of starting processes via its web interface. They are stored in `.../tutorial-process/src/main/resources/fhir/Task`.
Compared to regular [Task](basic-concepts-and-guides.md#task) resources used to
start BPMN processes, this type of [Task](basic-concepts-and-guides.md#task) resource requires the status `draft` instead the usual `requested`.
It also replaces the value for `authoredOn` with the placeholder `#{date}`, the values of organization
identifiers with the placeholder `#{organization}` and all instances of version numbers with `#{version}`. 
Additionally, it requires setting the `Task.identifier`
element. It should look something like this:  

```xml
<identifier>
     <system value="http://dsf.dev/sid/task-identifier" />
     <value value="http://dsf.dev/bpe/Process/processKey/#{version}/task-name" />
</identifier>
```
`processKey` should be the same one used in [URLs](basic-concepts-and-guides.md#urls).  
`task-name` can be any String you wish to identify this task with. E.g. you can use the file name of the Draft Task. 

For a complete example you can take a look at the Draft Task Resource in one of the solution branches
and compare it to the one needed for cURL. The [Task](basic-concepts-and-guides.md#task) resource created
for cURL can be found at `.../tutorial-process/src/main/resources/example-task.xml`.  

You might also want to check out [this guide](basic-concepts-and-guides.md#creating-task-resources-based-on-a-definition) 
if you do not know how to create [Task](basic-concepts-and-guides.md#task) resources in general.

***