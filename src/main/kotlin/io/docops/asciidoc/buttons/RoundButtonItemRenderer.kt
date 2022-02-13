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

class RoundButtonItemRenderer : ButtonMaker() {
    override fun makeButtons(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        val sb = StringBuilder(makeSvgHead(buttons = buttons, heightFactor = 60, defaultHeight = 75, widthFactor = 75, theme = theme))
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

    private fun drawButtonRow(rowCount: Int, buttons: MutableList<Button>, theme: Theme): String {
        val sb = StringBuilder("<g>")
        var recXpos = 35
        var yPos = 30
        if(rowCount>0) {
            yPos = rowCount * 75 + 30
        }
        var textXPos = 35
        buttons.forEachIndexed { index, button ->
            if(index > 0) {
                recXpos += 75
                textXPos += 75
            }
            var win = "_blank"
            if(!theme.newWin) {
                win = "_top"
            }
            if(theme.isPDF) {
                sb.append(
                    """
                   <use x="$recXpos" y="$yPos" xlink:href="#myCircle" fill="${theme.buttonColor(button)}">
                        <title class="description">${button.description.escapeXml()}</title>
                   </use>   
                """.trimIndent()
                )
                sb.append(
                    """
                <text x="$textXPos" y="${yPos + 5}" text-anchor="middle" class="label" fill="${theme.buttonTextColor(button)}">${button.title.escapeXml()}</text>
            """.trimIndent()
                )
            } else {
                sb.append(
                    """
                    <a xlink:href="${button.link}" target="$win">
                   <use x="$recXpos" y="$yPos" xlink:href="#myCircle" fill="${theme.buttonColor(button)}">
                        <title class="description">${button.description.escapeXml()}</title>
                   </use>
                   </a>
                """.trimIndent()
                )
                sb.append(
                    """
                <text x="$textXPos" y="${yPos + 5}" text-anchor="middle" ><a xlink:href="${button.link}" target="$win" style="fill: ${
                        theme.buttonTextColor(
                            button
                        )
                    }" class="label">${button.title.escapeXml()}</a></text>
            """.trimIndent()
                )
            }
        }
        sb.append("</g>")
        return sb.toString()
    }

    private fun makeStyles(): String {
        return """
        <style>
        circle.card {
            pointer-events: bounding-box;
            opacity: 1;
        }
        circle.card:hover {
            opacity: 0.6;
        }
        .subtitle {
            font-family: "Noto Sans",sans-serif;
            font-weight: normal;
            font-size: 10px;
        }
        rect.legend {
            pointer-events: bounding-box;
            opacity: 1;
        }

        rect.legend:hover {
            opacity: 0.6;
        }
        .label {
            font-family: "Noto Sans",sans-serif;
            font-size: 10px;
        }
    </style>"""
    }


}