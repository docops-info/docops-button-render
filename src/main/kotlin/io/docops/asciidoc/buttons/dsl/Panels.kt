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

package io.docops.asciidoc.buttons.dsl

import io.docops.asciidoc.buttons.models.ButtonImage
import io.docops.asciidoc.buttons.theme.ButtonType
import io.docops.asciidoc.buttons.theme.Grouping
import io.docops.asciidoc.buttons.theme.GroupingOrder

@PanelDSL
open class ButtonItem {
    var link = ""
    var label = ""
    var description = ""
    var type = label
}
@PanelDSL
class LargeButton: ButtonItem() {
    var authors = mutableListOf<String>()
    var date: String = ""
    var base64Image: ButtonImage? = null
    infix fun author(author: String) {
        authors.add(author)
    }
}
@PanelDSL
class SlimButton: ButtonItem() {
    var authors = mutableListOf<String>()
    var date: String = ""

    infix fun author(author: String) {
        authors.add(author)
    }
}
@PanelDSL
class PanelButton : ButtonItem() {

}

@PanelDSL
class RoundButton : ButtonItem() {

}
@PanelDSL
class Layout{
    var columns = 3
    var groupBy = Grouping.TITLE
    var groupOrder = GroupingOrder.ASCENDING
}

enum class FontWeight {
    normal, italic, bold
}

@PanelDSL
class Font {
    var color = "white"
    var weight = FontWeight.normal
}


@PanelDSL
class ColorMap {
    private val defaultColors = listOf("url(#linear-gradient-0)", "url(#linear-gradient-1)", "url(#linear-gradient-2)", "url(#linear-gradient-3)", "url(#linear-gradient-4)", "url(#linear-gradient-5)")
    var colors = mutableListOf<String>()
    private val colorPairs = mutableListOf<Pair<String, String>>()
    var colorDefs = ""
    internal var initialized = false
    fun gradient(startColor: String, endColor: String) {
        colorPairs.add(Pair(startColor, endColor))
    }
    fun color(color: String) {
        colors.add(color)
    }

    private fun toLinearGradient() : String {
        val sb = StringBuilder()
        colorPairs.forEachIndexed { index, pair ->
            sb.append("""
            <linearGradient id="gradient-$index" gradientUnits="userSpaceOnUse" x1="781.482" y1="79.988" x2="781.482" y2="49.983">
                <stop offset="0" stop-color="${pair.first}" stop-opacity="1"/>
                <stop offset="1" stop-color="${pair.second}" stop-opacity="1"/>
            </linearGradient>
            """.trimIndent())
            colors.add("url(#gradient-$index)")
        }
        return sb.toString()
    }
    fun setup(): ColorMap {
        colorDefs = toLinearGradient()
        if(colorDefs.isEmpty() && colors.isEmpty()) {
            colors.addAll(defaultColors)
        }
        initialized = true
        return this
    }
}


@PanelDSL
class ButtonTheme {
    var layout: Layout = Layout()
    var font = Font()
    var colorMap = ColorMap()
    var legendOn = true
    var newWin = true

    fun layout(layout: Layout.()->Unit) {
        this.layout = Layout().apply(layout)
    }
    fun validate(): ButtonTheme {
        if(!colorMap.initialized) {
            colorMap.setup()
        }
        return this
    }
    fun font(font: Font.()->Unit) {
        this.font = Font().apply(font)
    }
    fun colorMap(colorMap: ColorMap.()->Unit) {
        this.colorMap = ColorMap().apply(colorMap).setup()
    }

}
@PanelDSL
class Panels {
    var columns = 3
    var isPdf = false
    var panelButtons = mutableListOf<PanelButton>()
    var slimButtons = mutableListOf<SlimButton>()
    var largeButtons = mutableListOf<LargeButton>()
    var roundButtons = mutableListOf<RoundButton>()
    var buttonTheme: ButtonTheme = ButtonTheme()

    var buttonType = ButtonType.BUTTON

    fun panel(panelButton: PanelButton.()->Unit) {
        panelButtons.add(PanelButton().apply(panelButton))
        buttonType = ButtonType.BUTTON
    }
    fun slim(slimButton: SlimButton.()->Unit) {
        slimButtons.add(SlimButton().apply(slimButton))
        buttonType = ButtonType.SLIM_CARD
    }
    fun large(largeButton: LargeButton.()->Unit) {
        largeButtons.add(LargeButton().apply(largeButton))
        buttonType = ButtonType.LARGE_CARD
    }

    fun round(roundButton: RoundButton.() -> Unit) {
        roundButtons.add(RoundButton().apply(roundButton))
        buttonType = ButtonType.ROUND
    }
    fun theme(buttonTheme: ButtonTheme.()-> Unit) {
        this.buttonTheme = ButtonTheme().apply(buttonTheme).validate()
    }
    fun validate() : Panels {
        var count = 0
        val list = mutableListOf<String>()
        if(largeButtons.size>0) {
            count++
            list.add("Large")
        }
        if(slimButtons.size>0) {
            count++
            list.add("Slim")
        }
        if(panelButtons.size>0) {
            count++
            list.add("Panel")
        }
        if(roundButtons.size>0) {
            count++
            list.add("Round")
        }
        require(count<2){"More than one button type specified, see -> $list, only one type supported at a time"}
        require(count == 1) {
            "No Buttons were added"
        }
        return this
    }
}

fun panels(panel: Panels.() -> Unit) : Panels {
    return  Panels().apply(panel).validate()
}
