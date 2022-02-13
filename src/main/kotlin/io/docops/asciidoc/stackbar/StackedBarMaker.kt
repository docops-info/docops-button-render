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

import io.docops.asciidoc.stackbar.model.StackModel
import io.docops.asciidoc.utils.addLinebreaks
import java.io.File
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
        1 to listOf("#093145", "#107896", "#829356", "#0C374D", "#1287A8"),
        2 to listOf("#93A661", "#C2571A", "#9A2617", "#D3B53D", "#DA621E", "#AD2A1A"),
        3 to listOf("#EBC944", "#F26D21", "#C02F1D", "#0D3D56", "#1496BB", "#A3B86C", "#3C6478"),
        4 to listOf("#43ABC9", "#B5C689", "#BCA136", "#EFD469", "#F58B4C", "#CD594A")
    )


    fun makeStackedBar(stackModels: List<StackModel>, title: String): String {
        val i = (1..4).random()
        colors = colorRange[i]!!.toMutableList()
        val barWidth = 40
        val sb = StringBuilder()
        val width = 970
        sb.append(head(height = 460, width = width))
        sb.append(defs())
        sb.append(style())
        if (!pdf) {
            sb.append(script())
        }
        val norm = normalize(stackModels)
        var totalHeight = 20.0
        val x = width * 0.10
        sb.append(
            """
               <rect x="0" y="0" rx="5" ry="5" fill="#1E3B45" height="100%" width="100%"/>
               <text x="${width / 2}" y="24" text-anchor="middle" class="title" stroke="#ffffff" stroke-width="1px">$title</text>
            """.trimIndent()
        )
        stackModels.forEachIndexed { index, stackModel ->
            val height = 400 * norm[index]

            val nextHalf = height / 2 + totalHeight
            sb.append("""<rect x="$x" y="$totalHeight" width="$barWidth" height="$height" fill="${colors[index % colors.size]}" class="card" onmouseover="show('rect-$index');" onmouseout="hide('rect-$index');"/>""")
            sb.append("""<text x="${x + 20}" y="$nextHalf" text-anchor="middle" class="label" onmouseover="show('rect-$index');" onmouseout="hide('rect-$index');">${(norm[index] * 100).roundToInt()}%</text>""")
            sb.append("""<line x1="${x + 40}" y1="$nextHalf" x2="${x + 90}" y2="$nextHalf" style="stroke-width: 2; stroke: ${colors[index % colors.size]};" marker-end="url(#arrowhead)"/>""")
            val lines = addLinebreaks(stackModel.description, 120)
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
                sb.append("""<tspan x ="${x + 102}" dy = "$start">${line}</tspan>""")
            }
            sb.append("</text>")

            if (!pdf) {
                val desc = addLinebreaks(stackModel.fullDescription, 100)
                sb.append("""<text x="30" y="440" visibility="hidden" id="rect-$index" class="desc">""")

                desc.forEachIndexed { i, d ->
                    if (i == 0) {
                        start = 0
                    } else {
                        start = 12
                    }
                    sb.append("""<tspan x="$x" dy="$start" fill="#ffffff">${d}</tspan>""")
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
        lines.add("""[cols="15%,15%,20%, 50%", options=header]""")
        lines.add("|===")
        lines.add("|Percent |Value |Label |Description")
        list.forEachIndexed { index, stackModel ->
            lines.add("|${(norm[index] * 100).roundToInt()} % |${stackModel.value} |${stackModel.description} |${stackModel.fullDescription}")
        }
        lines.add("|===")
        return lines
    }
    private fun head(height: Int = 460, width: Int = 970): String {
        return """
            <?xml version='1.0'?>
            <svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height"
            viewBox="0 0 ${width + 20} ${height + 20}">
        """.trimIndent()
    }

    private fun end(): String {
        return "</svg>"
    }

    private fun style(): String {
        return """
    <style>
        .label {
        font-size: 10px;
        font-family: "Noto Sans",sans-serif;
        font-weight: bold;
        fill: #fff;
    }

    .desc {
        font-size: 9px;
        font-family: "Noto Sans",sans-serif;
        font-weight: bold;
        fill: #fff;
    }
        .title {
            font-size: 18px;
            font-family: "Noto Sans",sans-serif;
            font-weight: normal;
            fill: #fff;
        }
        .cool {
            fill: teal;
            stroke: teal;
        }
        rect.card {
            pointer-events: bounding-box;
            opacity: 1;
        }

        rect.card:hover {
            opacity: 0.6;
        }
    </style>
        """.trimIndent()
    }

    private fun defs(): String {
        return """
    <defs>
        <marker id="arrow" markerWidth="10" markerHeight="7"
                refX="0" refY="3.5" orient="auto">
            <polygon points="0 0, 10 3.5, 0 7" fill="#00ee11" opacity="0.1"/>
        </marker>
        <marker id="arrowhead" viewBox="0 -5 10 10" refX="5" refY="0" markerWidth="4" markerHeight="4" orient="auto">
            <path class="cool" d="M0,-5L10,0L0,5"/>
        </marker>
    </defs>
        """.trimIndent()
    }

    private fun script(): String {
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

