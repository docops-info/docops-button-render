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
    var font = Font()


}

@PanelDSL
class LargeButton : ButtonItem() {
    var authors = mutableListOf<String>()
    var date: String = ""
    var buttonImage: ButtonImage? = null
    infix fun author(author: String) {
        authors.add(author)
    }
}

@PanelDSL
class SlimButton : ButtonItem() {
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
class Layout {
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
    var font = """-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol""""
    var size = "9pt"
    var decoration = "none"
}


@PanelDSL
class ColorMap {

    var colors = mutableListOf(
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
        "#1EB178")
    private val colorPairs = mutableListOf<Pair<String, String>>()
    var colorDefs = ""
    internal var initialized = false


    fun color(color: String) {
        colors.add(color)
    }


}


@PanelDSL
class ButtonTheme {
    var layout: Layout = Layout()
    var font = Font()
    var colorMap = ColorMap()
    var legendOn = true
    var newWin = true
    var dropShadow = 1

    fun layout(layout: Layout.() -> Unit) {
        this.layout = Layout().apply(layout)
    }

    fun validate(): ButtonTheme {

        require(dropShadow in 0..9) { "Drop shadow value $dropShadow does not fall in the range 0..9" }
        return this
    }

    fun font(font: Font.() -> Unit) {
        this.font = Font().apply(font)
    }

    fun colorMap(colorMap: ColorMap.() -> Unit) {
        this.colorMap = ColorMap().apply(colorMap)
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

    fun panel(panelButton: PanelButton.() -> Unit) {
        panelButtons.add(PanelButton().apply(panelButton))
        buttonType = ButtonType.BUTTON
    }

    fun slim(slimButton: SlimButton.() -> Unit) {
        slimButtons.add(SlimButton().apply(slimButton))
        buttonType = ButtonType.SLIM_CARD
    }

    fun large(largeButton: LargeButton.() -> Unit) {
        largeButtons.add(LargeButton().apply(largeButton))
        buttonType = ButtonType.LARGE_CARD
    }

    fun round(roundButton: RoundButton.() -> Unit) {
        roundButtons.add(RoundButton().apply(roundButton))
        buttonType = ButtonType.ROUND
    }

    fun theme(buttonTheme: ButtonTheme.() -> Unit) {
        this.buttonTheme = ButtonTheme().apply(buttonTheme).validate()
    }

    fun validate(): Panels {
        var count = 0
        val list = mutableListOf<String>()
        if (largeButtons.size > 0) {
            count++
            list.add("Large")
        }
        if (slimButtons.size > 0) {
            count++
            list.add("Slim")
        }
        if (panelButtons.size > 0) {
            count++
            list.add("Panel")
        }
        if (roundButtons.size > 0) {
            count++
            list.add("Round")
        }
        require(count < 2) { "More than one button type specified, see -> $list, only one type supported at a time" }
        require(count == 1) {
            "No Buttons were added"
        }
        return this
    }
}

fun panels(panel: Panels.() -> Unit): Panels {
    return Panels().apply(panel).validate()
}
 fun font(font: Font.() -> Unit): Font {
    return Font().apply(font)
}