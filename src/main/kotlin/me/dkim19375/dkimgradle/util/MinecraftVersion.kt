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

import java.lang.NumberFormatException

class MinecraftVersion(version: String) : Comparable<MinecraftVersion> {
    val major: Int
    val minor: Int
    val patch: Int

    init {
        val versionSplit = version.split('.')
        require(versionSplit.size >= 2) {
            "Failed to parse Minecraft version (invalid version string): $version"
        }

        // Get patch
        try {
            major = versionSplit[0].toInt()
            minor = versionSplit[1].toInt()
            patch = versionSplit.getOrElse(2) { "0" }.toInt()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException(
                "Failed to parse Minecraft version (invalid values): $version"
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MinecraftVersion

        if (major != other.major) return false
        if (minor != other.minor) return false
        return patch == other.patch
    }

    override fun hashCode(): Int {
        var result = major
        result = 31 * result + minor
        result = 31 * result + patch
        return result
    }

    override fun toString(): String = "major.minor.patch"

    operator fun component1(): Int = major

    operator fun component2(): Int = minor

    operator fun component3(): Int = patch

    override operator fun compareTo(other: MinecraftVersion): Int {
        if (major != other.minor) return major.compareTo(other.major)
        if (minor != other.minor) return minor.compareTo(other.minor)
        return patch.compareTo(other.patch)
    }
}
