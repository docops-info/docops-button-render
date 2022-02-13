/*
 * Copyright 2020 The DocOps Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.docops.asciidoc.buttons.models

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Button(
    var title: String,
    var link: String,
    var description: String,
    var authors: MutableList<String>,
    var type: String,
    var date: String,
    var foregroundColor: String? = null,
    var backgroundColor: String? = null,
    var base64Image: ButtonImage? = null
) {

    fun dateFromStr(): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        return LocalDate.parse(date, formatter)
    }
}
data class ButtonImage(val base64Str: String, val type: String = "image/png")

fun String.escapeXml(): String {
    val sb = StringBuilder()
    for (element in this) {
        when (val c: Char = element) {
            '<' -> sb.append("&lt;")
            '>' -> sb.append("&gt;")
            '\"' -> sb.append("&quot;")
            '&' -> sb.append("&amp;")
            '\'' -> sb.append("&apos;")
            else -> if (c.code > 0x7e) {
                sb.append("&#" + c.code + ";")
            } else sb.append(c)
        }
    }
    return sb.toString()
}