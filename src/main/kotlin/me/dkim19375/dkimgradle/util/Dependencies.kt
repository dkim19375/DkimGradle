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

/**
 * 1. Adds the [Repository.SONATYPE_SNAPSHOTS_OLD] repository if the version is 1.15 or below
 * 2. Adds the [Repository.MAVEN_CENTRAL] and [Repository.SPIGOT] repositories
 * 3. Adds the dependency (org.spigotmc:spigot-api:[getVersionString])
 */
fun DependencyHandler.spigotAPI(versionString: String, project: Project? = null): String {
    val version = MinecraftVersion(versionString)
    // Repositories
    if (version.major <= 1 && version.minor <= 15) { project?.repositories?.maven(Repository.SONATYPE_SNAPSHOTS_OLD) }
    project?.repositories?.maven(Repository.MAVEN_CENTRAL, Repository.SPIGOT)
    // Dependency
    return "org.spigotmc:spigot-api:${getVersionString(versionString)}"
}

/**
 * 1. Adds the [Repository.MAVEN_CENTRAL], maven local, and [Repository.SPIGOT] repositories
 * 2. Adds the dependency (org.spigotmc:spigot:[getVersionString])
 */
fun DependencyHandler.spigotNMS(versionString: String, project: Project? = null): String {
    // Repositories
    project?.repositories?.maven(Repository.MAVEN_CENTRAL, Repository.SPIGOT)
    project?.repositories?.mavenLocal()
    // Dependency
    return "org.spigotmc:spigot:${getVersionString(versionString)}"
}

/**
 * 1. Adds the [Repository.MAVEN_CENTRAL], [Repository.SONATYPE_SNAPSHOTS_OLD], and [Repository.PAPER] repositories
 * 2. Adds the dependency ([PaperVersion.groupID]:[PaperVersion.artifactID]:[version]-R0.1-SNAPSHOT)
 */
fun DependencyHandler.paper(version: String, project: Project? = null): String {
    val paperVersion: PaperVersion = PaperVersion.parse(version)
    // Repositories
    project?.repositories?.maven(Repository.MAVEN_CENTRAL, Repository.SONATYPE_SNAPSHOTS_OLD, Repository.PAPER)
    // Dependency
    return "${paperVersion.groupID}:${paperVersion.artifactID}:${getVersionString(version)}"
}

/**
 * Returns the version string with `-R0.1-SNAPSHOT` appended to it
 */
fun DependencyHandler.getVersionString(version: String): String = "${version}-R0.1-SNAPSHOT"