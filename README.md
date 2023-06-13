![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/io.github.dkim19375.dkim-gradle?label=Gradle%20Plugin%20Portal)
![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/dkim19375/DkimGradle/gradle-test.yml?branch=master)
# DkimGradle
Gradle plugin for Spigot plugins

This plugin gives you some utilities which can help with creating projects or plugins.

### Currently only supports Kotlin DSL

## Installation
### `build.gradle.kts`
```kotlin
plugins {
    id("io.github.dkim19375.dkim-gradle") version "LATEST"
}
```

## Usage
### Maven repositories
Full list of repositories can be found [here](https://github.com/dkim19375/DkimGradle/blob/master/src/main/kotlin/me/dkim19375/dkimgradle/util/Repository.kt).

To add a repository, add the following to your build script:
```kotlin
import me.dkim19375.dkimgradle.util.Repository
import me.dkim19375.dkimgradle.util.maven

repositories {
    maven(Repository.SPIGOT)
}
```
Even better, you can add **all** the repositories at once:
```kotlin
repositories {
    // adds all the repositories in the Repository enum
    // mavenCentral is included in mavenAll()
    mavenAll()
    
    mavenLocal() // mavenLocal is not included in mavenAll()
}
```
### Dependencies
To keep compatibility with bots such as Renovate, this plugin only supports dependencies
which aren't required to be updated.

`spigotAPI(MinecraftVersion): String` returns a dependency string for Spigot API
`spigotNMS(MinecraftVersion): String` returns a dependency string for Spigot NMS
`paper(MinecraftVersion): String` returns a dependency string for Paper API
`getVersionString(MinecraftVersion): String` returns a version string that can be used in dependency strings


### Example `build.gradle.kts`
```kotlin
import me.dkim19375.dkimgradle.enums.MinecraftVersion
import me.dkim19375.dkimgradle.util.Repository
import me.dkim19375.dkimgradle.util.getVersionString
import me.dkim19375.dkimgradle.util.maven
import me.dkim19375.dkimgradle.util.spigotAPI // if you want to use Spigot API
import me.dkim19375.dkimgradle.util.spigotNMS // if you want to use Spigot NMS

plugins {
    id("io.github.dkim19375.dkim-gradle") version "LATEST"
}

repositories {
    mavenLocal() // required for Spigot NMS
    mavenCentral() // required for Spigot API and Paper API dependencies
    // maven(Repository.MAVEN_CENTRAL) also works
    maven(Repository.SPIGOT) // required for Spigot API
    maven(Repository.PAPER) // required for Paper API
    
    // or replace mavenCentral() and the two maven(Repository) calls with:
    mavenAll()
}

dependencies {
    compileOnly(spigotAPI("1.19.4")) // Spigot API for 1.19.4
    
    // Spigot NMS for 1.19.4
    // Does not require any of the repositories above (besides mavenLocal()),
    // but you must have run BuildTools for the current version
    compileOnly(spigotNMS("1.19.4")) // Spigot NMS for 1.19.4
    
    compileOnly(paper("1.19.4")) // Paper API for 1.19.4
    
    // Example return value: "1.19.4-R0.1-SNAPSHOT"
    val versionStr: String = getVersionString("1.19.4")
    // Example usage that provides Paper NMS: (requires Paperweight)
    paperDevBundle(getVersionString("1.19.4"))
}
```
### Common Scripts/Tasks
To avoid having to keep having the same tasks in every project,
this plugin can provide some in order to make it easier and quicker to set up new projects.

The list and documentation can be found 
[here](https://github.com/dkim19375/DkimGradle/blob/master/src/main/kotlin/me/dkim19375/dkimgradle/util/CommonScripts.kt).
