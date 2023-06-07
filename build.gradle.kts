import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `maven-publish`
    `java-gradle-plugin`
    kotlin("jvm") version "1.8.22"
    id("org.jetbrains.dokka") version "1.8.20"
    id("org.cadixdev.licenser") version "0.6.1"
    id("com.gradle.plugin-publish") version "1.2.0"
}

group = "me.dkim19375"
version = "1.1.4"

tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib-jdk8"))
}

license {
    header.set(rootProject.resources.text.fromFile("LICENSE"))
    include("**/*.kt", "**/*.groovy")
}

private object PluginInfo {
    const val ARTIFACT_ID = "dkim-gradle"
    const val DESCRIPTION = "Gradle plugin helpful in creating plugins"
    const val VCS = "github.com/dkim19375/DkimGradle"
}

@Suppress("UnstableApiUsage")
gradlePlugin {
    isAutomatedPublishing = true
    website.set("https://${PluginInfo.VCS}")
    vcsUrl.set("https://${PluginInfo.VCS}")
    plugins {
        create(PluginInfo.ARTIFACT_ID) {
            id = "io.github.dkim19375.${PluginInfo.ARTIFACT_ID}"
            implementationClass = "me.dkim19375.dkimgradle.DkimGradlePlugin"
            version = project.version
            displayName = "Dkim Gradle Plugin"
            description = PluginInfo.DESCRIPTION
            tags.set(listOf("dkim"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("pluginMaven") {
            groupId = "io.github.dkim19375"
            artifactId = PluginInfo.ARTIFACT_ID
            version = project.version as String

            pom {
                name.set("DkimGradle")
                description.set(PluginInfo.DESCRIPTION)
                url.set("https://${PluginInfo.VCS}")

                packaging = "jar"

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("dkim19375")
                    }
                }

                scm {
                    connection.set("scm:git:git://${PluginInfo.VCS}git")
                    developerConnection.set("scm:git:ssh://${PluginInfo.VCS}.git")
                    url.set("https://${PluginInfo.VCS}")
                }
            }
        }
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}