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
import io.docops.asciidoc.utils.addLinebreaks

class LargeCard : ButtonMaker() {
    override fun makeButtons(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        val sb = StringBuilder(makeSvgHead(
            buttons = buttons,
            heightFactor = 410,
            defaultHeight = 500,
            widthFactor = 300,
            theme = theme
        ))
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

    private fun drawButtonRow(rowCount: Int, row: MutableList<Button>, theme: Theme): String {
        val sb = StringBuilder("<svg>")
        var recXpos = 10
        var yPos = 10
        var dateXpos = 95
        if(rowCount>0) {
            yPos = rowCount * 410 + 10
        }
        row.forEachIndexed { index, button ->
            if(index > 0) {
                recXpos += 320
                dateXpos+= 320
            }
            var win = "_blank"
            if(!theme.newWin) {
                win = "_top"
            }
            val lines = addLinebreaks(button.description, 41)
            var imgOrRec = ""
            button.base64Image?.let {
                imgOrRec = "data:${it.type};base64,${it.base64Str}"
            }
            if(button.base64Image != null) {
                val img = button.base64Image
                img?.let {
                    imgOrRec = """
                        <image x="$recXpos" y="$yPos" width="300" height="191" href="data:image/png;base64, ${img.base64Str}"/>""".trimIndent()
                }
            } else {
                imgOrRec = """
                    <use x="$recXpos" y="$yPos" fill="${theme.buttonColor(button)}" xlink:href="#myLargerHeroRect"/>
                    """.trimIndent()
            }
            if(theme.isPDF) {
                sb.append(
                    """
                   <use x="$recXpos" y="$yPos" xlink:href="#myLargeRect"><title class="description">${button.description.escapeXml()}</title></use>     
                   $imgOrRec
                """.trimIndent()
                )
            } else {
                sb.append(
                    """
                    <a xlink:href="${button.link}" target="$win">
                    <use x="$recXpos" y="$yPos" xlink:href="#myLargeRect"><title class="description">${button.description.escapeXml()}</title></use>    
                   $imgOrRec
                   </a>
                """.trimIndent()
                )
            }
            val authors = StringBuilder()
            button.authors.forEach {
                authors.append("""<tspan x="${recXpos+10}" dy="18" class="author">${it.escapeXml()}</tspan>""")
            }
            sb.append("""
                <text x="${recXpos+10}" y="${yPos+220}" class="headline">${button.type.escapeXml()}</text>
                <text x="${recXpos+10}" y="${yPos+240}" class="longdesc">
                """)
            if(theme.isPDF) {
                sb.append("""<tspan class="link" text-decoration="underline" fill="#335D79">${button.title.escapeXml()}</tspan>""")
            }else {
                sb.append("""<tspan class="link" text-decoration="underline"><a href="${button.link}" fill="#335D79" target="$win">${button.title.escapeXml()}</a></tspan>""")
            }
            sb.append("""
                    $authors
                    <tspan x="${recXpos+10}" dy="18" class="date">${button.date.escapeXml()}</tspan>
            """.trimEnd())
            lines.forEachIndexed { i, str ->
                if(i < 6) {
                    sb.append(
                        """
                <tspan x="${recXpos + 10}" dy="18" class="longdesc">
                    ${str.toString().escapeXml()}
                </tspan>
                """.trimIndent()
                    )
                }
            }
            sb.append("</text>")
        }
        sb.append("</svg>")
        return sb.toString()
    }

    private fun makeStyles(): String {
        return """
            <style>
        rect.card {
            pointer-events: bounding-box;
            opacity: 01;
        }
        rect.card:hover {
            opacity: 0.6;
        }
        .headline {
            font: bold 18px "Noto Sans",sans-serif;;
            fill: #46494d;
        }
        .link {
            font: bold 12px "Noto Sans",sans-serif;;
            fill: #335D79;
        }
        .description {
            font: normal 12px "Noto Sans",sans-serif;
        }
        .longdesc {
            font: normal 11px "Noto Sans",sans-serif;
        }
    </style>
        """.trimIndent()
    }
}