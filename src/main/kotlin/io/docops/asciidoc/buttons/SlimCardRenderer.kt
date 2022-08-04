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
import io.docops.asciidoc.utils.addLinebreaks
import io.docops.asciidoc.utils.escapeXml


class SlimCardRenderer : ButtonMaker() {
    override  fun makeButtons(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        val sb = StringBuilder(makeSvgHead(buttons, 170, 250, 155, theme))
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
            val win = if(!theme.newWin) {
                "_top"
            } else {
                "_blank"
            }
            if(theme.isPDF) {
                // language=svg
                sb.append(
                    """
                   <use x="$recXpos" y="$yPos" class="card" fill="${theme.buttonColor(button)}" xlink:href="#mySlimRect"> 
                       <title class="description">${button.description.escapeXml()}</title>
                   </use>     
                """.trimIndent()
                )
            } else {
                // language=svg
                sb.append(
                    """
                    <a xlink:href="${button.link}" target="$win">
                   <use x="$recXpos" y="$yPos" class="card" fill="${theme.buttonColor(button)}" xlink:href="#mySlimRect"> 
                       <title class="description">${button.description.escapeXml()}</title>
                   </use>    
                   </a>
                """.trimIndent()
                )
            }
            val authors = StringBuilder()
            var dy: Int = if(button.authors.size == 0) {
                80
            } else {
                (80 - button.authors.size * 16) + 16
            }
            button.authors.forEach {
                authors.append("""<tspan x="${recXpos+4}" dy="16" class="author">${it.escapeXml()}</tspan>""")
            }
            val str = addLinebreaks(button.type.escapeXml(),25)
            var head = ""
            var c = 0
            var downBy = 16
            str.forEach {
                // language=svg
                head += """<tspan x="${recXpos+4}" dy="$downBy" class="category">${it.trim()}</tspan>"""
                c++
                if(c>0) {
                    downBy = 16
                    dy -= 16
                }
            }
            val titleStr = addLinebreaks(button.title.escapeXml(),25)
            var title = ""
            c = 0
            downBy = 0
            titleStr.forEach {
                title += """<tspan x="${recXpos+4}" dy="$downBy" class="title ${theme.buttonTextColor(button)}">${it.trim()}</tspan>"""
                c++
                if(c>0) {
                    downBy = 16
                    dy -= 16
                }
            }
            // language=svg
            sb.append("""
                <text x="${recXpos+2}" y="${yPos+20}">
                    $title
                    $head
                    $authors
                    <tspan x="$dateXpos" dy="16" class="date">${button.date.escapeXml()}</tspan>
                </text>
            """.trimIndent())
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
        // language=css
        var str =  """
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
        
        .card {
            filter: drop-shadow(3px 5px 2px rgb(0 0 0 / 0.${theme.dropShadow}));
        }

        .lineHead {
            fill: white;
            font-family: Helvetica, Arial, sans-serif;
            font-weight: bold;
            font-size: 9pt;
        }

        .category {
            fill: white;
            font-family: Helvetica, Arial, sans-serif;
            font-weight: bold;
            font-style: italic;
            font-size: 8pt;
        }
        .title {
            fill: white;
            font-family: Helvetica, Arial, sans-serif;
            font-weight: bold;
            font-style: normal;
            font-size: 9pt;
        }


        .author {
            font-family: Helvetica, Arial, sans-serif;
            font-weight: normal;
            font-size: 8pt;
            fill: darkslategrey;
        }
        .legendText {
            font-family: Helvetica, Arial, sans-serif;
            font-weight: normal;
            font-size: 9pt;
        }

        .date {
            fill: white;
            font-family: Helvetica, Arial, sans-serif;
            font-weight: normal;
            font-size: 10px;
        }
        #tooltip {
            background: cornsilk;
            border: 1px solid black;
            border-radius: 5px;
            padding: 5px;
        }
        
    """.trimIndent()
        theme.buttonStyleMap.forEach { (t, u) ->
            str += ".$u {$t}\n"
        }
        str += """</style>"""
        return str
    }
}