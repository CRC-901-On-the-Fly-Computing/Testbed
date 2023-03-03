# Gradle Tasks Reference

This is a list of all the Gradle tasks used in the project.

Application tasks
-----------------
* bootRunRuns
  * this project as a Spring Boot application.
* run
  * Runs this project as a JVM application

Build tasks
-----------
* assemble
  * Assembles the outputs of this project.
* bootJar
  * Assembles an executable jar archive containing the main classes and their dependencies.
* build
  * Assembles and tests this project.
* buildDependents
  * Assembles and tests this project and all projects that depend on it.
* buildNeeded
  * Assembles and tests this project and all projects it depends on.
* classes
  * Assembles main classes.
* clean
  * Deletes the build directory.
* generateSwaggerCode
  * Generates a source code from the OpenAPI specification.
* generateSwaggerCodeProject
  * Generates a source code from project.
* jar
  * Assembles a jar archive containing the main classes.
* javadocJar
* sourceJar
* testClasses
  * Assembles test classes.

Build Setup tasks
-----------------
* init
  * Initializes a new Gradle build.
* resolveSwaggerTemplate
  * Resolves template files from the swaggerTemplate configuration.
* wrapper
  * Generates Gradle wrapper files.

Checking tasks
--------------
* checkLicense
  * Check if License could be used

CheckingPreparation tasks
-------------------------
* checkLicensePreparation
  * Prepare for checkLicense

CRC901 tasks
------------
* downloadSpecification
  * Downloads PostgREST API.
* jenkins
  * Generates/updates Jenkins files and jobs.
* updateConfigs
  * Updates project configuration yaml files.

Distribution tasks
------------------
* assembleDist
  * Assembles the main distributions
* distTar
  * Bundles the project as a distribution.
* distZip
  * Bundles the project as a distribution.
* installDist
  * Installs the project as a distribution as-is.

Docker tasks
------------
* docker
  * Builds Docker image.
* dockerClean
  * Cleans Docker build directory.
* dockerfileZip
  * Bundles the configured Dockerfile in a zip file
* dockerPrepare
  * Prepares Docker build directory.
* dockerPush
  * Pushes named Docker image to configured Docker Hub.
* dockerPushDate
  * Pushes the Docker image with tag 'nexus.cs.upb.de/sfb901-testbed/admin_client:2020-05-01T08_37_54' to configured Docker Hub
* dockerPushLatest
  * Pushes the Docker image with tag 'nexus.cs.upb.de/sfb901-testbed/admin_client:latest' to configured Docker Hub
* dockerPushVersion
  * Pushes the Docker image with tag 'nexus.cs.upb.de/sfb901-testbed/admin_client:unversioned' to configured Docker Hub
* dockerTag
  * Applies all tags to the Docker image.
* dockerTagDate
  * Tags Docker image with tag 'nexus.cs.upb.de/sfb901-testbed/admin_client:2020-05-01T08_37_54'
* dockerTagLatest
  * Tags Docker image with tag 'nexus.cs.upb.de/sfb901-testbed/admin_client:latest'
* dockerTagsPush
  * Pushes all tagged Docker images to configured Docker Hub.
* dockerTagVersion
  * Tags Docker image with tag 'nexus.cs.upb.de/sfb901-testbed/admin_client:unversioned'

Documentation tasks
-------------------
* generateReDoc
  * Generates ReDoc from the OpenAPI specification.
* generateReDocProject
  * Generates ReDoc from project.
* generateSwaggerUI
  * Generates Swagger UI from the OpenAPI specification.
* generateSwaggerUIProject
  * Generates Swagger UI from project.
* javadoc
  * Generates Javadoc API documentation for the main source code.

Help tasks
----------
* buildEnvironment
  * Displays all buildscript dependencies declared in root project 'Testbed'.
* components
  * Displays the components produced by root project 'Testbed'. [incubating]
* dependencies
  * Displays all dependencies declared in root project 'Testbed'.
* dependencyInsight
  * Displays the insight into a specific dependency in root project 'Testbed'.
* dependentComponents
  * Displays the dependent components of components in root project 'Testbed'. [incubating]
* generateSwaggerCodeHelp
  * Displays available JSON configuration for task ':postgrest:generateSwaggerCode'
* generateSwaggerCodeProjectHelp
  * Displays available JSON configuration for task ':postgrest:generateSwaggerCodeProject'
* help
  * Displays a help message.
* model
  * Displays the configuration model of root project 'Testbed'. [incubating]
* projects
  * Displays the sub-projects of root project 'Testbed'.
* properties
  * Displays the properties of root project 'Testbed'.
* tasks
  * Displays the tasks runnable from root project 'Testbed' (some of the displayed tasks may belong to subprojects).

IDE tasks
---------
* cleanEclipse
  * Cleans all Eclipse files.
* cleanIdea
  * Cleans IDEA project files (IML, IPR)
* eclipse
  * Generates all Eclipse files.
* idea
  * Generates IDEA project files (IML, IPR, IWS)

Lint tasks
----------
* autoLintGradle
* criticalLintGradle
* fixGradleLint
* fixLintGradle
* generateGradleLintReport
* lintGradle

Locking tasks
-------------
* generateGlobalLock
  * Create a lock file in build/<configured name>
* generateLock
  * Create a lock file in build/<configured name>
* saveGlobalLock
  * Move the generated lock file into the project directory
* saveLock
  * Move the generated lock file into the project directory
* updateGlobalLock
  * Apply updates to a preexisting lock file and write to build/<specified name>
* updateLock
  * Apply updates to a preexisting lock file and write to build/<specified name>

Mustache tasks
--------------
* compileTemplate

Nebula Release tasks
--------------------
* candidate
* candidateSetup
* devSnapshot
* devSnapshotSetup
* final
* finalSetup
* postRelease
* releaseCheck
* snapshot
* snapshotSetup

Publishing tasks
----------------
* generateMetadataFileForNebulaPublication
  * Generates the Gradle metadata file for publication 'nebula'.
* generateMetadataFileForShadowPublication
  * Generates the Gradle metadata file for publication 'shadow'.
* generatePomFileForNebulaPublication
  * Generates the Maven POM file for publication 'nebula'.
* generatePomFileForShadowPublication
  * Generates the Maven POM file for publication 'shadow'.
* publish
  * Publishes all publications produced by this project.
* publishAllPublicationsToMaven10Repository
  * Publishes all Maven publications produced by this project to the maven10 repository.
* publishAllPublicationsToMaven11Repository
  * Publishes all Maven publications produced by this project to the maven11 repository.
* publishAllPublicationsToMaven12Repository
  * Publishes all Maven publications produced by this project to the maven12 repository.
* publishAllPublicationsToMaven13Repository
  * Publishes all Maven publications produced by this project to the maven13 repository.
* publishAllPublicationsToMaven14Repository
  * Publishes all Maven publications produced by this project to the maven14 repository.
* publishAllPublicationsToMaven15Repository
  * Publishes all Maven publications produced by this project to the maven15 repository.
* publishAllPublicationsToMaven16Repository
  * Publishes all Maven publications produced by this project to the maven16 repository.
* publishAllPublicationsToMaven17Repository
  * Publishes all Maven publications produced by this project to the maven17 repository.
* publishAllPublicationsToMaven18Repository
  * Publishes all Maven publications produced by this project to the maven18 repository.
* publishAllPublicationsToMaven19Repository
  * Publishes all Maven publications produced by this project to the maven19 repository.
* publishAllPublicationsToMaven20Repository
  * Publishes all Maven publications produced by this project to the maven20 repository.
* publishAllPublicationsToMaven21Repository
  * Publishes all Maven publications produced by this project to the maven21 repository.
* publishAllPublicationsToMaven22Repository
  * Publishes all Maven publications produced by this project to the maven22 repository.
* publishAllPublicationsToMaven23Repository
  * Publishes all Maven publications produced by this project to the maven23 repository.
* publishAllPublicationsToMaven24Repository
  * Publishes all Maven publications produced by this project to the maven24 repository.
* publishAllPublicationsToMaven25Repository
  * Publishes all Maven publications produced by this project to the maven25 repository.
* publishAllPublicationsToMaven26Repository
  * Publishes all Maven publications produced by this project to the maven26 repository.
* publishAllPublicationsToMaven27Repository
  * Publishes all Maven publications produced by this project to the maven27 repository.
* publishAllPublicationsToMaven28Repository
  * Publishes all Maven publications produced by this project to the maven28 repository.
* publishAllPublicationsToMaven29Repository
  * Publishes all Maven publications produced by this project to the maven29 repository.
* publishAllPublicationsToMaven2Repository
  * Publishes all Maven publications produced by this project to the maven2 repository.
* publishAllPublicationsToMaven30Repository
  * Publishes all Maven publications produced by this project to the maven30 repository.
* publishAllPublicationsToMaven31Repository
  * Publishes all Maven publications produced by this project to the maven31 repository.
* publishAllPublicationsToMaven32Repository
  * Publishes all Maven publications produced by this project to the maven32 repository.
* publishAllPublicationsToMaven33Repository
  * Publishes all Maven publications produced by this project to the maven33 repository.
* publishAllPublicationsToMaven34Repository
  * Publishes all Maven publications produced by this project to the maven34 repository.
* publishAllPublicationsToMaven35Repository
  * Publishes all Maven publications produced by this project to the maven35 repository.
* publishAllPublicationsToMaven36Repository
  * Publishes all Maven publications produced by this project to the maven36 repository.
* publishAllPublicationsToMaven37Repository
  * Publishes all Maven publications produced by this project to the maven37 repository.
* publishAllPublicationsToMaven38Repository
  * Publishes all Maven publications produced by this project to the maven38 repository.
* publishAllPublicationsToMaven39Repository
  * Publishes all Maven publications produced by this project to the maven39 repository.
* publishAllPublicationsToMaven3Repository
  * Publishes all Maven publications produced by this project to the maven3 repository.
* publishAllPublicationsToMaven40Repository
  * Publishes all Maven publications produced by this project to the maven40 repository.
* publishAllPublicationsToMaven41Repository
  * Publishes all Maven publications produced by this project to the maven41 repository.
* publishAllPublicationsToMaven42Repository
  * Publishes all Maven publications produced by this project to the maven42 repository.
* publishAllPublicationsToMaven43Repository
  * Publishes all Maven publications produced by this project to the maven43 repository.
* publishAllPublicationsToMaven44Repository
  * Publishes all Maven publications produced by this project to the maven44 repository.
* publishAllPublicationsToMaven45Repository
  * Publishes all Maven publications produced by this project to the maven45 repository.
* publishAllPublicationsToMaven4Repository
  * Publishes all Maven publications produced by this project to the maven4 repository.
* publishAllPublicationsToMaven5Repository
  * Publishes all Maven publications produced by this project to the maven5 repository.
* publishAllPublicationsToMaven6Repository
  * Publishes all Maven publications produced by this project to the maven6 repository.
* publishAllPublicationsToMaven7Repository
  * Publishes all Maven publications produced by this project to the maven7 repository.
* publishAllPublicationsToMaven8Repository
  * Publishes all Maven publications produced by this project to the maven8 repository.
* publishAllPublicationsToMaven9Repository
  * Publishes all Maven publications produced by this project to the maven9 repository.
* publishAllPublicationsToMavenRepository
  * Publishes all Maven publications produced by this project to the maven repository.
* publishNebulaPublicationToMaven10Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven10'.
* publishNebulaPublicationToMaven11Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven11'.
* publishNebulaPublicationToMaven12Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven12'.
* publishNebulaPublicationToMaven13Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven13'.
* publishNebulaPublicationToMaven14Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven14'.
* publishNebulaPublicationToMaven15Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven15'.
* publishNebulaPublicationToMaven16Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven16'.
* publishNebulaPublicationToMaven17Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven17'.
* publishNebulaPublicationToMaven18Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven18'.
* publishNebulaPublicationToMaven19Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven19'.
* publishNebulaPublicationToMaven20Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven20'.
* publishNebulaPublicationToMaven21Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven21'.
* publishNebulaPublicationToMaven22Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven22'.
* publishNebulaPublicationToMaven23Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven23'.
* publishNebulaPublicationToMaven24Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven24'.
* publishNebulaPublicationToMaven25Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven25'.
* publishNebulaPublicationToMaven26Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven26'.
* publishNebulaPublicationToMaven27Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven27'.
* publishNebulaPublicationToMaven28Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven28'.
* publishNebulaPublicationToMaven29Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven29'.
* publishNebulaPublicationToMaven2Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven2'.
* publishNebulaPublicationToMaven30Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven30'.
* publishNebulaPublicationToMaven31Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven31'.
* publishNebulaPublicationToMaven32Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven32'.
* publishNebulaPublicationToMaven33Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven33'.
* publishNebulaPublicationToMaven34Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven34'.
* publishNebulaPublicationToMaven35Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven35'.
* publishNebulaPublicationToMaven36Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven36'.
* publishNebulaPublicationToMaven37Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven37'.
* publishNebulaPublicationToMaven38Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven38'.
* publishNebulaPublicationToMaven39Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven39'.
* publishNebulaPublicationToMaven3Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven3'.
* publishNebulaPublicationToMaven40Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven40'.
* publishNebulaPublicationToMaven41Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven41'.
* publishNebulaPublicationToMaven42Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven42'.
* publishNebulaPublicationToMaven43Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven43'.
* publishNebulaPublicationToMaven44Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven44'.
* publishNebulaPublicationToMaven45Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven45'.
* publishNebulaPublicationToMaven4Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven4'.
* publishNebulaPublicationToMaven5Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven5'.
* publishNebulaPublicationToMaven6Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven6'.
* publishNebulaPublicationToMaven7Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven7'.
* publishNebulaPublicationToMaven8Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven8'.
* publishNebulaPublicationToMaven9Repository
  * Publishes Maven publication 'nebula' to Maven repository 'maven9'.
* publishNebulaPublicationToMavenLocal
  * Publishes Maven publication 'nebula' to the local Maven repository.
* publishNebulaPublicationToMavenRepository
  * Publishes Maven publication 'nebula' to Maven repository 'maven'.
* publishShadowPublicationToMavenLocal
  * Publishes Maven publication 'shadow' to the local Maven repository.
* publishShadowPublicationToMavenRepository
  * Publishes Maven publication 'shadow' to Maven repository 'maven'.
* publishToMavenLocal
  * Publishes all Maven publications produced by this project to the local Maven cache.

Reporting tasks
---------------
* generateLicenseReport
  * Generates license report for all dependencies of this project and its subprojects

Shadow tasks
------------
* knows
  * Do you know who knows?
* shadowJar
  * Create a combined JAR of project and runtime dependencies

Verification tasks
------------------
* check
  * Runs all checks.
* jacocoTestCoverageVerification
  * Verifies code coverage metrics based on specified rules for the test task.
* jacocoTestReport
  * Generates code coverage report for the test task.
* test
  * Runs the unit tests.
* validateSwagger
  * Validates YAML of the OpenAPI specification.
* validateSwaggerProject
  * Validates YAML of project.

To see all tasks and more detail, run gradlew tasks --all
