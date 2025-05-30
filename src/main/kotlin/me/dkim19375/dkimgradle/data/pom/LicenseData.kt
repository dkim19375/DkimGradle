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

data class LicenseData(
    val name: String,
    val url: String,
    val distribution: LicenseDistribution? = null,
    val comments: String? = null,
) {
    companion object {
        val MIT =
            LicenseData(
                name = "MIT",
                url = "https://opensource.org/licenses/MIT",
                distribution = LicenseDistribution.REPO,
            )
        val APACHE_2_0 =
            LicenseData(
                name = "Apache-2.0",
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt",
                distribution = LicenseDistribution.REPO,
            )
        val GPL_V3 =
            LicenseData(
                name = "GPL-3.0",
                url = "https://www.gnu.org/licenses/gpl-3.0.en.html",
                distribution = LicenseDistribution.REPO,
            )
    }

    enum class LicenseDistribution(val value: String) {
        /** May be downloaded from a Maven repository */
        REPO("repo"),

        /**
         * Must be manually installed
         *
         * @constructor Create empty Manual
         */
        MANUAL("manual"),
    }
}
