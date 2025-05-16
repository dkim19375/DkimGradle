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

@file:Suppress("unused")

package me.dkim19375.dkimgradle.enums

import org.gradle.api.artifacts.ArtifactRepositoryContainer
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

enum class Repository(internal val url: String) {
    /** [ArtifactRepositoryContainer.MAVEN_CENTRAL_URL] */
    MAVEN_CENTRAL(ArtifactRepositoryContainer.MAVEN_CENTRAL_URL),
    /**
     * [https://hub.spigotmc.org/nexus/content/repositories/snapshots/](https://hub.spigotmc.org/nexus/content/repositories/snapshots/)
     */
    SPIGOT("https://hub.spigotmc.org/nexus/content/repositories/snapshots/"),
    /**
     * [https://repo.papermc.io/repository/maven-public/](https://repo.papermc.io/repository/maven-public/)
     */
    PAPER("https://repo.papermc.io/repository/maven-public/"),
    /** [https://jitpack.io/](https://jitpack.io/) */
    JITPACK("https://jitpack.io/"),
    /** [https://repo.clojars.org/](https://repo.clojars.org/) */
    CLOJARS("https://repo.clojars.org/"),
    /** [https://m2.dv8tion.net/releases/](https://m2.dv8tion.net/releases/) */
    DV8TION("https://m2.dv8tion.net/releases/"),
    /** [https://repo.triumphteam.dev/releases/](https://repo.triumphteam.dev/releases/) */
    TRIUMPH_RELEASES("https://repo.triumphteam.dev/releases/"),
    /** [https://repo.triumphteam.dev/snapshots/](https://repo.triumphteam.dev/snapshots/) */
    TRIUMPH_SNAPSHOTS("https://repo.triumphteam.dev/snapshots/"),
    /** [https://repo.viaversion.com/everything/](https://repo.viaversion.com/everything/) */
    VIA_VERSION("https://repo.viaversion.com/everything/"),
    /**
     * [https://repo.dmulloy2.net/repository/public/](https://repo.dmulloy2.net/repository/public/)
     */
    PROTOCOL_LIB("https://repo.dmulloy2.net/repository/public/"),
    /**
     * [https://nexus.scarsz.me/content/groups/public/](https://nexus.scarsz.me/content/groups/public/)
     */
    SCARSZ("https://nexus.scarsz.me/content/groups/public/"),
    /**
     * [https://repo.codemc.org/repository/maven-public/](https://repo.codemc.org/repository/maven-public/)
     */
    CODE_MC("https://repo.codemc.org/repository/maven-public/"),
    /**
     * [https://oss.sonatype.org/content/repositories/releases/](https://oss.sonatype.org/content/repositories/releases/)
     */
    SONATYPE_RELEASES_OLD("https://oss.sonatype.org/content/repositories/releases/"),
    /**
     * [https://oss.sonatype.org/content/repositories/snapshots/](https://oss.sonatype.org/content/repositories/snapshots/)
     */
    SONATYPE_SNAPSHOTS_OLD("https://oss.sonatype.org/content/repositories/snapshots/"),
    /**
     * [https://s01.oss.sonatype.org/content/repositories/releases/](https://s01.oss.sonatype.org/content/repositories/releases/)
     */
    SONATYPE_RELEASES_LEGACY("https://s01.oss.sonatype.org/content/repositories/releases/"),
    /**
     * [https://s01.oss.sonatype.org/content/repositories/snapshots/](https://s01.oss.sonatype.org/content/repositories/snapshots/)
     */
    SONATYPE_SNAPSHOTS_LEGACY("https://s01.oss.sonatype.org/content/repositories/snapshots/"),
    /**
     * [https://central.sonatype.com/repository/maven-snapshots/](https://central.sonatype.com/repository/maven-snapshots/)
     */
    SONATYPE_SNAPSHOTS("https://central.sonatype.com/repository/maven-snapshots/"),
    /**
     * [https://nexus.umbcraft.online/repository/umbcraft-pub/](https://nexus.umbcraft.online/repository/umbcraft-pub/)
     */
    UMB_CRAFT("https://nexus.umbcraft.online/repository/umbcraft-pub/"),
    /**
     * [https://repo.extendedclip.com/content/repositories/placeholderapi/](https://repo.extendedclip.com/content/repositories/placeholderapi/)
     */
    PLACEHOLDER_API("https://repo.extendedclip.com/content/repositories/placeholderapi/"),
    /** [https://repo.alessiodp.com/releases/](https://repo.alessiodp.com/releases/) */
    ALESSIO_DP("https://repo.alessiodp.com/releases/"),
    /**
     * [https://repo.onarandombox.com/content/groups/public/](https://repo.onarandombox.com/content/groups/public/)
     */
    MULTIVERSE("https://repo.onarandombox.com/content/groups/public/"),
    /**
     * [https://repo.extendedclip.com/content/repositories/public/](https://repo.extendedclip.com/content/repositories/public/)
     */
    EXTENDED_CLIP("https://repo.extendedclip.com/content/repositories/public/"),
    /** [https://maven.enginehub.org/repo/](https://maven.enginehub.org/repo/) */
    ENGINE_HUB("https://maven.enginehub.org/repo/"),
    /** [https://redempt.dev/](https://redempt.dev/) */
    REDEMPT("https://redempt.dev/"),
    /** [https://repo.kryptonmc.org/releases/](https://repo.kryptonmc.org/releases/) */
    KRYPTON_RELEASES("https://repo.kryptonmc.org/releases/"),
    /** [https://repo.kryptonmc.org/snapshots/](https://repo.kryptonmc.org/snapshots/) */
    KRYPTON_SNAPSHOTS("https://repo.kryptonmc.org/snapshots/"),

    // keep at bottom
    /** [https://repo.essentialsx.net/releases/](https://repo.essentialsx.net/releases/) */
    ESSENTIALS_RELEASES("https://repo.essentialsx.net/releases/"),
    /** [https://repo.essentialsx.net/snapshots/](https://repo.essentialsx.net/snapshots/) */
    ESSENTIALS_SNAPSHOTS("https://repo.essentialsx.net/snapshots/"),
}

fun RepositoryHandler.maven(
    vararg repositories: Repository
): Map<Repository, MavenArtifactRepository> = repositories.associateWith { maven(it.url) }

fun RepositoryHandler.mavenAll(
    vararg exclude: Repository
): Map<Repository, MavenArtifactRepository> =
    Repository.values().filterNot(exclude::contains).associateWith { maven(it.url) }
