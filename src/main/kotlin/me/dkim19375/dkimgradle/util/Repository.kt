@file:Suppress("unused")

package me.dkim19375.dkimgradle.util

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

enum class Repository(internal val url: String) {
    SPIGOT("https://hub.spigotmc.org/nexus/content/repositories/snapshots/"),
    PAPER("https://repo.papermc.io/repository/maven-public/"),
    JITPACK("https://jitpack.io/"),
    TRIUMPH_RELEASES("https://repo.triumphteam.dev/releases/"),
    TRIUMPH_SNAPSHOTS("https://repo.triumphteam.dev/snapshots/"),
    VIA_VERSION("https://repo.viaversion.com/everything/"),
    PROTOCOL_LIB("https://repo.dmulloy2.net/repository/public/"),
    SCARSZ("https://nexus.scarsz.me/content/groups/public/"),
    CODE_MC("https://repo.codemc.org/repository/maven-public/"),
    SONATYPE_RELEASES_OLD("https://oss.sonatype.org/content/repositories/releases/"),
    SONATYPE_SNAPSHOTS_OLD("https://oss.sonatype.org/content/repositories/snapshots/"),
    SONATYPE_RELEASES("https://s01.oss.sonatype.org/content/repositories/releases/"),
    SONATYPE_SNAPSHOTS("https://s01.oss.sonatype.org/content/repositories/snapshots/"),
    UMB_CRAFT("https://nexus.umbcraft.online/repository/umbcraft-pub/"),
    PLACEHOLDER_API("https://repo.extendedclip.com/content/repositories/placeholderapi/"),
    ALESSIO_DP("https://repo.alessiodp.com/releases/"),
    MULTIVERSE("https://repo.onarandombox.com/content/groups/public/"),
    EXTENDED_CLIP("https://repo.extendedclip.com/content/repositories/public/"),
    ENGINE_HUB("https://maven.enginehub.org/repo/"),

    // keep at bottom
    ESSENTIALS_RELEASES("https://repo.essentialsx.net/releases/"),
    ESSENTIALS_SNAPSHOTS("https://repo.essentialsx.net/snapshots/"),
}

fun RepositoryHandler.mavenAll(): Map<Repository, MavenArtifactRepository> = Repository.values().associateWith {
    maven(it.url)
}

fun RepositoryHandler.maven(repository: Repository): MavenArtifactRepository = maven(repository.url)