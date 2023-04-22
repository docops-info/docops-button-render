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

package io.docops.asciidoc.stackbar

import io.docops.asciidoc.buttons.dsl.panels
import io.docops.asciidoc.buttons.generateRectPathData
import io.docops.asciidoc.buttons.theme.Theme
import io.docops.asciidoc.buttons.theme.theme
import io.docops.asciidoc.stackbar.model.StackModel
import io.docops.asciidoc.utils.addLinebreaks
import kotlin.math.roundToInt

class StackedBarMaker(val pdf: Boolean = false) {
    private var colorMap = listOf(
        "#093145", "#107896", "#829356", "#0C374D", "#1287A8",
        "#93A661", "#C2571A", "#9A2617", "#D3B53D", "#DA621E", "#AD2A1A",
        "#EBC944", "#F26D21", "#C02F1D", "#0D3D56", "#1496BB", "#A3B86C", "#3C6478",
        "#43ABC9", "#B5C689", "#BCA136", "#EFD469", "#F58B4C", "#CD594A"
    )

    var colors = mutableListOf<String>()
    var colorRange = mapOf(
        1 to listOf("#deaddf","#e8e08e","#e7d2ae","#97dfd2","#9efdc1","#f3e76f","#a7cbda","#ef8ce3","#6ed7ed","#d9b8a2","#e9ce86","#dac588","#fae587","#56f1e2","#8fbeef","#d8b5e3","#cba2f8","#cf7bf3","#ead471","#d3e58f"),
        2 to listOf("#b9deae","#c8bbe5","#8baeea","#65cef4","#d7f640","#ddfc65","#ece2a7","#f1a9b2","#abc2f0","#ed9ecb","#f588d0","#f1d692","#ec8af0","#fc64f3","#7ecbe6","#78e9c9","#61f3fc","#a197e7","#a9dcbd","#a7ef7d"),
        3 to listOf("#ecc4b9","#e2c5c3","#e69cf0","#eab2bc","#b7b1e0","#cff24e","#cfe8c1","#8cd6f4","#e9aecb","#a7d3be","#ed8bc9","#69d5eb","#f3a0c4","#f4c99f","#96b6fd","#f356ec","#aec0ec","#aac7e5","#9291fa","#aff8b5"),
        4 to listOf("#efd17b","#7fd4d7","#dd9ad6","#fbdb80","#e2d1ad","#e0e878","#e7b2c5","#e579cb","#e9b392","#e1b0ce","#c5e885","#48d6f6","#e0b4e3","#bcfc7a","#db78dc","#b6f3c3","#d397c7","#f8a9aa","#72a7fb","#81cdd8"),
    )


    fun makeStackedBar(stackModels: List<StackModel>, title: String, darkMode: Boolean = false): String {
        val i = (1..4).random()
        colors = colorRange[i]!!.toMutableList()
        val barWidth = 40
        val sb = StringBuilder()
        val width = 800
        sb.append(head(height = 460, width = width))
        sb.append(defs(stackModels))
        sb.append(style(darkMode))
        if (!pdf) {
            sb.append(script())
        }
        val norm = normalize(stackModels)
        var totalHeight = 20.0
        val x = width * 0.10

        val outerBoxD= generateRectPathData(800f, height = 460f, 20f,20f,20f,20f)
        // language=svg
        sb.append(
            """<g transform="translate(0,0)"><path d="$outerBoxD" fill="url(#outerBox)" filter="url(#dropShadow)"/></g>
               <text x="${width / 2}" y="24" text-anchor="middle" class="title">$title</text>
            """.trimIndent()
        )
        stackModels.forEachIndexed { index, stackModel ->
            val height = 400 * norm[index]

            val nextHalf = height / 2 + totalHeight
            val pathd= generateRectPathData(barWidth.toFloat(), height = height.toFloat(), 2f,2f,2f,2f)
            // language=svg
            sb.append("""<g transform="translate($x,$totalHeight)"><path d="$pathd" onmouseover="show('rect-$index');" onmouseout="hide('rect-$index');" filter="url(#dropShadow)" fill="url(#grad-$index)"/></g>""".trimIndent())
            // language=svg
            sb.append("""<text x="${x + 20}" y="$nextHalf" text-anchor="middle" class="label" onmouseover="show('rect-$index');" onmouseout="hide('rect-$index');">${(norm[index] * 100).roundToInt()}%</text>""")
            // language=svg
            sb.append("""<line x1="${x + 40}" y1="$nextHalf" x2="${x + 90}" y2="$nextHalf" style="stroke-width: 2; stroke: ${colors[index % colors.size]};" marker-end="url(#arrowhead)"/>""")
            val lines = addLinebreaks(stackModel.description, 50)
            // language=svg
            sb.append(
                """
                <text x="${x + 102}" y="$nextHalf" class="desc">"""
            )
            var start = 3
            if (lines.size > 1) {
                start = -2
            }
            lines.forEachIndexed { i, line ->
                if (i > 0) {
                    start = 12
                }
                // language=svg
                sb.append("""<tspan x ="${x + 102}" dy = "$start">${line}</tspan>""")
            }
            sb.append("</text>")

            if (!pdf) {
                val desc = addLinebreaks(stackModel.fullDescription, 50)
                // language=svg
                sb.append("""<text x="400" y="80" visibility="hidden" id="rect-$index" class="desc">""")

                desc.forEachIndexed { i, d ->
                    start = if (i == 0) {
                        0
                    } else {
                        12
                    }
                    // language=svg
                    sb.append("""<tspan x="410" dy="$start" fill="#000">${d}</tspan>""")
                }
                sb.append("""</text>""")
            }
            totalHeight += height
        }
        sb.append(end())
        return sb.toString()
    }

    private fun normalize(stackModels: List<StackModel>): MutableList<Double> {
        val values = mutableListOf<Double>()
        val sum = stackModels.sumOf { it.value }
        stackModels.forEach {
            values.add(it.value / sum)
        }
        return values
    }

    fun toLine(filename: String, list: List<StackModel>): MutableList<String> {
        val norm = normalize(list)
        val lines = mutableListOf<String>()

        lines.add(".$filename")
        // language=Asciidoc
        lines.add("""[cols="15%,15%,20%, 50%", options=header]""")
        // language=Asciidoc
        lines.add("|===")
        // language=Asciidoc
        lines.add("|Percent |Value |Label |Description")
        list.forEachIndexed { index, stackModel ->
            lines.add("|${(norm[index] * 100).roundToInt()} % |${stackModel.value} |${stackModel.description} |${stackModel.fullDescription}")
        }
        lines.add("|===")
        return lines
    }
    private fun head(height: Int = 460, width: Int = 970): String {
        // language=svg
        return """
            <?xml version='1.0'?>
            <svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height"
            viewBox="0 0 ${width + 20} ${height + 20}">
        """.trimIndent()
    }

    private fun end(): String {
        return "</svg>"
    }

    private fun style(darkMode: Boolean): String {
        var fill = "#000000"
        if(darkMode) {
            fill = "#ffffff"
        }
        // language=html
        return """
    <style>
        .label {font-size: 10px;font-family: ".AppleSystemUIFont","Noto Sans",sans-serif;font-weight: bold;fill: $fill}
        .desc { font-size: 12px; font-family: ".AppleSystemUIFont","Noto Sans",sans-serif; font-weight: bold; fill: $fill}
        .title { font-size: 18px; font-family: ".AppleSystemUIFont","Noto Sans",sans-serif; font-weight: normal; fill: $fill}
        .cool { fill: teal; stroke: teal; }
        rect.outerbox { filter: drop-shadow(3px 5px 2px rgb(0 0 0 / 0.5)); }
        rect.card { pointer-events: bounding-box; opacity: 1; }
        rect.card:hover { opacity: 0.6; }
        path:hover {
            fill: none;
            stroke: gold;
            pointer-events: all;
            cursor: pointer;
        }
    </style>
        """.trimIndent()
    }

    private fun defs(stackModels: List<StackModel>): String {
        val sb = StringBuilder()
        val panels  = panels {theme {
            colorMap {
                color("#D10093")
                color("#C98C53")
                color("#E4D17E")
                color("#EAC1E6")
                color("#B46FDE")
                color("#A9FBE6")
                color("#F62257")
                color("#4AC9ED")
                color("#91DFF4")
                color("#74AD28")
                color("#9EFF6E")
                color("#AF0AEC")
                color("#851AF2")
                color("#E23614")
                color("#1F6627")
                color("#84D962")
                color("#A93286")
                color("#9E0AE5")
                color("#2FA1F1")
                color("#C5893C")
            }

        }
            button {  }
        }
        val theme =  Theme()


        stackModels.forEachIndexed { index, stackModel ->
            val color = panels.buttonTheme.colorMap.colors[index % colors.size]
            val m = theme.gradientFromColor(color)
            sb.append("""
           <linearGradient id="grad-$index" x2="1" y2="1">
            <stop class="stop1" offset="0%" stop-color="${m["color1"]}"/>
            <stop class="stop2" offset="50%" stop-color="${m["color2"]}"/>
            <stop class="stop3" offset="100%" stop-color="${m["color3"]}"/>
            </linearGradient>""")
        }
        val m = theme.gradientFromColor("#eeeeee")
        sb.append("""
            <linearGradient id="outerBox" x2="1" y2="1">
            <stop class="stop1" offset="0%" stop-color="${m["color1"]}"/>
            <stop class="stop2" offset="50%" stop-color="${m["color2"]}"/>
            <stop class="stop3" offset="100%" stop-color="${m["color3"]}"/>
            </linearGradient>""${'"'})
        """.trimIndent())
        // language=svg
        return """
    <defs>
        <marker id="arrow" markerWidth="10" markerHeight="7"
                refX="0" refY="3.5" orient="auto">
            <polygon points="0 0, 10 3.5, 0 7" fill="#00ee11" opacity="0.1"/>
        </marker>
        <marker id="arrowhead" viewBox="0 -5 10 10" refX="5" refY="0" markerWidth="4" markerHeight="4" orient="auto">
            <path class="cool" d="M0,-5L10,0L0,5"/>
        </marker>
        <filter id="dropShadow" height="112%">
            <feGaussianBlur in="SourceAlpha" stdDeviation="1"/>
            <feOffset dx="1" dy="3" result="offsetblur"/>
            <feComponentTransfer>
                <feFuncA type="linear" slope="0.2"/>
            </feComponentTransfer>
            <feMerge>
                <feMergeNode/>
                <feMergeNode in="SourceGraphic"/>
            </feMerge>
        </filter>
        $sb
    </defs>
        """.trimIndent()
    }

    private fun script(): String {
        // language=html
        return """
    <script>
        var show = function(id) {
            var elem = document.getElementById(id)
            if(elem) {
                elem.setAttribute("visibility", "visible");
            }
        };
        var hide = function (id) {
            var elem = document.getElementById(id)
            if(elem) {
                elem.setAttribute("visibility", "hidden");
            }
        };
    </script>
        """.trimIndent()
    }
}

