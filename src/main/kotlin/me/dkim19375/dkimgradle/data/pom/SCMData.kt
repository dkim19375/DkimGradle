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

package me.dkim19375.dkimgradle.data.pom

import me.dkim19375.dkimgradle.annotation.API

/**
 * More information:
 * - [Maven Pom Reference](https://maven.apache.org/pom.html#scm)
 * - [Maven Central SCM
 *   Information](https://central.sonatype.org/publish/requirements/#scm-information)
 */
data class SCMData(
    /**
     * This URL gives read access **(Does not have to be public)**
     *
     * **Example:** `scm:git:https://github.com/Username/Repository.git`
     *
     * *More information can be found in the docs of this class*
     */
    val connection: String,
    /**
     * This URL gives write access **(Does not have to be public)**
     *
     * **Examples:**
     * - `scm:git:ssh://github.com:Username/Repository.git` (**SSH setup required!**)
     * - `scm:git:https://github.com/Username/Repository.git`
     *
     * *More information can be found in the docs of this class*
     */
    val developerConnection: String,
    /**
     * This URL gives the browser access to browse the repository **(Does not have to be public)**
     *
     * **Example:** `https://github.com/Username/Repository`
     *
     * *More information can be found in the docs of this class*
     */
    val url: String? = null,
    /**
     * This specifies the tag that this project lives under
     *
     * **Example:** HEAD
     *
     * *More information can be found in the docs of this class*
     */
    val tag: String? = null,
) {
    companion object {

        /**
         * Generate a SCMData for git easily with a lower chance of making an error
         *
         * @param username The username or organization name of the repository owner
         * @param repository The name of the repository
         * @param developerSSH Whether to use the SSH protocol for the developer connection
         * @param connectionSSH Whether to use the SSH protocol for the connection
         * @param host The git server host (ex: "github.com")
         * @param connectionsProtocol The protocol for the connections (ex: "https://", "ssh://")
         * @param developerConnectionProtocol The protocol for the developer connection (this
         *   parameter should only be configured if the protocol is not SSH and is also not the same
         *   as [connectionsProtocol])
         * @param browserProtocol The protocol for the browser (ex: "https://")
         * @param tag The tag (ex: "HEAD")
         * @return an [SCMData] with properly configured values
         */
        @API
        fun generateGit(
            username: String,
            repository: String,
            developerSSH: Boolean,
            connectionSSH: Boolean = false,
            host: String = "github.com",
            developerConnectionProtocol: String = if (developerSSH) "git@" else "https://",
            connectionsProtocol: String = if (connectionSSH) "git@" else "https://",
            browserProtocol: String = "https://",
            tag: String? = null,
        ): SCMData =
            SCMData(
                connection =
                    "scm:git:$connectionsProtocol$host${
                if (connectionSSH) ':' else '/'
            }$username/$repository.git",
                developerConnection =
                    "scm:git:$developerConnectionProtocol$host${
                if (developerSSH) ':' else '/'
            }$username/$repository.git",
                url = "$browserProtocol$host/$username/$repository",
                tag = tag,
            )
    }
}
