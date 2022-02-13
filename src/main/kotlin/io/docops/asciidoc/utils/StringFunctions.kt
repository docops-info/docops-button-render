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

package io.docops.asciidoc.utils

import java.util.*

class StringFunctions {
}

fun addLinebreaks(input: String, maxLineLength: Int): MutableList<StringBuilder> {
    val list = mutableListOf<StringBuilder>()

    val tok = StringTokenizer(input, " ")
    var output = StringBuilder(input.length)
    var lineLen = 0
    while (tok.hasMoreTokens()) {
        val word = tok.nextToken()
        if (lineLen + word.length > maxLineLength) {
            list.add(output)
            output = StringBuilder(input.length)
            lineLen = 0
        }
        output.append("$word ")
        lineLen += word.length
    }
    if (list.size == 0 || lineLen > 0) {
        list.add(output)
    }
    return list
}
