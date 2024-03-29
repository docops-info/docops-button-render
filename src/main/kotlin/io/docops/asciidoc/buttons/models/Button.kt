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

import io.docops.asciidoc.buttons.dsl.Font
import io.docops.asciidoc.buttons.dsl.Link
import io.docops.asciidoc.buttons.theme.GradientStyle
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Button(
    var title: String,
    var link: String,
    var description: String,
    var authors: MutableList<String>,
    var type: String,
    var date: String,
    var font: Font? = null,
    var backgroundColor: String? = null,
    var links:MutableList<Link>? = null,
    var buttonImage: ButtonImage? = null,
    var line1: String? = null,
    var line2: String? = null,
    var line1Size: String? = "30px",
    var line2Size: String? = "30px",
    var leadingZeroNumbersOn: Boolean = false,
    var gradientStyle: GradientStyle? = null,
    var numberColor: String = "#000000",
    var id: String = "ID${UUID.randomUUID().toString()}"
) {

    fun dateFromStr(): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        return LocalDate.parse(date, formatter)
    }

}
data class ButtonImage(val ref: String, val type: String = "image/png")

