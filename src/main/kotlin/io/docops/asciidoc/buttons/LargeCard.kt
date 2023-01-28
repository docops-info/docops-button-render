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
import java.util.*

class LargeCard : ButtonMaker() {
    override fun makeButtons(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        val sb = StringBuilder(
            makeSvgHead(
                buttons = buttons,
                heightFactor = 410,
                defaultHeight = 500,
                widthFactor = 310,
                theme = theme
            )
        )
        sb.append(makeDefs(theme))
        sb.append(makeStyles(buttons, theme))
        sb.append(drawButtons(buttons, theme))
        if (theme.legendOn) {
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

    private fun drawButtonRow(rowCount: Int, row: MutableList<Button>, theme: Theme): String {
        val sb = StringBuilder("<svg>")
        var recXpos = 10
        var yPos = 10
        var dateXpos = 95
        if (rowCount > 0) {
            yPos = rowCount * 410 + 10
        }
        row.forEachIndexed { index, button ->
            if (index > 0) {
                recXpos += 320
                dateXpos += 320
            }
            val win = if (!theme.newWin) {
                "_top"
            } else {
                "_blank"
            }
            val lines = addLinebreaks(button.description, 41)
            var imgOrRec = ""
            button.buttonImage?.let {
                imgOrRec = "data:${it.type};base64,${it.ref}"
            }
            if (button.buttonImage != null) {
                val img = button.buttonImage
                img?.let {
                    // language=svg
                    imgOrRec = """
                        <use x="$recXpos" y="$yPos" class="card" fill="${theme.buttonColor(button)}" xlink:href="#myLargerHeroRect"/>
                        <image x="$recXpos" y="$yPos" width="300" height="191" href="${img.ref}"/>""".trimIndent()
                }
            } else {
                // language=svg
                imgOrRec = """
                    <use x="$recXpos" y="$yPos" class="card" fill="${theme.buttonColor(button)}" xlink:href="#myLargerHeroRect"/>
                    """.trimIndent()

                if(button.line1 != null && button.line2 != null) {
                    imgOrRec += makeTwoTone(recXpos,yPos, button.line1, button.line2, theme.buttonColor(button), button, theme)
                }
            }
            if (theme.isPDF) {
                // language=svg
                sb.append(
                    """
                   <use x="$recXpos" y="$yPos" class="card" xlink:href="#myLargeRect"><title class="description">${button.description.escapeXml()}</title></use>     
                   $imgOrRec
                """.trimIndent()
                )
            } else {
                // language=svg
                sb.append(
                    """
                    <a xlink:href="${button.link}" target="$win">
                    <use x="$recXpos" y="$yPos" class="card" xlink:href="#myLargeRect"><title class="description">${button.description.escapeXml()}</title></use>    
                   $imgOrRec
                   </a>
                """.trimIndent()
                )
            }
            val authors = StringBuilder()
            // language=svg
            button.authors.forEach {
                authors.append("""<tspan x="${recXpos + 10}" dy="18" class="author">${it.escapeXml()}</tspan>""")
            }
            // language=svg

            if (theme.isPDF) {
                // language=svg
                sb.append("""
                        <tspan class="title" text-decoration="underline" fill="#335D79">${button.title.escapeXml()}</tspan>
                        <text x="${recXpos + 10}" y="${yPos + 220}" class="category">${button.type.escapeXml()}</text>
                        <text x="${recXpos + 10}" y="${yPos + 240}" class="longdesc">
                    """.trimIndent())
            } else {
                // language=svg
                sb.append("""
                        <text x="${recXpos + 10}" y="${yPos + 220}" class="link">
                            <a href="${button.link}" class="title ${theme.buttonTextColor(button)}">
                                ${button.title.escapeXml()}
                            </a>
                        </text>
                        <text x="${recXpos + 10}" y="${yPos + 238}" class="category ${theme.buttonTextColor(button)}">${button.type.escapeXml()}</text>
                        <text x="${recXpos + 10}" y="${yPos + 240}" class="longdesc">
                    """.trimIndent())
            }
            // language=svg
            sb.append(
                """
                    $authors
                    <tspan x="${recXpos + 10}" dy="18" class="date">${button.date.escapeXml()}</tspan>
            """.trimEnd()
            )
            lines.forEachIndexed { i, str ->
                if (i < 6) {
                    // language=svg
                    sb.append(
                        """
                <tspan x="${recXpos + 10}" dy="18" class="longdesc">${str.toString().escapeXml()}</tspan>
                """.trimIndent()
                    )
                }
            }
            sb.append("</text>")
        }
        sb.append("</svg>")
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
            pointer-events: bounding-box;
            opacity: 1;
        }
        .card:hover {
            opacity: 0.6;
        }
        .headline {
            font-family:  Helvetica, Arial, sans-serif;
            fill: #46494d;
        }
        .link {
        font-family:  Helvetica, Arial, sans-serif;
            fill: #335D79;
        }
        .description {
            font-size: 9pt;
            font-family:  Helvetica, Arial, sans-serif;    
        }
        .category {
            font-size: 10pt;
            font-family:  Helvetica, Arial, sans-serif;    
        }
        .longdesc {
            font-family:  Helvetica, Arial, sans-serif;     
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

    private fun makeTwoTone(x: Int, y: Int, line1: String?, line2: String?, color: String, button: Button, theme: Theme): String {
        val guid = UUID.randomUUID().toString()
        var font = "Arial, Helvetica, sans-serif"
        if(button.font != null) {
            button.font?.family?.let {
                font = it
            }
        }
        return """
            <svg id="twotone$guid" x="$x" y="$y" width="300px" height="191px" viewBox="0 0 300 191" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
                <title>ICON</title>
                <desc></desc>
                <style>
                    .oddstyle$guid {
                        font: bold 60px $font;
                        fill: $color;
                    }

                    .evenstyle$guid {
                        font: bold 60px $font;
                        fill: #ffffff;
                    }
                </style>
                <g id="Page-1$guid" stroke="none" stroke-width="1" fill="#FFFFFF">
                    <rect width="100%" height="100%" fill="none" />
                    <rect width="100%" height="50%" fill="#FFFFFF"/>
                    <rect y="95.5" width="100%" height="50%" fill="$color" />
                    <text text-anchor="middle" x="150" y="67.75" class="oddstyle$guid">$line1</text>
                    <text text-anchor="middle" x="150" y="163.25" class="evenstyle$guid">$line2</text>
                </g>
            </svg>
        """.trimIndent()
    }
}