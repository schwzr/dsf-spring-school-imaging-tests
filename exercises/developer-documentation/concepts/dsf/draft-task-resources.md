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