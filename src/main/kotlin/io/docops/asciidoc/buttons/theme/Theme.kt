

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

import io.docops.asciidoc.buttons.dsl.Case
import io.docops.asciidoc.buttons.dsl.Font
import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.utils.escapeXml
import java.awt.Color

import java.util.*

@ThemeDSL
class Theme {
    companion object {
        private val fontWeights = listOf("bold", "normal", "italic")
    }

    val id = "DocOps-${UUID.randomUUID()}"
    var columns = 3

    //var colorMap = listOf("#5F4B8B", "#E69A8D", "#ADEFD1", "#00203F", "#ED2B33", "#D85A7F", "#E6A57E")
    var colorMap = mutableListOf(
        "#2913ED",
        "#324DE5",
        "#32B0A1",
        "#1EB178",
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
        "#F62A0C"

    )

    //var colorMap = listOf("url(#linear-gradient-0)", "url(#linear-gradient-1)", "url(#linear-gradient-2)", "url(#linear-gradient-3)", "url(#linear-gradient-4)", "url(#linear-gradient-5)")
    var groupBy = Grouping.ORDER
    var groupOrder = GroupingOrder.ASCENDING
    var fontWeight = "normal"
    var type = ButtonType.BUTTON
    var newWin = true
    var legendOn = false
    var isPDF = false
    var defs = ""
    var dropShadow = 1
    var font: Font = Font()

    var typeMap = mutableMapOf<String, String>()
    val buttonStyleMap = mutableMapOf<String, String>()
    var gradientStyle: GradientStyle? = null
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
        if (f == null) {
            f = this.font
        }
        //language=css
        var style = ""
        f.let {

            if (it.family.isNotEmpty()) {
                style += "font-family:${it.family.escapeXml()};"
            }
            if (it.size.isNotEmpty()) {
                style += "font-size:${it.size};"
            }
            if (it.color.isNotEmpty()) {
                style += "fill:${it.color};"
            }
            if (it.spacing.isNotEmpty()) {
                style += "letter-spacing:${it.spacing};"
            }
            if (it.bold) {
                style += "font-weight:bold;"
            }
            if (it.italic) {
                style += "font-style:italic;"
            }
            if (it.underline) {
                style += "text-decoration:underline;"
            }
            if (it.vertical) {
                style += "writing-mode:vertical-rl;"
            }
            style += when (it.case) {
                Case.CAPITALIZE -> "text-transform:capitalize;"
                Case.UPPER -> "text-transform:uppercase;"
                Case.LOWER -> "text-transform:lowercase;"
                Case.SMALLCAPS -> "font-variant:small-caps;"
                else -> {
                    ""
                }
            }
            var clazz = ""
            if(null == this.buttonStyleMap[style]) {
                clazz = "btnclass${(this.buttonStyleMap.size+1)}"
                this.buttonStyleMap[style] = clazz
            } else {
                clazz = this.buttonStyleMap[style]!!
            }
            style = "style=\"$style\""

            return clazz
        }
    }

    fun buildGradientStyle(button: Button) : String{
        val color = buttonColor(button)
        val m = gradientFromColor(color)
        return """
.${button.id}_cls { fill: url(#${button.id}); }
            
        """.trimIndent()
    }

    /*fun genOklchColor(mcolor: String): String {
        val color = RGB(mcolor)
        val lch = color.toOklch()
        return "oklch(${lch.l*100}% ${lch.c} ${lch.h})"
    }*/
    fun buildGradientDef(button: Button): String {
        val color = buttonColor(button)
        val m = gradientFromColor(color)
        return """
           <linearGradient id="${button.id}" x2="1" y2="1">
            <stop class="stop1" offset="0%" stop-color="${m["color1"]}"/>
            <stop class="stop2" offset="50%" stop-color="${m["color2"]}"/>
            <stop class="stop3" offset="100%" stop-color="${m["color3"]}"/>
            </linearGradient> 
        """
    }
    fun gradientFromColor(color: String): Map<String, String> {
        val decoded = Color.decode(color)
        val tinted1 = tint(decoded, 0.5)
        val tinted2 = tint(decoded, 0.25)
        return mapOf("color1" to tinted1, "color2" to tinted2, "color3" to color)
    }
    private fun shade(color: Color): String {
        val rs: Double = color.red * 0.50
        val gs = color.green * 0.50
        val bs = color.blue * 0.50
        return  "#${rs.toInt().toString(16)}${gs.toInt().toString(16)}${bs.toInt().toString(16)}"
    }
    private fun tint(color: Color, factor: Double): String {
        val rs = color.red + (factor * (255 - color.red))
        val gs = color.green + (factor * (255 - color.green))
        val bs = color.blue + (factor * (255 - color.blue))
        return  "#${rs.toInt().toString(16)}${gs.toInt().toString(16)}${bs.toInt().toString(16)}"
    }

    fun randomColor() {
        val color = (Math.random() * 16777215).toInt() or (0xFF shl 24)
    }
}



val SlimCardsTheme = theme {
    this typeIs "SLIM_CARD"
    groupBy = Grouping.ORDER
    groupOrder = GroupingOrder.ASCENDING
    columns = 4
}
val LargeCards = theme {
    this typeIs "LARGE_CARD"
}

enum class ButtonType {
    BUTTON, ROUND, LARGE_CARD, SLIM_CARD, RECTANGLE, PILL
}

enum class Grouping {
    ORDER, TYPE, TITLE, AUTHOR, DATE
}

enum class GroupingOrder {
    ASCENDING, DESCENDING
}


//dark themes
val BlueTheme = GradientStyle("Blues", color1 = "#447799", color2 = "#224488", color3 = "#112266", titleColor = "#ffa500", panelStroke = PanelStroke("#5D5B5B", 5))
val RedTheme = GradientStyle("Reds", color1 = "#ee8181", color2 = "#ef2e2e", color3 = "#e70505", titleColor = "#1A1212")
val GreenTheme = GradientStyle("Greens", color1 = "#50da77", color2 = "#1baf45", color3 = "#1baf45")
val PurpleTheme = GradientStyle("Purples", color1 = "#bb90f3", color2 = "#ad7cee", color3 = "#a770ef")
val LightPurpleTheme = GradientStyle("Purples", color1 = "#ae88f6 ", color2 = "#b796f6", color3 = "#c9a7f6", fontColor = "#000000")
val MagentaTheme = GradientStyle("Magentas", color1 = "#f373f3", color2 = "#e82ee8", color3 = "#FF00FF")
val DarkTheme = GradientStyle("Darks","#282525", color2 = "#252424", color3 = "#1e1f22", fontColor = "#cd9d72", titleColor="#cacaca", panelStroke = PanelStroke(color = "#cd9d72", 5))
val DarkTheme2 = GradientStyle("Darks2","#131938", color2 = "#060b26", color3 = "#01061d", fontColor = "#888171", titleColor="#d9d2c3", PanelStroke(color = "#cd9d72", 5))
//light themes
val LightGreyTheme = GradientStyle("LightGreys", color1 = "#c8c7cb", color2 = "#cdcdce", color3 = "#ebebec", fontColor = "#000000", panelStroke =  PanelStroke(color = "#cd9d72", 5))
val OrangeTheme = GradientStyle("Oranges", color1 = "#f8c567", color2 = "#faac1d", color3 = "#ffa500", "#000000")
class GradientStyle(val gradientId: String, val color1: String, val color2: String, val color3: String,
                    val fontColor: String = "white",
                    val titleColor: String = "whitesmoke",
                    val panelStroke: PanelStroke = PanelStroke()) {
    var style = """

    .${gradientId.lowercase()} { fill: url(#$gradientId); }
    #$gradientId .stop1 {stop-color: $color1;}
    #$gradientId .stop2 {stop-color: $color2;}
    #$gradientId .stop3 {stop-color: $color3;}

    """.trimIndent()
    fun gradientIdToXml() = """
        <linearGradient id="$gradientId">
            <stop class="stop1" offset="0%" />
            <stop class="stop2" offset="50%" />
            <stop class="stop3" offset="100%" />
        </linearGradient>
    """.trimIndent()
}

class PanelStroke(val color: String = "black", val width: Int = 3)
fun theme(theme: Theme.() -> Unit): Theme {
    return Theme().apply(theme).validate()
}
