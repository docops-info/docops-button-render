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
import io.docops.asciidoc.utils.makeLines

class RoundButtonItemRenderer : ButtonMaker() {
    override fun makeButtons(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        val sb = StringBuilder(makeSvgHead(buttons = buttons, heightFactor = 155, defaultHeight = 130, widthFactor = 140, theme = theme))
        sb.append(makeDefs(theme))
        sb.append(makeStyles(buttons, theme))
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
        var recXpos = 80
        var yPos = 65
        if(rowCount>0) {
            yPos = rowCount * 130 + 65
        }
        var textXPos = 55
        buttons.forEachIndexed { index, button ->
            if(index > 0) {
                recXpos += 125
                textXPos += 115
            }
            val win = if(!theme.newWin) {
                "_top"
            } else {
                "_blank"
            }
            if(theme.isPDF) {
                // language=svg
                sb.append(
                    """
                   <use x="$recXpos" y="$yPos" xlink:href="#myCircle" fill="${theme.buttonColor(button)}">
                        <title class="description">${button.description.escapeXml()}</title>
                   </use>
                   <text class="category" visibility="hidden">${button.type.escapeXml()}</text>
                   <text class="author" visibility="hidden">${button.authors.firstOrNull()}</text>
                   <text class="date" visibility="hidden">${button.date.escapeXml()}</text>
                """.trimIndent()
                )
                sb.append(
                    """
                <text x="$recXpos" y="${yPos + 5}" text-anchor="middle" class="label ${theme.buttonTextColor(button)}">""")
                val lines = button.title.makeLines()
                var dy = 0
                if(lines.size>2) {
                    dy = -10 * (lines.size - 2)
                }
                lines.forEachIndexed {i, str ->
                    if(i>0) {
                        dy = 12
                    }
                    sb.append(
                        """<tspan class="title" x="$recXpos" dy="$dy">${str.escapeXml()}</tspan>"""
                    )
                }
                sb.append("""</text>""")
            } else {
                // language=svg
                sb.append(
                   """
                   <a xlink:href="${button.link}" target="$win">
                       <use x="$recXpos" y="$yPos" xlink:href="#myCircle" class="card" fill="${theme.buttonColor(button)}">
                           <title class="description">${button.description.escapeXml()}</title>
                       </use>
                       <text class="category" visibility="hidden">${button.type.escapeXml()}</text>
                       <text class="author" visibility="hidden">${button.authors.firstOrNull()}</text>
                       <text class="date" visibility="hidden">${button.date.escapeXml()}</text>
                   </a>
                """.trimIndent()
                )
                // language=svg
                sb.append(
                    """ 
                <a xlink:href="${button.link}" target="$win">
                    <text x="$recXpos" y="${yPos + 5}" text-anchor="middle" >""")
                val lines = button.title.makeLines()
                var dy = 0
                if(lines.size>2) {
                    dy = -10 * (lines.size - 2)
                }
                lines.forEachIndexed {i, str ->
                    if(i>0) {
                        dy = 12
                    }
                    // language=svg
                    sb.append(
                        """<tspan class="title ${theme.buttonTextColor(button)}" x="$recXpos" dy="$dy">${str.escapeXml()}</tspan>"""
                        )
                }
                sb.append("""
                    </text>
                    </a>
            """.trimIndent()
                )
            }
        }
        sb.append("</g>")
        return sb.toString()
    }

    private fun makeStyles(buttonList: MutableList<MutableList<Button>>, theme: Theme): String {
        buttonList.forEach { buttons ->
            buttons.forEach {
                    item -> theme.buttonTextColor(item)
            }
        }
        //language=html
        var str =  """
        <style>
        circle.card {
            filter: drop-shadow(3px 5px 2px rgb(0 0 0 / 0.${theme.dropShadow})); 
            pointer-events: bounding-box;
            opacity: 1;
        }
        circle.card:hover {
            opacity: 0.6;
        }
        use.card {
            filter: drop-shadow(3px 5px 2px rgb(0 0 0 / 0.${theme.dropShadow}));
            pointer-events: bounding-box;
            opacity: 1;
        }
        use.card:hover {
            opacity: 0.6;
        }
        .subtitle {
            font-family: Helvetica, Arial, sans-serif;
            font-weight: normal;
            font-size: 9pt;
        }
        rect.legend {
            pointer-events: bounding-box;
            opacity: 1;
        }

        rect.legend:hover {
            opacity: 0.6;
        }
        .label {
            font-family: Helvetica, Arial, sans-serif;
            font-size: 9pt;
        }
        .legendText {
            font-size: 9pt;
            font-family:  Helvetica, Arial, sans-serif;    
        }
        
    """.trimIndent()
        theme.buttonStyleMap.forEach { (t, u) ->
            str += ".$u {$t}\n"
        }
        str += """</style>"""
        return str
    }

}