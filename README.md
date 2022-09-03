![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/io.github.dkim19375.dkim-gradle?label=Gradle%20Plugin%20Portal)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/dkim19375/DkimGradle/Testing%20CI%20with%20Gradle)
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
    mavenCentral() // mavenCentral not included in mavenAll()
    mavenLocal() // mavenLocal not included in mavenAll()
    mavenAll() // adds all the repositories in the Repository enum
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
    maven(Repository.SPIGOT) // required for Spigot API
    maven(Repository.PAPER) // required for Paper API
    
    // or replace two maven(Repository) calls with:
    mavenAll()
}

dependencies {
    compileOnly(spigotAPI(MinecraftVersion.V1_19_2)) // Spigot API for 1.19.2
    
    // Spigot NMS for 1.19.2
    // Does not require any of the repositories above,
    // but you must have run BuildTools for the current version
    compileOnly(spigotNMS(MinecraftVersion.V1_19_2)) // Spigot NMS for 1.19.2
    
    compileOnly(paper(MinecraftVersion.V1_19_2)) // Paper API for 1.19.2
    
    // Example return value: "1.19.2-R0.1-SNAPSHOT"
    val versionStr: String = getVersionString(MinecraftVersion.V1_19_2)
    // Example usage that provides Paper NMS: (requires Paperweight)
    paperDevBundle(getVersionString(MinecraftVersion.V1_19_2))
}
```
### Common Scripts/Tasks
To avoid having to keep having the same tasks in every project,
this plugin can provide some in order to make it easier and quicker to set up new projects.

The list and documentation can be found 
[here](https://github.com/dkim19375/DkimGradle/blob/master/src/main/kotlin/me/dkim19375/dkimgradle/util/CommonScripts.kt).
