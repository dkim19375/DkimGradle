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
    REDEMPT("https://redempt.dev/"),
    KRYPTON_RELEASES("https://repo.kryptonmc.org/releases/"),
    KRYPTON_SNAPSHOTS("https://repo.kryptonmc.org/snapshots/"),

    // keep at bottom
    ESSENTIALS_RELEASES("https://repo.essentialsx.net/releases/"),
    ESSENTIALS_SNAPSHOTS("https://repo.essentialsx.net/snapshots/"),
}

fun RepositoryHandler.mavenAll(vararg exclude: Repository): Map<Repository, MavenArtifactRepository> =
    Repository.values().filterNot(exclude::contains).associateWith {
        maven(it.url)
    }

fun RepositoryHandler.maven(repository: Repository): MavenArtifactRepository = maven(repository.url)