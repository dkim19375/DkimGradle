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

package me.dkim19375.dkimgradle.enums

enum class PaperVersion(val groupID: String, val artifactID: String) {
    BELOW_1_9("org.github.paperspigot", "paperspigot-api"),
    BELOW_1_17("com.destroystokyo.paper", "paper-api"),
    REST("io.papermc.paper", "paper-api");
}

fun getPaperVersion(version: String): PaperVersion {
    // Split version string
    val versionSplit = version.split(".")
    if (versionSplit.size < 2) {
        println("Failed to parse Paper Minecraft version: $version")
        return PaperVersion.BELOW_1_9
    }

    // Get major and minor version
    val major: Int
    val minor: Int
    try {
        major = versionSplit[0].toInt()
        minor = versionSplit[1].toInt()
    } catch (e: NumberFormatException) {
        println("Failed to parse Paper Minecraft version: $version")
        return PaperVersion.BELOW_1_9
    }

    // Return correct PaperVersion
    if (major < 1) return PaperVersion.BELOW_1_9
    if (major == 1) {
        if (minor < 9) return PaperVersion.BELOW_1_9
        if (minor < 17) return PaperVersion.BELOW_1_17
    }
    return PaperVersion.REST
}