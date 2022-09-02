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
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.named
import java.io.File

fun Project.addReplacementsTask(
    replacements: Map<String, () -> String> = mapOf(
        "pluginVersion" to version::toString
    ),
) {
    tasks.named<Copy>("processResources") {
        outputs.upToDateWhen { false }
        expand(replacements.mapValues(Map.Entry<*, () -> String>::value))
    }
}

fun Project.removeBuildJarsTask(directory: String = "build/libs"): TaskRegisterDelegate = TaskRegisterDelegate(this) {
    project.tasks.findByName("classes")?.also { classes ->
        classes.dependsOn(this@TaskRegisterDelegate)
    } ?: throw IllegalStateException("classes task is not found")
    doLast {
        File(project.rootDir, directory).deleteRecursively()
    }
}

fun Project.copyFileTask(
    copyToDirectory: String,
    dependsOnTask: Task? = run {
        val taskNames = listOf("deleteAll", "reobfJar", "shadowJar", "build")
        taskNames.mapNotNull(tasks::findByName).firstOrNull()
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

fun Project.deleteAllTask(
    deleteFilesInDirectories: Collection<String>,
    fileName: String,
    dependsOnTask: Task? = run {
        val taskNames = listOf("reobfJar", "shadowJar", "build")
        taskNames.mapNotNull(tasks::findByName).firstOrNull()
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

@Suppress("UNUSED_VARIABLE")
@API
fun Project.setupTasksForMC(
    serverFoldersRoot: String,
    serverFolderNames: Collection<String>,
    mainServerName: String,
    jarFileName: String,
    dependsOnTask: Task? = run {
        val taskNames = listOf("reobfJar", "shadowJar", "build")
        taskNames.mapNotNull(tasks::findByName).firstOrNull()
            ?: throw IllegalStateException("No default dependsOn task found!")
    },
    jar: () -> File,
) {
    addReplacementsTask()
    val removeBuildJars by removeBuildJarsTask()
    val serverRoot = serverFoldersRoot.removeSuffix("/").removeSuffix("\\")
    val deleteAll by deleteAllTask(
        deleteFilesInDirectories = serverFolderNames.map { folderName ->
            "$serverRoot/$folderName/plugins"
        },
        fileName = jarFileName,
        dependsOnTask = dependsOnTask,
    )
    val copyFile by copyFileTask(
        copyToDirectory = "$serverRoot/$mainServerName/plugins",
        dependsOnTask = deleteAll,
        jar = jar
    )
}