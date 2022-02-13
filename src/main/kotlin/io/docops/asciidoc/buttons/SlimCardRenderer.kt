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

package io.docops.asciidoc.buttons

import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.buttons.models.escapeXml
import io.docops.asciidoc.buttons.theme.Theme


class SlimCardRenderer : ButtonMaker() {
    override  fun makeButtons(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        val sb = StringBuilder(makeSvgHead(buttons, 170, 250, 150, theme))
        sb.append(makeDefs(theme))
        sb.append(makeStyles())
        sb.append(drawButtons(buttons,theme))
        if(theme.legendOn) {
            sb.append(drawLegend(types))
        }
        sb.append(makeSvgEnd())
        return sb.toString()
    }

    private fun drawButtons(buttonList: MutableList<MutableList<Button>>, theme: Theme): String {
        val sb = StringBuilder()
        buttonList.forEachIndexed { index, buttons ->
            sb.append(drawButtonRow(index, buttons, theme))
        }
        return sb.toString()
    }
    private fun drawButtonRow(rowCount: Int, row : MutableList<Button>, theme: Theme): String {
        val sb = StringBuilder("<g>")
        var recXpos = 10
        var yPos = 10
        var dateXpos = 95
        if(rowCount>0) {
            yPos = rowCount * 160 + 10
        }
        row.forEachIndexed { index, button ->
            if(index > 0) {
                recXpos += 160
                dateXpos+= 160
            }
            var win = "_blank"
            if(!theme.newWin) {
                win = "_top"
            }
            if(theme.isPDF) {
                sb.append(
                    """
                   <use x="$recXpos" y="$yPos" fill="${theme.buttonColor(button)}" xlink:href="#mySlimRect"> 
                       <title class="description">${button.description.escapeXml()}</title>
                   </use>     
                """.trimIndent()
                )
            } else {
                sb.append(
                    """
                    <a xlink:href="${button.link}" target="$win">
                   <use x="$recXpos" y="$yPos" fill="${theme.buttonColor(button)}" xlink:href="#mySlimRect"> 
                       <title class="description">${button.description.escapeXml()}</title>
                   </use>    
                   </a>
                """.trimIndent()
                )
            }
            val authors = StringBuilder()
            val dy: Int = if(button.authors.size == 0) {
                80
            } else {
                (80 - button.authors.size * 16) + 16
            }
            button.authors.forEach {
                authors.append("""<tspan x="${recXpos+4}" dy="16" class="author">${it.escapeXml()}</tspan>""")
            }
            sb.append("""
                <text x="${recXpos+2}" y="${yPos+20}">
                    <tspan x="${recXpos+4}" dy="0" class="lineHead">${button.type.escapeXml()}</tspan>
                    <tspan x="${recXpos+4}" dy="16" class="subtitle">${button.title.escapeXml()}</tspan>
                    $authors
                    <tspan x="$dateXpos" dy="$dy" class="date">${button.date.escapeXml()}</tspan>
                </text>
            """.trimIndent())
        }
        sb.append("</g>")
        return sb.toString()
    }



    private fun makeStyles(): String {
        return """
            <style>
        rect.card {
            pointer-events: bounding-box;
            opacity: 1;
        }

        rect.card:hover {
            opacity: 0.6;
        }

        .card {
            filter: url(#dropShadow);
        }

        .lineHead {
            fill: white;
            font-family: "Noto Sans",sans-serif;
            font-weight: bold;
            font-size: 12px;
        }

        .subtitle {
            fill: white;
            font-family: "Noto Sans",sans-serif;
            font-weight: normal;
            font-size: 10px;
        }

        .author {
            fill: black;
            font-family: "Noto Sans",sans-serif;
            font-weight: normal;
            font-size: 10px;
        }

        .date {
            fill: white;
            font-family: "Noto Sans",sans-serif;
            font-weight: normal;
            font-size: 10px;
        }
        #tooltip {
            background: cornsilk;
            border: 1px solid black;
            border-radius: 5px;
            padding: 5px;
        }
    </style>
    """.trimIndent()
    }
}