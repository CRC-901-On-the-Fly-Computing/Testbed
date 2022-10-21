# Using the Project Generator

## Our project setup

In the Testbed we are using an API-first driven approach. That is, that we specify the API before we write the logic and then generate the corresponding server-side code.
We use the Open-API format to specify the client api and then use the swagger generator to generate the server code.

Hence, every project has 2 basic source folders:
1) build/src/main/java
2) src/main/java

The first folder is the generated server code, this code will be re-generated before the gradle build command, hence changing this code is useless.

The second folder is obviously the implementation logic code, this folder is not overwritten or anything.

### How it works
As mentioned above, we use the swagger codegen project to generate our spring server. Actually, we use the official gradle plugin to generate our code (https://github.com/int128/gradle-swagger-generator-plugin).
To get a grip on how this plugin works, let's have a look on a common configuration of this plugin within our project:

```
swaggerSources {
    project {
        inputFile = file("${projectDir}/specifications/example_service_specification.yaml")
        code {
            language = 'spring'
            outputDir = buildOutputDir
            configFile = file("${projectDir}/specifications/config.json")
            templateDir = file("${projectDir}/specifications/templates")
            components = ["apis", "supportingFiles"]
            rawOptions = ["--import-mappings", "Review=de.upb.crypto.react.acs.review.impl.react.ReactRepresentableReview, ResponseMessage=de.upb.crc901.otftestbed.commons.rest.SimpleJSONMessage, ServiceReputationList=de.upb.crc901.otftestbed.commons.reputation.ServiceReputationList, StatisticDefinitionModel=de.upb.crc901.otftestbed.commons.reputation.StatisticDefinitionModel, ServiceReputation=de.upb.crc901.otftestbed.commons.reputation.ServiceReputation", "--additional-properties", "baseImplementationPackage=de.upb.crc901.otftestbed.review_board.impl"]
        }
    }
}
```
The first important thing is the input OpenAPI file, this is located in the subfolder specifications. In this file you can specify the REST-API.
The next important thing is that we do not want swagger-codegen to generate our model-code (if you want that you have to include the "model" flag in the list of components). The model code are the input and output definitions you specify in the OpenAPI file. Swagger can generate stub-classes for these, however, we often share these classes among other projects, thus we tell the swagger-codegen plugin to use our classes instead.
This works via the import mapping flag. There you have to map a swagger definition tag to the fully classified class-name of the model (this class has to be in the classpath obviously).


### Changing the process
As you might have already noticed, there are several other files in the specifications folder. The most important folder here is the templates folder. Since swagger chose to regenerate the application.properties in each process, we have to substitute it every time. Thus, you should edit the application.properties of the specification/templates folder instead of trying to create your own application.properties. The next files are for editing the generated code. You can read this up ont the swagger codegen site but basically, how it works is that you can edit the generated server-side code with these files. We use this for injecting start-parameters or spring-beans.


## Generating a project

Since there are a lot of config files and we do not want you to copy and paste all this stuff everytime, we chose to write a generator for our projects. This generator is located in the template folder.
There are just three things that you have to do:
1) Edit the config file in the config folders
2) Edit the OpenAPI file in the config folders
3) Run ./gradlew generateProject

Now you should find your project folder in the build folder. Please note that this project won't compile yet as it isn't entered in the settings.gradle.
So the next thing you should do is to move it in the right directory (the one you specified in the config file) and enter it in the settings.gradle . Then you can build the project (./gradlew build & ./gradlew eclipse).
