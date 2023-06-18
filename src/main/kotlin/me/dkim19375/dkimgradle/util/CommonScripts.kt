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

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.github.gradlenexus.publishplugin.NexusPublishExtension
import me.dkim19375.dkimgradle.annotation.API
import me.dkim19375.dkimgradle.data.DocSourcesJarTasksHolder
import me.dkim19375.dkimgradle.data.pom.DeveloperData
import me.dkim19375.dkimgradle.data.pom.LicenseData
import me.dkim19375.dkimgradle.data.pom.SCMData
import me.dkim19375.dkimgradle.delegate.TaskRegisterDelegate
import me.dkim19375.dkimgradle.enums.DokkaOutputFormat
import org.gradle.api.DefaultTask
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.component.SoftwareComponent
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.Publication
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.internal.publication.MavenPomInternal
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File
import java.net.URI

/**
 * Checks if the project is using Kotlin
 */
@API
fun Project.isKotlin(): Boolean = setOf(
    "kotlin-gradle-plugin",
    "org.jetbrains.kotlin.js",
    "org.jetbrains.kotlin.jvm",
    "org.jetbrains.kotlin.native",
    "org.jetbrains.kotlin.android",
    "org.jetbrains.kotlin.multiplatform",
).any {
    plugins.hasPlugin(it)
} || plugins.any { it is KotlinBasePlugin }

/**
 * Checks if the Shadow plugin is applied
 */
fun Project.hasShadowPlugin(): Boolean = plugins.hasPlugin("com.github.johnrengelman.shadow")

/**
 * Gets the Java plugin extension
 */
fun Project.getJavaExtension(): JavaPluginExtension {
    check(plugins.hasPlugin("java")) { "Java plugin is not applied!" }
    return extensions["java"] as JavaPluginExtension
}

/**
 * Sets the text encoding for the project
 *
 * @param encoding The encoding to set
 */
fun Project.setJavaTextEncoding(encoding: String = "UTF-8") {
    tasks.withType<JavaCompile> { options.encoding = encoding }
}

/**
 * Sets the Java version for the project
 *
 * @param javaVersion The java version to set (example: [JavaVersion.VERSION_1_8])
 */
fun Project.setJavaVersion(javaVersion: JavaVersion = JavaVersion.VERSION_1_8) {
    val versionNum = javaVersion.name.removePrefix("VERSION_").removePrefix("1_").toInt()
    val java: JavaPluginExtension = getJavaExtension()
    java.sourceCompatibility = javaVersion
    java.targetCompatibility = javaVersion
    if (!isKotlin()) {
        return
    }
    val kotlin = extensions["kotlin"] as KotlinProjectExtension
    kotlin.jvmToolchain(versionNum)
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = versionNum.toString()
    }
}

/**
 * Sets the artifact/archive classifier for the JAR and Shadow JAR tasks
 *
 * @param classifier The classifier to set
 */
fun Project.setShadowArchiveClassifier(classifier: String = "") {
    check(hasShadowPlugin()) { "Shadow plugin is not applied!" }
    tasks.named<ShadowJar>("shadowJar") { archiveClassifier.set(classifier) }
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
fun Project.addJavaJavadocSourcesJars(
    javadocClassifier: String? = null,
    sourcesClassifier: String? = null,
): DocSourcesJarTasksHolder {
    val java: JavaPluginExtension = getJavaExtension()
    java.withJavadocJar()
    java.withSourcesJar()
    if (javadocClassifier != null) {
        tasks.named<Jar>("javadocJar") { archiveClassifier.set(javadocClassifier) }
    }
    if (sourcesClassifier != null) {
        tasks.named<Jar>("sourcesJar") { archiveClassifier.set(sourcesClassifier) }
    }
    return DocSourcesJarTasksHolder(
        javadocJarTask = tasks.named("javadocJar").get() as Jar,
        sourcesJarTask = tasks.named("sourcesJar").get() as Jar
    )
}

@API
fun Project.addKotlinKDocSourcesJars(
    javadocClassifier: String = "javadoc",
    sourcesClassifier: String = "sources",
    dokkaType: DokkaOutputFormat = tasks.findByName(DokkaOutputFormat.HTML_MULTI_MODULE.taskName)?.let {
        DokkaOutputFormat.HTML_MULTI_MODULE
    } ?: DokkaOutputFormat.HTML,
): DocSourcesJarTasksHolder {
    check(plugins.hasPlugin("org.jetbrains.dokka")) { "Dokka plugin is not applied!" }

    val sourceSets = extensions["sourceSets"] as SourceSetContainer
    val mainSourceSet = sourceSets.named<SourceSet>("main")

    val dokkaTask = tasks.findByName(dokkaType.taskName) as? DokkaTask
    if (dokkaTask == null) {
        if (dokkaType.isMultiModule) {
            throw IllegalArgumentException(
                "Dokka task '${dokkaType.taskName}' not found! " +
                        "This dokka output format is for multi-module projects only."
            )
        }
        throw IllegalArgumentException("Dokka task '${dokkaType.taskName}' not found!")
    }

    val dokkaJar = tasks.create("${dokkaType.taskName}Jar", Jar::class) {
        dependsOn(dokkaTask)
        from(dokkaTask)
        archiveClassifier.set(javadocClassifier)
    }

    val sourcesJar = tasks.create("sourcesJar", Jar::class) {
        from(mainSourceSet.get().allSource.srcDirs)
        archiveClassifier.set(sourcesClassifier)
    }

    return DocSourcesJarTasksHolder(
        javadocJarTask = dokkaJar,
        sourcesJarTask = sourcesJar,
    )
}

/**
 * Configures the ProcessResources [Task] to add replacements
 *
 * @param replacements A [Map] of all the replacements
 */
fun Project.addReplacementsTask(
    replacements: Map<String, () -> String> = mapOf(
        "version" to version::toString
    ),
) {
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
 * Relocates the specified package to the specified package
 *
 * @param from The package to relocate
 * @param to The package to relocate to
 */
@API
fun Project.relocate(from: String, to: String) {
    check(hasShadowPlugin()) { "Shadow plugin is not applied!" }
    tasks.named<ShadowJar>("shadowJar") { relocate(from, to) }
}

/**
 * Sets up a simple publishing configuration
 *
 * 1. Applies the `maven-publish` plugin
 * 2. Creates a [MavenPublication] with the specified parameters
 * 3. Configures the [MavenPublication] with the specified [configuration]
 *
 * @param groupId The group ID
 * @param artifactId The artifact ID
 * @param version The version
 * @param component The [SoftwareComponent] to publish
 * @param artifacts The artifacts to publish
 * @param configuration The configuration of the [MavenPublication]
 *
 * @return The [MavenPublication] that was created
 */
inline fun Project.setupPublishing(
    groupId: String? = null,
    artifactId: String? = null,
    version: String? = null,
    component: SoftwareComponent? = if (isKotlin()) components["kotlin"] else components["java"],
    artifacts: Collection<Any> = emptyList(),
    crossinline configuration: MavenPublication.() -> Unit
): MavenPublication {
    apply(plugin = "maven-publish")
    return (extensions["publishing"] as PublishingExtension).publications.create<MavenPublication>("maven") {
        groupId?.let { this.groupId = it }
        artifactId?.let { this.artifactId = it }
        version?.let { this.version = it }
        component?.let { this.from(component) }
        artifacts.forEach(this::artifact)
        configuration()
    }
}

/**
 * Sets up a more advanced publishing configuration geared towards Maven Central
 *
 * 1. Calls [setupPublishing] with the specified parameters
 * 2. Configures the [MavenPublication] with the specified parameters
 * 3. Calls [setupSigning(...)][me.dkim19375.dkimgradle.util.setupSigning] with the specified parameters if [setupSigning] is true
 * 4. Calls [setupNexusPublishing(...)][me.dkim19375.dkimgradle.util.setupNexusPublishing] with the specified parameters if [setupNexusPublishing] is true
 *
 * @param groupId The group ID to publish to (ex: `io.github.username`) - **Required for Maven Central**
 * @param artifactId The artifact ID to publish to (ex: `cool-library`) - **Required for Maven Central**
 * @param artifacts The artifacts to include (see [MavenPublication.artifact] for more information) *May need to add sources and javadocs jar which are required for Maven Central*
 * @param snapshot If this artifact is a snapshot version
 * @param name The name of the project - **Required for Maven Central**
 * @param description The description of the project - **Required for Maven Central**
 * @param url The URL of the project - **Required for Maven Central**
 * @param licenses The license(s) of the project - **Required for Maven Central**
 * @param developers The developer(s) of the project - **Required for Maven Central**
 * @param scm The SCM (Source Code Management) data of the project - **Required for Maven Central**
 * @param publicationName The name of the publishing publication to create
 * @param component The software component that should be published - **Required for Maven Central**
 * @param verifyMavenCentral If these parameters should be checked to see if **most** of the requirements for Maven Central are met
 * @param setupSigning If the signing plugin should be configured - **Required for Maven Central**
 * @param setupNexusPublishing If the Nexus publishing plugin should be configured - **Required for Maven Central**
 * @param configuration Extra configuration to apply to the [MavenPublication]
 *
 * @return The [MavenPublication] that was created
 */
@API
inline fun Project.setupPublishingCentral(
    groupId: String? = project.group.toString(),
    artifactId: String? = project.name,
    version: String? = project.version.toString(),
    artifacts: Collection<Any> = emptyList(),
    snapshot: Boolean = false,
    name: String? = project.name,
    description: String? = null,
    url: String? = null,
    licenses: List<LicenseData> = emptyList(),
    developers: List<DeveloperData> = emptyList(),
    scm: SCMData? = null,
    component: SoftwareComponent? = if (isKotlin()) components["kotlin"] else components["java"],
    packaging: String? = "jar",
    verifyMavenCentral: Boolean = false,
    setupSigning: Boolean = plugins.hasPlugin("signing"),
    setupNexusPublishing: Boolean = plugins.hasPlugin("io.github.gradle-nexus.publish-plugin"),
    crossinline configuration: MavenPublication.() -> Unit = {},
): MavenPublication {
    val versionString = version?.let { if (snapshot) "$it-SNAPSHOT" else it }
    setupPublishing(groupId, artifactId, versionString, component, artifacts, configuration).apply {
        val requireForCentral: (
            condition: Boolean,
            notSetParameter: String,
        ) -> Unit = { condition, notSetParameter ->
            if (verifyMavenCentral) require(condition) { "Maven central verification failed! Parameter '$notSetParameter' must be set!" }
        }

        requireForCentral(this.groupId != null, "groupId")
        requireForCentral(this.artifactId != null, "artifactId")

        pom {
            val internalPom = this as? MavenPomInternal
            name?.let(this@pom.name::set)
            requireForCentral(this.name != null, "name")
            description?.let(this@pom.description::set)
            requireForCentral(this.description != null, "description")
            url?.let(this@pom.url::set)
            requireForCentral(this.url != null, "url")

            packaging?.let(this@pom::setPackaging)
            requireForCentral(this.packaging != null, "packaging")

            if (licenses.isNotEmpty()) licenses {
                licenses.forEach {
                    license {
                        this.name.set(it.name)
                        this.url.set(it.url)
                        it.distribution?.value?.let(this.distribution::set)
                        it.comments?.let(this.comments::set)
                    }
                }
            }
            requireForCentral(licenses.isNotEmpty() || !internalPom?.licenses.isNullOrEmpty(), "licenses")

            val filteredDevs = developers.filterNot(DeveloperData::isEmpty)
            if (filteredDevs.isNotEmpty()) developers {
                filteredDevs.forEach {
                    developer {
                        it.id?.let(this.id::set)
                        it.url?.let(this.url::set)
                        it.timezone?.let(this.timezone::set)
                        it.roles.takeIf(List<String>::isNotEmpty)?.let(this.roles::set)
                        it.email?.let(this.email::set)
                        it.name?.let(this.name::set)
                        it.organization?.let(this.organization::set)
                        it.organizationUrl?.let(this.organizationUrl::set)
                        it.properties.takeIf(Map<String, String>::isNotEmpty)?.let(this.properties::set)
                    }
                }
            }
            requireForCentral(filteredDevs.isNotEmpty() || !internalPom?.developers.isNullOrEmpty(), "developers")

            if (scm != null) scm {
                connection.set(scm.connection)
                developerConnection.set(scm.developerConnection)
                requireForCentral(scm.url != null, "scm.url")
                scm.url?.let(this.url::set)
                scm.tag?.let(this.tag::set)
            }
            requireForCentral(scm != null || internalPom?.scm != null, "scm")
        }

        if (setupSigning) {
            setupSigning(this)
        } else if (verifyMavenCentral) {
            logger.warn("Calling `setupPublishing` without 'setupSigning' while verifying for Maven Central!")
            logger.warn("Please note that Maven Central requires signing")
        }

        if (setupNexusPublishing) setupNexusPublishing()
    }
}

fun Project.setupSigning(
    publication: Publication = (extensions["publishing"] as PublishingExtension).publications.first(),
) {
    println("Setting up signing for publication '${publication.name}'")
    val signing = extensions["signing"] as SigningExtension
    signing.sign(publication)
}

/**
 * Set up the [Nexus Publishing Plugin](https://github.com/gradle-nexus/publish-plugin)
 *
 * @param packageGroup The package group to publish to - **Example**: `io.github.username`
 * @return The Nexus publishing configuration
 */
fun Project.setupNexusPublishing(
    packageGroup: String = group.toString(),
    nexusUrl: URI = uri("https://s01.oss.sonatype.org/service/local/"), // Maven Central (new)
    snapshotRepositoryUrl: URI = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"),
    username: String = findProperty("mavenUsername") as? String
        ?: findProperty("OSSRH_USERNAME") as? String
        ?: findProperty("ossrhUsername") as? String
        ?: throw IllegalArgumentException("No username found!"),
    password: String = findProperty("mavenPassword") as? String
        ?: findProperty("OSSRH_USERNAME") as? String
        ?: findProperty("ossrhUsername") as? String
        ?: throw IllegalArgumentException("No username found!"),
): NexusPublishExtension = (extensions["nexusPublishing"] as NexusPublishExtension).apply {
    this.packageGroup.set(packageGroup)
    this@apply.repositories {
        sonatype {
            this.nexusUrl.set(nexusUrl)
            this.snapshotRepositoryUrl.set(snapshotRepositoryUrl)
            this.username.set(username)
            this.password.set(password)
        }
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
        val folder = file(rootDir).resolve(copyToDirectory).takeIf(File::exists) ?: File(copyToDirectory)
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
            val folder = file(rootDir).resolve(folderName).takeIf(File::exists) ?: File(folderName)
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
        "name" to name::toString, "version" to this.version::toString
    ),
    group: String = this.group.toString(),
    version: String = this.version.toString(),
    javaVersion: JavaVersion = JavaVersion.VERSION_1_8,
    artifactClassifier: String = "",
    textEncoding: String = "UTF-8",
    jar: () -> File,
) {
    setupMC(
        replacements = replacements,
        group = group,
        version = version,
        javaVersion = javaVersion,
        artifactClassifier = artifactClassifier,
        textEncoding = textEncoding,
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
        copyToDirectory = "$serverRoot/$mainServerName/plugins", dependsOnTask = deleteAll, jar = jar
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
    javaVersion: JavaVersion? = JavaVersion.VERSION_1_8,
    replacements: Map<String, () -> String>? = mapOf(
        "name" to name::toString, "version" to version::toString
    ),
    textEncoding: String? = "UTF-8",
    artifactClassifier: String? = "",
) {
    apply(plugin = "java")
    this.group = group
    this.version = version
    javaVersion?.let(::setJavaVersion)
    replacements?.let(::addReplacementsTask)
    textEncoding?.let(::setJavaTextEncoding)
    if (hasShadowPlugin()) {
        artifactClassifier?.let(::setShadowArchiveClassifier)
        addBuildShadowTask()
    }
}
