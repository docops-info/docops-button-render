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

class LargeCard : ButtonMaker() {
    override fun makeButtons(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        val sb = StringBuilder(makeSvgHead(
            buttons = buttons,
            heightFactor = 410,
            defaultHeight = 500,
            widthFactor = 310,
            theme = theme
        ))
        sb.append(makeDefs(theme))
        sb.append(makeStyles(theme))
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
                    // language=svg
                    imgOrRec = """
                        <use x="$recXpos" y="$yPos" class="card" fill="${theme.buttonColor(button)}" xlink:href="#myLargerHeroRect"/>
                        <image x="$recXpos" y="$yPos" width="300" height="191" href="${img.base64Str}"/>""".trimIndent()
                }
            } else {
                // language=svg
                imgOrRec = """
                    <use x="$recXpos" y="$yPos" class="card" fill="${theme.buttonColor(button)}" xlink:href="#myLargerHeroRect"/>
                    """.trimIndent()
            }
            if(theme.isPDF) {
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
                authors.append("""<tspan x="${recXpos+10}" dy="18" class="author">${it.escapeXml()}</tspan>""")
            }
            // language=svg
            sb.append("""
                <text x="${recXpos+10}" y="${yPos+220}" class="headline">${button.type.escapeXml()}</text>
                <text x="${recXpos+10}" y="${yPos+240}" class="longdesc">
                """)
            if(theme.isPDF) {
                // language=svg
                sb.append("""<tspan class="link" text-decoration="underline" fill="#335D79">${button.title.escapeXml()}</tspan>""")
            }else {
                // language=svg
                sb.append("""<tspan class="link" text-decoration="underline"><a href="${button.link}" fill="#335D79" target="$win">${button.title.escapeXml()}</a></tspan>""")
            }
            // language=svg
            sb.append("""
                    $authors
                    <tspan x="${recXpos+10}" dy="18" class="date">${button.date.escapeXml()}</tspan>
            """.trimEnd())
            lines.forEachIndexed { i, str ->
                if(i < 6) {
                    // language=svg
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

    private fun makeStyles(theme: Theme): String {
        //language=html
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
        .card {
            pointer-events: bounding-box;
            opacity: 1;
        }
        .card:hover {
            opacity: 0.6;
        }
        .headline {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
            fill: #46494d;
        }
        .link {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
            fill: #335D79;
        }
        .description {
            font-size: 9pt;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
        }
        .longdesc {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
            font-size: 9pt;
        }
    </style>
        """.trimIndent()
    }
}