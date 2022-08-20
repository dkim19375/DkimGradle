@file:Suppress("unused", "UnusedReceiverParameter")

package me.dkim19375.dkimgradle.util

import me.dkim19375.dkimgradle.enums.MinecraftVersion
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.spigotAPI(version: MinecraftVersion): String =
    "org.spigotmc:spigot-api:${version.version}-R0.1-SNAPSHOT"

fun DependencyHandler.spigotNMS(version: MinecraftVersion): String =
    "org.spigotmc:spigot:${version.version}-R0.1-SNAPSHOT"

fun DependencyHandler.paper(version: MinecraftVersion): String =
    "${version.type.groupID}:${version.type.artifactID}:${getVersionString(version)}"

fun DependencyHandler.getVersionString(version: MinecraftVersion): String =
    "${version.version}-R0.1-SNAPSHOT"