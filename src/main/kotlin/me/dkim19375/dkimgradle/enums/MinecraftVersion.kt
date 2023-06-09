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
    V1_7_2("1.7.2", PaperVersion.BELOW_1_9),
    V1_7_5("1.7.5", PaperVersion.BELOW_1_9),
    V1_7_8("1.7.8", PaperVersion.BELOW_1_9),
    V1_7_9("1.7.9", PaperVersion.BELOW_1_9),
    V1_7_10("1.7.10", PaperVersion.BELOW_1_9),
    V1_8_8("1.8.8", PaperVersion.BELOW_1_9),
    V1_9("1.9", PaperVersion.BELOW_1_17),
    V1_9_2("1.9.2", PaperVersion.BELOW_1_17),
    V1_9_4("1.9.4", PaperVersion.BELOW_1_17),
    V1_10("1.10", PaperVersion.BELOW_1_17),
    V1_10_2("1.10.2", PaperVersion.BELOW_1_17),
    V1_11("1.11", PaperVersion.BELOW_1_17),
    V1_11_1("1.11.1", PaperVersion.BELOW_1_17),
    V1_11_2("1.11.2", PaperVersion.BELOW_1_17),
    V1_12("1.12", PaperVersion.BELOW_1_17),
    V1_12_1("1.12.1", PaperVersion.BELOW_1_17),
    V1_12_2("1.12.2", PaperVersion.BELOW_1_17),
    V1_13("1.13", PaperVersion.BELOW_1_17),
    V1_13_1("1.13.1", PaperVersion.BELOW_1_17),
    V1_13_2("1.13.2", PaperVersion.BELOW_1_17),
    V1_14("1.14", PaperVersion.BELOW_1_17),
    V1_14_1("1.14.1", PaperVersion.BELOW_1_17),
    V1_14_2("1.14.2", PaperVersion.BELOW_1_17),
    V1_14_3("1.14.3", PaperVersion.BELOW_1_17),
    V1_14_4("1.14.4", PaperVersion.BELOW_1_17),
    V1_15("1.15", PaperVersion.BELOW_1_17),
    V1_15_1("1.15.1", PaperVersion.BELOW_1_17),
    V1_15_2("1.15.2", PaperVersion.BELOW_1_17),
    V1_16("1.16", PaperVersion.BELOW_1_17),
    V1_16_1("1.16.1", PaperVersion.BELOW_1_17),
    V1_16_2("1.16.2", PaperVersion.BELOW_1_17),
    V1_16_3("1.16.3", PaperVersion.BELOW_1_17),
    V1_16_4("1.16.4", PaperVersion.BELOW_1_17),
    V1_16_5("1.16.5", PaperVersion.BELOW_1_17),
    V1_17("1.17", PaperVersion.REST),
    V1_17_1("1.17.1", PaperVersion.REST),
    V1_18("1.18", PaperVersion.REST),
    V1_18_1("1.18.1", PaperVersion.REST),
    V1_18_2("1.18.2", PaperVersion.REST),
    V1_19("1.19", PaperVersion.REST),
    V1_19_1("1.19.1", PaperVersion.REST),
    V1_19_2("1.19.2", PaperVersion.REST),
    V1_19_3("1.19.3", PaperVersion.REST),
    V1_19_4("1.19.4", PaperVersion.REST),
    V1_20("1.20", PaperVersion.REST),
}