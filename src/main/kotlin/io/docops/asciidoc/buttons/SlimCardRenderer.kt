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
import io.docops.asciidoc.buttons.theme.PanelStroke
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
        var style = ""
        theme.gradientStyle?.let {
            style = it.gradientId.lowercase()
        }
        val sb = StringBuilder("<g>")
        var recXpos = 10
        var yPos = 10
        var dateXpos = 95
        if(rowCount>0) {
            yPos = rowCount * 160 + 10
        }
        row.forEachIndexed { index, button ->
            var btnTextColor = " ${theme.buttonTextColor(button)}"
            theme.gradientStyle?.let { btnTextColor="" }
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
                   <use x="$recXpos" y="$yPos" class="card $style shape" fill="${theme.buttonColor(button)}" xlink:href="#mySlimRect">
                       <title class="description">${button.description.escapeXml()}</title>
                   </use>
                """.trimIndent()
                )
            } else {
                // language=svg
                sb.append(
                    """
                    <a xlink:href="${button.link}" target="$win">
                   <use x="$recXpos" y="$yPos" class="card $style shape" fill="${theme.buttonColor(button)}" aria-hidden="true" focusable="false" xlink:href="#mySlimRect">
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
                authors.append("""<tspan x="${recXpos+4}" dy="16" class="author $btnTextColor">${it.escapeXml()}</tspan>""")
            }
            val str = addLinebreaks(button.type.escapeXml(),25)
            var head = ""
            var c = 0
            var downBy = 16
            str.forEach {
                // language=svg
                head += """<tspan x="${recXpos+4}" dy="$downBy" class="category$btnTextColor">${it.trim()}</tspan>"""
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
                title += """<tspan x="${recXpos+4}" dy="$downBy" class="title $btnTextColor">${it.trim()}</tspan>"""
                c++
                if(c>0) {
                    downBy = 16
                    dy -= 16
                }
            }
            val descStr = addLinebreaks(button.description.escapeXml(), 25 )
            var desc = ""
            c = 0
            downBy = 16
            descStr.forEach {
                if(c < 4) {
                    title += """<tspan x="${recXpos + 4}" dy="$downBy" class="description $btnTextColor">${it.trim()}</tspan>"""
                    c++
                    if (c > 0) {
                        downBy = 16
                        dy -= 16
                    }
                }
            }
            // language=svg
            sb.append("""
                <text x="${recXpos+2}" y="${yPos+20}">
                    $title
                    $head
                    $authors
                    <tspan x="${recXpos+2}" dy="16" class="date">${button.date.escapeXml()}</tspan>
                </text>
            """.trimIndent())
        }
        sb.append("</g>")
        return sb.toString()
    }



    private fun makeStyles(buttonList: MutableList<MutableList<Button>>, theme: Theme): String {
        var stroke = PanelStroke()
        var fontColor = "white"
        var titleColor = "whitesmoke"
        var style = ""
        var strokeWidth = 3
        theme.gradientStyle?.let {
            style = it.style
            fontColor = it.fontColor
            stroke = it.panelStroke
            titleColor = it.titleColor
            strokeWidth= it.panelStroke.width
        }
        buttonList.forEach { buttons ->
            buttons.forEach {
                    item -> theme.buttonTextColor(item)
            }
        }
        // language=html
        var str =  """
        <style>
        use { filter: drop-shadow(3px 5px 2px rgb(0 0 0 / 0.${theme.dropShadow}));  pointer-events: bounding-box; opacity: 1; }
        use:hover { opacity: 0.9; -webkit-animation: 0.5s draw linear forwards; animation: 0.5s draw linear forwards; }
        .lineHead { fill: $fontColor; font-family: Helvetica, Arial, sans-serif; font-weight: bold; font-size: 9pt; }
        .description { fill: $fontColor; font-family: Helvetica, Arial, sans-serif; font-size: 8pt; }
        .category { fill: $fontColor; font-family: Helvetica, Arial, sans-serif; font-size: 8pt; font-weight: bold; font-style: italic}
        .title { fill: $titleColor; font-family: Helvetica, Arial, sans-serif; font-weight: bold; font-style: normal; font-size: 9pt; }
        .author { font-family: Helvetica, Arial, sans-serif; font-weight: normal; font-size: 8pt; fill: $fontColor; }
        .legendText { font-family: Helvetica, Arial, sans-serif; font-weight: normal; font-size: 9pt; }
        .date { fill: $fontColor; font-family: Helvetica, Arial, sans-serif; font-weight: normal; font-size: 10px; }

        @keyframes draw { 
            0% { stroke-dasharray: 140 540; stroke-dashoffset: -474; stroke-width:3px; } 
            100%{ stroke-dasharray: 760; stroke-dashoffset:0; stroke-width:${stroke.width}px; } 
        }
        
        .shape{ stroke:${stroke.color};}
        
        $style
    """.trimIndent()
        theme.buttonStyleMap.forEach { (t, u) ->
            str += ".$u {$t}\n"
        }
        str += """</style>"""
        return str
    }
}