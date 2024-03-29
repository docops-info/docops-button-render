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
import io.docops.asciidoc.buttons.theme.GradientStyle
import io.docops.asciidoc.buttons.theme.Grouping
import io.docops.asciidoc.buttons.theme.GroupingOrder

@PanelDSL
open class ButtonItem {
    var link = ""
    var label = ""
    var description = ""
    var type = label
    var font: Font? = null
    var date: String = ""
    var authors = mutableListOf<String>()
    var links = mutableListOf<Link>()
    var gradientStyle: GradientStyle? = null
    infix fun author(author: String) {
        authors.add(author)
    }

    infix fun link(link: Link.() -> Unit) {
        links.add(Link().apply(link))
    }
}

data class Line(val line: String, val size: String = "30px")
@PanelDSL
class LargeButton : ButtonItem() {
    var buttonImage: ButtonImage? = null
    var line1: Line? = null
    var line2: Line? = null
}

@PanelDSL
class SlimButton : ButtonItem()

@PanelDSL
class PanelButton : ButtonItem()


@PanelDSL
class Button : ButtonItem()

@PanelDSL
class RoundButton : ButtonItem()

@PanelDSL
class RectangleButton : ButtonItem() {
    var buttonImage: ButtonImage? = null
    var leadingZeroNumbersOn: Boolean = false
    var numberColor: String = "#000000"
}

@PanelDSL
class Layout {
    var columns = 3
    var groupBy = Grouping.ORDER
    var groupOrder = GroupingOrder.ASCENDING
}

@PanelDSL
class Font {
    var family = "Arial, Helvetica, sans-serif"
    var size = "11px"
    var color = "#FFFFFF"
    var spacing = "normal"
    var bold = false
    var italic = false
    var underline = false
    var vertical = false
    var case = Case.NONE
}

@PanelDSL
enum class Case {
    UPPER, LOWER, SENTENCE, CAPITALIZE, SMALLCAPS, NONE
}


@PanelDSL
class ColorMap {

    var colors = mutableListOf<String>()
    private val colorPairs = mutableListOf<Pair<String, String>>()
    var colorDefs = ""
    internal var initialized = false


    fun color(color: String) {
        colors.add(color)
    }

    fun named(colorList: MutableList<String>) {
        colors.addAll(colorList)
    }
}


@PanelDSL
class ButtonTheme {
    var layout: Layout = Layout()
    var font: Font? = null
    var colorMap = ColorMap()
    var legendOn = false
    var newWin = true
    var dropShadow = 1
    var scale: Float = 1.0f
    var strokeColor = "gold"
    var panelOpacity = 0.9f
    var gradientStyle : GradientStyle? = null
    fun layout(layout: Layout.() -> Unit) {
        this.layout = Layout().apply(layout)
    }

    fun validate(): ButtonTheme {

        require(dropShadow in 0..9) { "Drop shadow value $dropShadow does not fall in the range 0..9" }
        return this
    }


    fun colorMap(colorMap: ColorMap.() -> Unit) {
        this.colorMap = ColorMap().apply(colorMap)
    }

}

@PanelDSL
class Link constructor(){
    constructor(label: String = "", href: String= "") : this() {
        this.label = label
        this.href = href
    }

    var label = ""
    var href = ""
}
@PanelDSL
class Panels {
    var columns = 3
    var isPdf = false
    var panelButtons = mutableListOf<PanelButton>()
    var slimButtons = mutableListOf<SlimButton>()
    var largeButtons = mutableListOf<LargeButton>()
    var roundButtons = mutableListOf<RoundButton>()
    var rectangleButtons = mutableListOf<RectangleButton>()
    var buttonTheme: ButtonTheme = ButtonTheme()

    var buttonType = ButtonType.BUTTON

    fun panel(panelButton: PanelButton.() -> Unit) {
        panelButtons.add(PanelButton().apply(panelButton))
        buttonType = ButtonType.BUTTON
    }

    fun pill(panelButton: PanelButton.() -> Unit) {
        panelButtons.add(PanelButton().apply(panelButton))
        buttonType = ButtonType.PILL
    }

    fun button(panelButton: PanelButton.() -> Unit) {
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

    fun rectangle(rectangleButton: RectangleButton.() -> Unit) {
        rectangleButtons.add(RectangleButton().apply(rectangleButton))
        buttonType = ButtonType.RECTANGLE
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
        if(rectangleButtons.size>0) {
            count++
            list.add("Rectangle")
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

fun panel(panel: Panels.() -> Unit): Panels {
    return Panels().apply(panel).validate()
}
fun font(font: Font.() -> Unit): Font {
    return Font().apply(font)
}