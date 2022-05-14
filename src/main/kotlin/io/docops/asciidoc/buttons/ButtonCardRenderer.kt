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
import io.docops.asciidoc.buttons.theme.Theme
import io.docops.asciidoc.utils.escapeXml

class ButtonCardRenderer : ButtonMaker() {
    override fun makeButtons(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        val widthFactor = 305
        val sb = StringBuilder(makeSvgHead(buttons = buttons, heightFactor = 40, defaultHeight = 60, widthFactor = widthFactor, theme = theme))
        sb.append(makeDefs(theme))
        sb.append(makeStyles())
        sb.append(drawButtons(buttons,theme, widthFactor))
        if(theme.legendOn) {
            sb.append(drawLegend(types))
        }
        sb.append(makeSvgEnd())
        return sb.toString()
    }

    private fun drawButtons(buttonList: MutableList<MutableList<Button>>, theme: Theme, widthFactor: Int): String {
        val sb = StringBuilder()
        buttonList.forEachIndexed { index, buttons ->
            sb.append(drawButtonRow(index, buttons, theme, widthFactor))
        }
        return sb.toString()
    }

    private fun drawButtonRow(rowCount: Int, buttons: MutableList<Button>, theme: Theme, widthFactor: Int): String {
        val sb = StringBuilder("<g>")
        var recXpos = 10
        var yPos = 10
        if(rowCount>0) {
            yPos = rowCount * 40 + 10
        }
        var textXPos = recXpos + (widthFactor/2)
        buttons.forEachIndexed { index, button ->
            if(index > 0) {
                recXpos += 310
                textXPos = recXpos + (widthFactor/2)
            }
            var win = "_blank"
            if(!theme.newWin) {
                win = "_top"
            }
            if(theme.isPDF) {
                // language=svg
                sb.append(
                    """
                   <use x="$recXpos" y="$yPos" class="card" fill="${theme.buttonColor(button)}" xlink:href="#myPanel">
                        <title class="description">${button.description.escapeXml()}</title>
                   </use>      
                """.trimIndent())
                // language=svg
                sb.append("""
                <text x="$textXPos" y="${yPos+20}" class="label" text-anchor="middle" style="fill: ${theme.buttonTextColor(button)}">${button.title.escapeXml()}</text>
            """.trimIndent())
            } else {
                // language=svg
                sb.append(
                    """
                    <a xlink:href="${button.link}" target="$win">
                   <use x="$recXpos" y="$yPos" class="card" fill="${theme.buttonColor(button)}" xlink:href="#myPanel">
                        <title class="description">${button.description.escapeXml()}</title>
                   </use> 
                   </a>
                """.trimIndent()
                )
                // language=svg
                sb.append("""
                <text x="$textXPos" y="${yPos+20}" text-anchor="middle" class="label"><a xlink:href="${button.link}" target="$win" style="fill: ${theme.buttonTextColor(button)}; text-decoration: underline;">${button.title.escapeXml()}</a></text>
            """.trimIndent())
            }

        }
        sb.append("</g>")
        return sb.toString()
    }

    private fun makeStyles(): String {
        // language=svg
        return """
        <style>
        rect.card {
            pointer-events: bounding-box;
            opacity: 1;
        }
        rect.card:hover {
            opacity: 0.6;
        }
        use.card {
            pointer-events: bounding-box;
            opacity: 1;
        }
        use.card:hover {
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
        }
    </style>
    """
    }


}