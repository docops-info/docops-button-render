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

import io.docops.asciidoc.buttons.models.Button

@ThemeDSL
class Theme {
    companion object {
        private val fontWeights = listOf("bold", "normal", "italic")
    }
    var columns = 3
    //var colorMap = listOf("#5F4B8B", "#E69A8D", "#ADEFD1", "#00203F", "#ED2B33", "#D85A7F", "#E6A57E")
    var colorMap = listOf("#c8dfcc",
            "#b2a2eb",
            "#9bf6da",
            "#eea1d3",
            "#eccfa1",
            "#a3e6d4",
            "#fbb394",
            "#d5d2b6",
            "#eedbbf",
            "#dce5b7",
            "#b0f6bf",
            "#f0abb7",
            "#a7b5f5",
            "#c7a0f9",
            "#d4d4b0",
            "#e1bdbc",
            "#accdec",
            "#b5e4dd",
            "#a1fb88",
            "#eefab4")
    //var colorMap = listOf("url(#linear-gradient-0)", "url(#linear-gradient-1)", "url(#linear-gradient-2)", "url(#linear-gradient-3)", "url(#linear-gradient-4)", "url(#linear-gradient-5)")
    var groupBy = Grouping.DATE
    var groupOrder = GroupingOrder.ASCENDING
    var fontWeight = "normal"
    var type = ButtonType.BUTTON
    var newWin = true
    var legendOn = true
    var isPDF = false
    var defs = ""

    var typeMap = mutableMapOf<String, String>()
    infix fun typeIs (other: String) {
        this.type = ButtonType.valueOf(other)
    }
    internal fun validate(): Theme {
        require(fontWeights.contains(fontWeight)) {"not a valid font weight $fontWeight"}
        return this
    }
    fun buttonColor(button: Button): String {
       button.backgroundColor?.let {
           return it
       }
       val col = typeMap[button.type]
        return if(null == col) {
            val color = colorMap[typeMap.size % colorMap.size ]
            typeMap[button.type] = color
            color
        } else {
            col
        }
    }
    fun buttonTextColor(button: Button) : String {
         button.foregroundColor?.let {
             return it
         }
        return "white"
    }
}


val SlimCardsTheme = theme {
    //colorMap = listOf("#fbc1cc", "#fa99b2", "#1fe4f5", "#3fbafe", "#76b2fe", "#b69efe", "#60efbc", "#58d5c9", "#f588d8", "#c0a3e5")
    this.colorMap = listOf("#c8dfcc",
        "#b2a2eb",
        "#9bf6da",
        "#eea1d3",
        "#eccfa1",
        "#a3e6d4",
        "#fbb394",
        "#d5d2b6",
        "#eedbbf",
        "#dce5b7",
        "#b0f6bf",
        "#f0abb7",
        "#a7b5f5",
        "#c7a0f9",
        "#d4d4b0",
        "#e1bdbc",
        "#accdec",
        "#b5e4dd",
        "#a1fb88",
        "#eefab4")
    this typeIs "SLIM_CARD"
    groupBy= Grouping.TYPE
    groupOrder = GroupingOrder.ASCENDING
    columns = 4
}
val LargeCards = theme {
    var colorMap = listOf("#c8dfcc",
        "#b2a2eb",
        "#9bf6da",
        "#eea1d3",
        "#eccfa1",
        "#a3e6d4",
        "#fbb394",
        "#d5d2b6",
        "#eedbbf",
        "#dce5b7",
        "#b0f6bf",
        "#f0abb7",
        "#a7b5f5",
        "#c7a0f9",
        "#d4d4b0",
        "#e1bdbc",
        "#accdec",
        "#b5e4dd",
        "#a1fb88",
        "#eefab4")
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

fun theme(theme: Theme.()->Unit) : Theme {
    return Theme().apply(theme).validate()
}

