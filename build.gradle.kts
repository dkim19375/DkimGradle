import me.dkim19375.dkimgradle.data.pom.DeveloperData
import me.dkim19375.dkimgradle.data.pom.LicenseData
import me.dkim19375.dkimgradle.data.pom.SCMData
import me.dkim19375.dkimgradle.util.addKotlinKDocSourcesJars
import me.dkim19375.dkimgradle.util.setupJava
import me.dkim19375.dkimgradle.util.setupPublishing

plugins {
    `kotlin-dsl`
    `maven-publish`
    `java-gradle-plugin`
    alias(libs.plugins.dkim.gradle)
    alias(libs.plugins.dokkatoo)
    alias(libs.plugins.gradle.plugin.publish)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.licenser)
    alias(libs.plugins.spotless)
}

group = "me.dkim19375"
version = "1.4.5"

setupJava(javaVersion = JavaVersion.VERSION_11)

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib-jdk8"))

    // Plugins
    compileOnly(libs.kotlin.plugin)
    compileOnly(libs.licenser)
    compileOnly(libs.nexus.publish.plugin)
    compileOnly(libs.shadow)
    compileOnly(libs.spotless)
}

private object PluginInfo {
    const val ARTIFACT_ID = "dkim-gradle"
    const val DESCRIPTION = "Gradle plugin helpful in creating plugins"
    const val VCS_USERNAME = "dkim19375"
    const val VCS_REPOSITORY = "DkimGradle"
    const val VCS = "github.com/$VCS_USERNAME/$VCS_REPOSITORY"
}

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

addKotlinKDocSourcesJars()

setupPublishing(
    groupId = "io.github.dkim19375",
    artifactId = PluginInfo.ARTIFACT_ID,
    description = PluginInfo.DESCRIPTION,
    url = "https://${PluginInfo.VCS}",
    licenses = listOf(LicenseData.MIT),
    developers = listOf(
        DeveloperData(
            id = "dkim19375",
            roles = listOf("developer"),
            timezone = "America/New_York",
            url = "https://github.com/dkim19375",
        )
    ),
    scm = SCMData.generateGit(
        username = PluginInfo.VCS_USERNAME,
        repository = PluginInfo.VCS_REPOSITORY,
        developerSSH = true,
    ),
    publicationName = "pluginMaven",
    component = null,
    verifyMavenCentral = true,
    ignoreComponentVerification = true,
    setupSigning = false,
    setupNexusPublishing = false,
)