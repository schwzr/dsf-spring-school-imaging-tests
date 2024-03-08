### Creating ValueSets for DSF Processes

You might find yourself in the situation where you need to create a [ValueSet](basic-concepts-and-guides.md#valueset).
For example, when adding [Input Parameters](basic-concepts-and-guides.md#task-input-parameters) to DSF [Task](basic-concepts-and-guides.md#task)
resources, you will also have to reference a [ValueSet](basic-concepts-and-guides.md#valueset) resource in your
binding for `Task.input.type` to be able to set the type of your [Input Parameter](basic-concepts-and-guides.md#task-input-parameters).
[ValueSets](basic-concepts-and-guides.md#valueset) for the DSF differ from regular [ValueSets](basic-concepts-and-guides.md#valueset)
in that some element's values are managed by the DSF BPE server. You can use the following template for your
[ValueSet](basic-concepts-and-guides.md#valueset):
```xml
<ValueSet xmlns="http://hl7.org/fhir">
    <meta>
        <tag>
            <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag" />
            <code value="ALL" />
        </tag> 
    </meta>
    <url value="http://dsf.dev/fhir/ValueSet/my-value-set"/>    <!--dummy value-->
    <!-- version managed by bpe -->
    <version value="#{version}" />
    <name value="My ValueSet"/>     <!--dummy value-->
    <title value="My ValueSet Title"/>  <!--dummy value-->
    <!-- status managed by bpe -->
    <status value="unknown" />
    <experimental value="false"/>
    <!-- date managed by bpe -->
    <date value="#{date}"/>
    <publisher value="DSF"/>    <!--dummy value-->
    <description value="ValueSet with all codes from my-code-system"/>      <!--dummy value-->
    <immutable value="true"/>
    <compose>
        <include>
            <system value="http://dsf.dev/fhir/CodeSystem/my-code-system"/>     <!--dummy value-->
            <version value="#{version}"/>   
        </include>  
    </compose>
</ValueSet> 
```
Replace dummy values with appropriate values of your own. Do not change elements managed by the DSF BPE server.  
The `compose` element defines the codes included in this [ValueSet](basic-concepts-and-guides.md#valueset).
It holds at least one `include` element. Each `include` element refers to a [CodeSystem](basic-concepts-and-guides.md#codesystem)
and contains a list of `concept` elements which in turn contain the actual `code` element.
Using one code from `my-code-system` and one code from `my-other-code-system` would result in the following `compose` element:
```xml
<ValueSet xmlns="http://hl7.org/fhir">
    ...
    <compose>
        <include>
            <system value="http://dsf.dev/fhir/CodeSystem/my-code-system"/>
            <version value="#{version}"/>   
            <concept>
                <code value="my-code"/>
            </concept>
        </include>  
        <include>
            <system value="http://dsf.dev/fhir/CodeSystem/my-other-code-system"/>
            <version value="#{version}"/>
            <concept>
                <code value="my-other-code"/>
            </concept>
        </include>
    </compose>
</ValueSet>
```
The DSF BPE server will read your [ValueSet](basic-concepts-and-guides.md#valueset) from
`tutorial-process/src/main/resources/fhir/ValueSet`.

You might also want to check out [this guide](basic-concepts-and-guides.md#creating-codesystems-for-the-dsf-processes)
on how to create [CodeSystems](basic-concepts-and-guides.md#codesystem).