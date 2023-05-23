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
        sb.append(makeDefs(buttons, theme))
        val styles =  makeStyles(buttons, theme)
        if(!theme.isPDF) {
            sb.append(styles)
        }
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
        val topLeft = 10f
        val topRight = 10f
        val bottomLeft = 10f
        val bottomRight = 10f
        val sb = StringBuilder("<svg filter=\"url(#Bevel2)\">")
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
            val lines = addLinebreaks(button.description, 35)
            var imgOrRec = ""
            button.buttonImage?.let {
                imgOrRec = "data:${it.type};base64,${it.ref}"
            }
            if (button.buttonImage != null) {
                val img = button.buttonImage
                img?.let {
                    // language=svg
                    imgOrRec = """
                        <g transform="translate($recXpos,$yPos)">
                         <path d="${generateRectPathData(300.toFloat(), (191).toFloat(), topLeft, topRight, 0.0F, 0.0F)}" 
                            class="card" fill="${theme.buttonColor(button)}"/>
                        </g>
                        <image x="$recXpos" y="$yPos" width="300" height="191" xlink:href="${img.ref}" href="${img.ref}"/>""".trimIndent()
                }
            } else {
                // language=svg
                imgOrRec = """
                    <g transform="translate($recXpos,$yPos)">
                    <path d="${generateRectPathData(300.toFloat(), (191).toFloat(), topLeft, topRight, 0.0F, 0.0F)}" class="card ${button.id}_cls" fill="${theme.buttonColor(button)}" filter="url(#dropShadow)"/>
                    </g>
                    """.trimIndent()

                if(button.line1 != null && button.line2 != null) {
                    imgOrRec += makeTwoTone(recXpos,yPos, button.line1, button.line2, theme.buttonColor(button), button, theme)
                }
            }
            var textDeco = "text-decoration: none;"
            button.font?.let {
                if(it.underline){ textDeco =  "text-decoration: underline;"}
            }

            if (theme.isPDF) {
                // language=svg
                sb.append(
                    """
                   <g transform="translate($recXpos,$yPos)">
                     <path d="${generateRectPathData(300.toFloat(), (400).toFloat(), topLeft, topRight, bottomRight, bottomLeft)}" 
                        filter="url(#dropShadow)" fill="#ffffff"/>
                    </g>     
                   $imgOrRec
                """.trimIndent()
                )
            } else {

                // language=svg
                sb.append(
                    """
                    <a xlink:href="${button.link}" target="$win" style='$textDeco'>
                   <g class="glass" transform="translate($recXpos,$yPos)" id="largePanelId">
                     <path d="${generateRectPathData(300.toFloat(), (400).toFloat(), 10.0F, 10.0F, 22.0F, 22.0F)}" 
                        filter="url(#dropShadow)" class="card shape" fill="#ffffff"
                        stroke-width="2" stroke="black" stroke-dasharray="2000" stroke-dashoffset="2000"
                       ><title class="description">${button.description.escapeXml()}</title>
                        <animate id="p1"
                             attributeName="stroke-dashoffset"
                             begin="mouseover"
                             end="mouseout"
                             values="2037;0;2037"
                             dur="2.5s"
                             calcMode="linear"
                             repeatCount="indefinite"
                        />
                        </path>
                    </g> 
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
                        <text x="${recXpos + 10}" y="${yPos + 220}" class="title glass" text-decoration="underline" fill="#335D79">${button.title.escapeXml()}</text>
                        <text x="${recXpos + 10}" y="${yPos + 230}" class="category">${button.type.escapeXml()}</text>
                        <text x="${recXpos + 10}" y="${yPos + 240}" class="longdesc">
                    """.trimIndent())
            } else {
                val typeStr = addLinebreaks(button.type.escapeXml(),35)
                val strBuilder = StringBuilder()
                var dy = 0
                typeStr.forEachIndexed { index, str ->
                    if(index > 0) {
                        dy = 12
                    }
                    strBuilder.append("""<tspan x="${recXpos + 10}" dy="$dy">$str</tspan>""")
                }
                // language=svg
                sb.append("""
                        <a xlink:href="${button.link}" href="${button.link}" class="${theme.buttonTextColor(button)}" style='$textDeco'>
                        <text filter="url(#Bevel2)" x="${recXpos + 10}" y="${yPos + 220}" class="link glass title" fill="#000" >${button.title.escapeXml()}</text>
                        </a>
                        <text x="${recXpos + 10}" y="${yPos + 234}" class="category ${theme.buttonTextColor(button)}">${strBuilder}</text>
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
        val btnGrad = StringBuilder()
        buttonList.forEach { buttons ->
            buttons.forEach {
                item -> theme.buttonTextColor(item)
                btnGrad.append(theme.buildGradientStyle(item))
            }
        }
        //language=html
        var str =  """
        <style>
        #${theme.id} rect.card { pointer-events: bounding-box; opacity: 1; }
        #${theme.id} .card { pointer-events: bounding-box; opacity: 1; }
        #${theme.id} .card:hover {  transition: mask-position 2s ease,-webkit-mask-position 2s ease;
            -webkit-mask-position: 120%;
            mask-position: 120%;
            opacity: 1;}
        #${theme.id} .headline { font-family:  Helvetica, Arial, sans-serif; fill: #46494d; }
        #${theme.id} .link { font-family:  Helvetica, Arial, sans-serif; fill: #335D79; }
        #${theme.id} .description { font-size: 9pt; font-family:  Helvetica, Arial, sans-serif; }
        #${theme.id} .category { font-size: 10pt; font-family:  Helvetica, Arial, sans-serif; }
        #${theme.id} .longdesc { font-family:  Helvetica, Arial, sans-serif; font-size: 9pt; }
        #${theme.id} .legendText { font-size: 9pt; font-family:  Helvetica, Arial, sans-serif; }
        
        @keyframes draw { 
            0% { stroke-dasharray: 140 540; stroke-dashoffset: -474; stroke-width:3px; } 
            100%{ stroke-dasharray: 760; stroke-dashoffset:0; stroke-width:3px; } 
        }
        
        #${theme.id} .shape{ stroke:black;}  
        
        $btnGrad
        """.trimIndent()
        theme.buttonStyleMap.forEach { (t, u) ->
            str += "#${theme.id} .$u {$t}\n"
        }
        str += """
            ${glassStyle()}
            </style>""".trimIndent()
        return str
    }

    private fun makeTwoTone(x: Int, y: Int, line1: String?, line2: String?, color: String, button: Button, theme: Theme): String {
        var font = "Arial, Helvetica, sans-serif"
        if(button.font != null) {
            button.font?.family?.let {
                font = it
            }
        }
        val color1 = theme.buildGradientStyle(button = button)
        val def1= theme.buildGradientDef(button)
        //language=svg
        return """
            <svg class="glass" id="twotone_${button.id}" x="$x" y="$y" width="300px" height="191px" viewBox="0 0 300 191" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
                <title>ICON</title>
                <defs>${def1}</defs>
                <style>
                    .oddstyle${button.id} {
                        font: bold ${button.line1Size} $font;
                        fill: $color;
                        font-family: Arial, Helvetica, sans-serif;
                    }

                    .evenstyle${button.id} {
                        font: bold ${button.line2Size} $font;
                        fill: #ffffff;
                        font-family: Arial, Helvetica, sans-serif;
                    }
                    $color1
                </style>
                <g id="Page-1${button.id}" stroke="none" stroke-width="1" fill="#FFFFFF" >
                    <rect width="100%" height="100%" fill="none" />
                    <path  filter="url(#Bevel2)" d="${generateRectPathData(300.toFloat(), (191/2).toFloat(), 10.0F, 10.0F, 0.0F, 0.0F)}" />
                    
                    <g transform="translate(0,95.5)">
                        <path d="M0,00 300,0 300,95.5 0,95.5" class="${button.id}_cls" filter="url(#Bevel2)"/>
                    </g>
                    <text text-anchor="middle" x="150" y="67.75" class="glass oddstyle${button.id}" filter="url(#Bevel2)">$line1</text>
                    <text text-anchor="middle" x="150" y="163.25" class="glass evenstyle${button.id}" filter="url(#Bevel2)">$line2</text>
                </g>
            </svg>
        """.trimIndent()
    }
}
fun generateRectPathData(width: Float, height: Float, topLetRound:Float, topRightRound:Float, bottomRightRound:Float, bottomLeftRound:Float): String {
    return """M 0 $topLetRound A $topLetRound $topLetRound 0 0 1 $topLetRound 0 L ${(width - topRightRound)} 0 A $topRightRound $topRightRound 0 0 1 $width $topRightRound L $width ${(height - bottomRightRound)} A $bottomRightRound $bottomRightRound 0 0 1 ${(width - bottomRightRound)} $height L $bottomLeftRound $height A $bottomLeftRound $bottomLeftRound 0 0 1 0 ${(height - bottomLeftRound)} Z"""
}