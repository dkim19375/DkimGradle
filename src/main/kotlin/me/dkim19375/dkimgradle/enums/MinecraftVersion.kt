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

@Suppress("unused")
enum class MinecraftVersion(val version: String, val type: PaperVersion) {
    V1_7_10("1.7.10", PaperVersion.BELOW_1_9),
    V1_8_8("1.8.8", PaperVersion.BELOW_1_9),
    V1_9_4("1.9.4", PaperVersion.BELOW_1_17),
    V1_10_2("1.10.2", PaperVersion.BELOW_1_17),
    V1_11_2("1.11.2", PaperVersion.BELOW_1_17),
    V1_12_2("1.12.2", PaperVersion.BELOW_1_17),
    V1_13_2("1.13.2", PaperVersion.BELOW_1_17),
    V1_14_4("1.14.4", PaperVersion.BELOW_1_17),
    V1_15_2("1.15.2", PaperVersion.BELOW_1_17),
    V1_16_4("1.16.4", PaperVersion.BELOW_1_17),
    V1_16_5("1.16.5", PaperVersion.BELOW_1_17),
    V1_17_1("1.17.1", PaperVersion.REST),
    V1_18_2("1.18.2", PaperVersion.REST),
    V1_19_2("1.19.2", PaperVersion.REST),
}