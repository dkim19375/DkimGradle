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

package me.dkim19375.dkimgradle.delegate

import org.gradle.api.Project
import org.gradle.api.Task
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class TaskRegisterDelegate(
    private val project: Project,
    private val action: Task.(String) -> Unit,
) : ReadOnlyProperty<Any?, Task> {
    operator fun provideDelegate(ref: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, Task> =
        TaskRegisterDelegate(project, action)

    override fun getValue(thisRef: Any?, property: KProperty<*>): Task = project.tasks.register(property.name) {
        action(this, property.name)
    }.get()
}