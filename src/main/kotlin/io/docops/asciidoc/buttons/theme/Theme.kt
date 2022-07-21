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

package io.docops.asciidoc.buttons.theme

import io.docops.asciidoc.buttons.dsl.Font
import io.docops.asciidoc.buttons.dsl.font
import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.utils.escapeXml
import java.util.StringJoiner

@ThemeDSL
class Theme {
    companion object {
        private val fontWeights = listOf("bold", "normal", "italic")
    }

    var columns = 3

    //var colorMap = listOf("#5F4B8B", "#E69A8D", "#ADEFD1", "#00203F", "#ED2B33", "#D85A7F", "#E6A57E")
    var colorMap = mutableListOf(
        "#DA79EC",
        "#DECD5E",
        "#F6AB4C",
        "#F8827D",
        "#FB0F5F",
        "#ED4661",
        "#7FA0BC",
        "#74F78F",
        "#EBFB01",
        "#1EB20B",
        "#06A69F",
        "#E33241",
        "#E21A0E",
        "#CAF728",
        "#CEFF48",
        "#F62A0C",
        "#2913ED",
        "#324DE5",
        "#32B0A1",
        "#1EB178"
    )

    //var colorMap = listOf("url(#linear-gradient-0)", "url(#linear-gradient-1)", "url(#linear-gradient-2)", "url(#linear-gradient-3)", "url(#linear-gradient-4)", "url(#linear-gradient-5)")
    var groupBy = Grouping.DATE
    var groupOrder = GroupingOrder.ASCENDING
    var fontWeight = "normal"
    var type = ButtonType.BUTTON
    var newWin = true
    var legendOn = true
    var isPDF = false
    var defs = ""
    var dropShadow = 1
    var font: Font = Font()

    var typeMap = mutableMapOf<String, String>()
    infix fun typeIs(other: String) {
        this.type = ButtonType.valueOf(other)
    }

    internal fun validate(): Theme {
        require(fontWeights.contains(fontWeight)) { "not a valid font weight $fontWeight" }
        require(dropShadow in 0..9) { "dropShadow value $dropShadow does not fall in the range 0..9" }
        return this
    }

    fun buttonColor(button: Button): String {
        var color = ""
        button.backgroundColor?.let {
            color = it
        }
        val col = typeMap[button.type]

        if (null == col && color.isEmpty()) {
            color = colorMap[typeMap.size % colorMap.size]
            typeMap[button.type] = color
        } else {
            if (col != null) {
                color = col
            }
        }

        return color
    }

    fun buttonTextColor(button: Button): String {
        var f = button.font
        if(f == null) {
            f = this.font
        }
        //language=css
        var style = ""
        f.let {
            style += "style=\""
            if(it.color.isNotEmpty()) {
                style += """fill: ${it.color};"""
            }
            if(it.font.isNotEmpty()) {
                style += " font-family: ${it.font.escapeXml()};"
            }
            if(it.size.isNotEmpty()) {
                style +=" font-size: ${it.size};"
            }
            style += " font-weight: ${it.weight};"
            return style + """" text-decoration="${it.decoration.escapeXml()}""""
        }
    }
}


val SlimCardsTheme = theme {
    this typeIs "SLIM_CARD"
    groupBy = Grouping.TYPE
    groupOrder = GroupingOrder.ASCENDING
    columns = 4
}
val LargeCards = theme {
    this typeIs "LARGE_CARD"
}

enum class ButtonType {
    BUTTON, ROUND, LARGE_CARD, SLIM_CARD
}

enum class Grouping {
    TYPE, TITLE, AUTHOR, DATE
}

enum class GroupingOrder {
    ASCENDING, DESCENDING
}

fun theme(theme: Theme.() -> Unit): Theme {
    return Theme().apply(theme).validate()
}

