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

package me.dkim19375.dkimgradle.util

import me.dkim19375.dkimgradle.annotation.API
import me.dkim19375.dkimgradle.delegate.TaskRegisterDelegate
import org.gradle.api.DefaultTask
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import java.io.File

/**
 * Checks if the project is using Kotlin
 */
@API
fun Project.isKotlin(): Boolean = setOf(
    "org.jetbrains.kotlin.js",
    "org.jetbrains.kotlin.jvm",
    "org.jetbrains.kotlin.native",
    "org.jetbrains.kotlin.android",
    "org.jetbrains.kotlin.multiplatform",
).any {
    plugins.hasPlugin(it)
}

/**
 * Checks if the Shadow plugin is applied
 */
fun Project.hasShadowPlugin(): Boolean = plugins.hasPlugin("com.github.johnrengelman.shadow")

/**
 * Sets the text encoding for the project
 *
 * @param encoding The encoding to set
 */
fun Project.setJavaTextEncoding(encoding: String = "UTF-8") {
    tasks.withType<JavaCompile> { options.encoding = encoding }
}

/**
 * Sets the Java (and possibly Kotlin in the future) version for the project
 *
 * @param javaVersion The java version to set (example: `1.8`)
 */
fun Project.setJavaVersion(javaVersion: JavaVersion = JavaVersion.VERSION_1_8) {
    tasks.named<JavaPluginExtension>("java") { // tasks.withType doesn't work
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}

/**
 * Adds the task that makes `gradle build` run `gradle shadowJar`
 */
fun Project.addBuildShadowTask() {
    check(hasShadowPlugin()) { "Shadow plugin is not applied!" }
    tasks.named<DefaultTask>("build") { dependsOn("shadowJar") }
}

/**
 * Adds the task that generates the Javadoc and sources jar files
 *
 * @param javadocClassifier The classifier for the Javadoc jar file
 * @param sourcesClassifier The classifier for the sources jar file
 */
@API
fun Project.javaJavadocSourcesJars(javadocClassifier: String? = null, sourcesClassifier: String? = null) {
    tasks.named<JavaPluginExtension>("java") {  // tasks.withType doesn't work
        withJavadocJar()
        withSourcesJar()
    }
    if (javadocClassifier != null) { tasks.named<Jar>("javadocJar") { archiveClassifier.set(javadocClassifier) } }
    if (sourcesClassifier != null) { tasks.named<Jar>("sourcesJar") { archiveClassifier.set(sourcesClassifier) } }
}

/**
 * Configures the ProcessResources [Task] to add replacements
 *
 * @param replacements A [Map] of all the replacements
 */
fun Project.addReplacementsTask(
    replacements: Map<String, () -> String> = mapOf(
        "version" to version::toString
    )) {
    tasks.named<Copy>("processResources") {
        outputs.upToDateWhen { false }
        expand(replacements.mapValues { it.value() })
    }
}

/**
 * Adds the specified compiler arguments to the project
 */
@API
fun Project.addCompilerArgs(vararg args: String) {
    tasks.withType<JavaCompile> { options.compilerArgs.addAll(args) }
}

/**
 * Deletes ALL files in the specified [directory]
 *
 * @param directory The directory to delete the files from (default is "build/libs")
 * @return The [Task] that deletes the files
 */
fun Project.removeBuildJarsTask(directory: String = "build/libs"): TaskRegisterDelegate = TaskRegisterDelegate(this) {
    project.tasks.findByName("classes")?.also { classes ->
        classes.dependsOn(this@TaskRegisterDelegate)
    } ?: throw IllegalStateException("classes task is not found")
    doLast {
        File(project.rootDir, directory).deleteRecursively()
    }
}

/**
 * Copies the [built file][jar] to the [directory][copyToDirectory]
 *
 * @param copyToDirectory The directory to copy the built file to
 * @param dependsOnTask The task that should run after this task, or null if non should be run
 * @param jar The [File] to copy
 * @return the [Task] that copies the file
 */
fun Project.copyFileTask(
    copyToDirectory: String,
    dependsOnTask: Task? = run {
        val taskNames = listOf("deleteAll", "reobfJar", "shadowJar", "build")
        taskNames.firstNotNullOfOrNull(tasks::findByName)
            ?: throw IllegalStateException("No default dependsOn task found!")
    },
    jar: () -> File,
): TaskRegisterDelegate = TaskRegisterDelegate(this) {
    dependsOnTask?.finalizedBy(this)
    doLast {
        val jarFile = jar()
        val folder = file(rootDir).resolve(copyToDirectory).takeIf(File::exists)
            ?: File(copyToDirectory)
        if (!folder.exists()) {
            logger.warn("Folder $folder does not exist! Build won't be copied")
            return@doLast
        }
        jarFile.copyTo(File(folder, jarFile.name), true)
    }
}

/**
 * Deletes matching jar files in [deleteFilesInDirectories]
 *
 * @param deleteFilesInDirectories Directories to search
 * @param fileName The base file name to search for (exact if [exactName] is true, otherwise startsWith)
 * @param dependsOnTask The task that should run after this task, or null if non should be run
 * @param exactName Whether the file name should be exact or should check if the file name
 * starts with [fileName]
 * @param ignoreCase Whether the file name check should ignore the case
 * @return The [Task] that deletes the files
 */
fun Project.deleteAllTask(
    deleteFilesInDirectories: Collection<String>,
    fileName: String,
    dependsOnTask: Task? = run {
        val taskNames = listOf("reobfJar", "shadowJar", "build")
        taskNames.firstNotNullOfOrNull(tasks::findByName)
            ?: throw IllegalStateException("No default dependsOn task found!")
    },
    exactName: Boolean = false,
    ignoreCase: Boolean = true,
): TaskRegisterDelegate = TaskRegisterDelegate(this) {
    dependsOnTask?.finalizedBy(this)
    doLast {
        for (folderName in deleteFilesInDirectories) {
            val folder = file(rootDir).resolve(folderName).takeIf(File::exists)
                ?: File(folderName)
            if (!folder.exists()) {
                logger.warn("Folder $folder does not exist! Folder won't have any files deleted")
                return@doLast
            }
            if (!folder.isDirectory) {
                logger.warn("Folder $folder is not a directory! Folder won't have any files deleted")
                return@doLast
            }
            for (file in folder.listFiles() ?: emptyArray()) {
                if (!file.isFile) {
                    continue
                }
                val delete = if (exactName) {
                    file.name.equals(fileName, ignoreCase)
                } else {
                    file.name.startsWith(fileName, ignoreCase)
                }
                if (delete) {
                    file.delete()
                }
            }
        }
    }
}

/**
 * A function that calls the other functions for you
 *
 * Example:
 *
 *     setupTasksForMC(
 *         serverFoldersRoot = "../.TestServers",
 *         serverFolderNames = listOf("1.8", "1.19"),
 *         mainServerName = "1.19",
 *         jarFileName = shadowJar.get().archiveBaseName.get(), // for Shadow
 *         jar = { reobfJar.get().outputJar.get().asFile } // for PaperWeight
 *     )
 *
 * @param serverFoldersRoot The folder which holds the other servers (for [deleteAllTask])
 * @param serverFolderNames The names of the other servers (for [deleteAllTask])
 * @param mainServerName The server folder name that you want to copy the jar to (for [copyFileTask])
 * @param jarFileName The base name of the jar file (for [deleteAllTask] and [copyFileTask])
 * @param dependsOnTask The task that you want the [deleteAll task][deleteAllTask] to depend on
 * @param jar A function that returns the built jar file
 */
@API
fun Project.setupTasksForMC(
    serverFoldersRoot: String,
    serverFolderNames: Collection<String>,
    mainServerName: String,
    jarFileName: String,
    dependsOnTask: Task? = run {
        val taskNames = listOf("reobfJar", "shadowJar", "build")
        taskNames.firstNotNullOfOrNull(tasks::findByName)
            ?: throw IllegalStateException("No default dependsOn task found!")
    },
    replacements: Map<String, () -> String> = mapOf(
        "name" to name::toString,
        "version" to this.version::toString
    ),
    group: String = this.group.toString(),
    version: String = this.version.toString(),
    javaVersion: JavaVersion = JavaVersion.VERSION_1_8,
    textEncoding: String = "UTF-8",
    jar: () -> File,
) {
    setupMC(
        group = group,
        version = version,
        javaVersion = javaVersion,
        textEncoding = textEncoding,
        replacements = replacements,
    )
    removeBuildJarsTask()
    val serverRoot = serverFoldersRoot.removeSuffix("/").removeSuffix("\\")
    val deleteAll by deleteAllTask(
        deleteFilesInDirectories = serverFolderNames.map { folderName ->
            "$serverRoot/$folderName/plugins"
        },
        fileName = jarFileName,
        dependsOnTask = dependsOnTask,
    )
    copyFileTask(
        copyToDirectory = "$serverRoot/$mainServerName/plugins",
        dependsOnTask = deleteAll,
        jar = jar
    )
}

/**
 * Sets up the project with the specified [group] and [version] for a simple Minecraft project
 *
 * Adds the text encoding, replacements, and build shadow task (if the Shadow plugin is applied)
 *
 * @param group The group of the project (example: `me.dkim19375`)
 * @param version The version of the project (example: `1.0.0`)
 * @param javaVersion The java version of the project (example: [JavaVersion.VERSION_1_8])
 * @param replacements The replacements for the [replacements task][addReplacementsTask]
 * @param textEncoding The text encoding for the [text encoding task][setJavaTextEncoding]
 */
@API
fun Project.setupMC(
    group: String,
    version: String = "1.0.0",
    javaVersion: JavaVersion? = null,
    replacements: Map<String, () -> String> = mapOf(
        "name" to name::toString,
        "version" to version::toString
    ),
    textEncoding: String = "UTF-8",
) {
    this.group = group
    this.version = version
    javaVersion?.let(::setJavaVersion)
    addReplacementsTask(replacements)
    setJavaTextEncoding(textEncoding)
    if (hasShadowPlugin()) addBuildShadowTask()
}
