/*
 * MIT License
 *
 * Copyright (c) 2022 dkim19375
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:Suppress("unused", "UnusedReceiverParameter")

package me.dkim19375.dkimgradle.util

import me.dkim19375.dkimgradle.enums.PaperVersion
import me.dkim19375.dkimgradle.enums.Repository
import me.dkim19375.dkimgradle.enums.maven
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import java.lang.NumberFormatException

fun DependencyHandler.spigotAPI(versionString: String, project: Project?): String {
    val version = Version(versionString)
    // Repositories
    if (version.major <= 1 && version.minor <= 15) { project?.repositories?.maven(Repository.SONATYPE_SNAPSHOTS_OLD) }
    project?.repositories?.maven(Repository.MAVEN_CENTRAL, Repository.SPIGOT)
    // Dependency
    return "org.spigotmc:spigot-api:${getVersionString(versionString)}"
}

fun DependencyHandler.spigotNMS(versionString: String, project: Project?): String {
    // Repositories
    project?.repositories?.maven(Repository.MAVEN_CENTRAL, Repository.SPIGOT)
    project?.repositories?.mavenLocal()
    // Dependency
    return "org.spigotmc:spigot:${getVersionString(versionString)}"
}

fun DependencyHandler.paper(version: String, project: Project?): String {
    val paperVersion: PaperVersion = PaperVersion.parse(version)
    // Repositories
    project?.repositories?.maven(Repository.MAVEN_CENTRAL, Repository.SONATYPE_SNAPSHOTS_OLD, Repository.PAPER)
    // Dependency
    return "${paperVersion.groupID}:${paperVersion.artifactID}:${getVersionString(version)}"
}

fun DependencyHandler.getVersionString(version: String): String = "${version}-R0.1-SNAPSHOT"

class Version(version: String) {
    val major: Int
    val minor: Int
    val patch: Int

    init {
        val versionSplit = version.split('.')
        require(versionSplit.size >= 2) { "Failed to parse Minecraft version (invalid version string): $version" }

        // Get patch
        try {
            major = versionSplit[0].toInt()
            minor = versionSplit[1].toInt()
            patch = versionSplit.getOrElse(2) { "0" }.toInt()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Failed to parse Minecraft version (invalid values): $version")
        }
    }
}