# Gradle Scripts

The gradle scripts in this directory contain self developed functionality and/or
load and configure external plugins.

For abstraction purposes and clarity, the file names of the scripts are stored
in a map in the script `gradleScripts.gradle`. After loading this script, you
can load the other scripts via the `gradleScript` map.  
E.g. for the repositories
```groovy
apply from: gradleScript.repositories
```
instead of
```groovy
apply from: "${rootProject.projectDir}/gradle/scripts/repositories.gradle"
```


## Dependencies

To manage the version for the plugins or for the dependencies inside the
projects, there is a dependency management script `dependencyManagement.gradle`.
The versions are stored in maps in the file `versions.gradle` of the root
directory and need to be loaded before the dependency management script.

To load the dependency management within a `buildscript` block, there is an
additional script `applyBuildscriptDepMgmt.gradle`. It contains also the loading
of the versions. The reason why an extra script exists to load the management
in the `buildscript` block is that it has to be loaded frequently and the other
only once in the root `build.gradle` file.


## Repositories

The default repositories used in the entire project are loaded in the script
`repositories.gradle`. If needed, you can add additional repositories in
the corresponding project.


## Plugins

Each used gradle plugin is outsourced with its configuration into a separate
script. To load a plugin, you must load the appropriate plugin script and then
call a script specific method `applyXyzConfig`.

The plugin scripts share some basic elements to load, apply and configure the
plugin.

At the top of the script is the most frequently changed part, the configuration.
Mainly it is a map with the name of the configuration as key and the
configuration closure as value. Here you can define as many configurations as
you want to and name it as you like.

```groovy
def configs = [
	'default': {
		// default configuration
	},
	'other': {
		// other configuration
	}
	// ... additional configurations
]
```

After that, you have the `buildscript` block to load the dependency management
to manage the used plugin version, the repository for the dependencies and the
dependencies mainly the plugin.

```groovy
buildscript {
	apply from: rootProject.gradleScript.applyBuildscriptDepMgmt, to: owner

	repositories {
		// ...
	}

	dependencies {
		// ...
	}
}
```

Next, you have a method to apply the plugin. Normally, you would load a gradle
plugin by its id. But due to a bug (see below), this is currently not possible
from scripts. As workaround, you must load the plugin class directly.  
Gradle issues: [#1262](https://github.com/gradle/gradle/issues/1262)
[#1322](https://github.com/gradle/gradle/issues/1322)  
Gradle fix: [#9594](https://github.com/gradle/gradle/pull/9594)

```groovy
ext.applyDemonstrationPlugin = {
	apply plugin: org.package.of.plugin.ClassOfPlugin
}
```

The apply plugin method does not load a configuration. To load and configure a
plugin at the same time, there is an additional method with the configuration
name as a parameter and a boolean value to indicate whether the plugin should be
applied.

```groovy
ext.applyDemonstrationConfig = { configName = 'default', boolean applyPlugin = true ->

	def configuration = configs[configName]
	if (configuration == null) {
		throw new GradleScriptException("No plugin configuration found with name: ${configName}", null)
	}

	if (applyPlugin) {
		applyDemonstrationPlugin()
	}

	configuration()
}
```
