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
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.spigotAPI(version: String): String =
    "org.spigotmc:spigot-api:${getVersionString(version)}"

fun DependencyHandler.spigotNMS(version: String): String =
    "org.spigotmc:spigot:${getVersionString(version)}"

fun DependencyHandler.paper(version: String): String {
    val paperVersion: PaperVersion = PaperVersion.parse(version)
    return "${paperVersion.groupID}:${paperVersion.artifactID}:${getVersionString(version)}"
}

fun DependencyHandler.getVersionString(version: String): String =
    "${version}-R0.1-SNAPSHOT"