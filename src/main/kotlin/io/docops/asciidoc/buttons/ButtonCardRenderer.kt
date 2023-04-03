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
        sb.append(makeDefs(buttons,theme))
        val styles =  makeStyles(buttons, theme)
        if(!theme.isPDF) {
            sb.append(styles)
        }
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
            val win = if(!theme.newWin) {
                "_top"
            } else {
                "_blank"
            }
            if(theme.isPDF) {
                // language=svg
                sb.append(
                    """
                   <use x="$recXpos" y="$yPos" class="card" fill="${theme.buttonColor(button)}" xlink:href="#myPanel">
                        <title class="description">${button.description.escapeXml()}</title>
                   </use>      
                   <text class="category" visibility="hidden">${button.type.escapeXml()}</text>
                   <text class="author" visibility="hidden">${button.authors.firstOrNull()}</text>
                   <text class="date" visibility="hidden">${button.date.escapeXml()}</text>
                """.trimIndent())
                // language=svg
                sb.append("""
                <text x="$textXPos" y="${yPos+20}" class="title ${theme.buttonTextColor(button)}" text-anchor="middle">${button.title.escapeXml()}</text>
            """.trimIndent())
            } else {
                // language=svg
                sb.append(
                   """
                   <a xlink:href="${button.link}" target="$win">
                       <use x="$recXpos" y="$yPos" class="card ${button.id}_cls shape" fill="${theme.buttonColor(button)}" xlink:href="#myPanel">
                           <title class="description">${button.description.escapeXml()}</title>
                       </use>
                       <text class="category" visibility="hidden">${button.type.escapeXml()}</text>
                       <text class="author" visibility="hidden">${button.authors.firstOrNull()}</text>
                       <text class="date" visibility="hidden">${button.date.escapeXml()}</text>
                   </a>
                """.trimIndent()
                )
                // language=svg
                sb.append("""
                <text x="$textXPos" y="${yPos+20}" text-anchor="middle" class="label"><a xlink:href="${button.link}" class="title ${theme.buttonTextColor(button)}">${button.title.escapeXml()}</a></text>
            """.trimIndent())
            }

        }
        sb.append("</g>")
        return sb.toString()
    }

    private fun makeStyles(buttonList: MutableList<MutableList<Button>>, theme: Theme): String {
        val btnGrad = StringBuilder()
        buttonList.forEach { buttons ->
            buttons.forEach { item ->
                theme.buttonTextColor(item)
                btnGrad.append(theme.buildGradientStyle(item))
            }
        }
        // language=css
        var str = """
        <style>
        #${theme.id} rect.card { filter: drop-shadow(3px 5px 2px rgb(0 0 0 / 0.${theme.dropShadow})); pointer-events: bounding-box; opacity: 1; }
        #${theme.id} rect.card:hover { opacity: 0.9; }
        #${theme.id} use.card { filter: drop-shadow(3px 5px 2px rgb(0 0 0 / 0.${theme.dropShadow})); pointer-events: bounding-box; opacity: 1; }
        #${theme.id} use.card:hover { opacity: 0.9; -webkit-animation: 0.5s draw linear forwards; animation: 0.5s draw linear forwards; filter: url(#sofGlow)}
        #${theme.id} .card { pointer-events: bounding-box; opacity: 1; }
        #${theme.id} .card:hover { opacity: 0.9; }
        .subtitle { font-family: Helvetica, Arial, sans-serif; font-weight: normal; font-size: 10px; }
        #${theme.id} rect.legend { pointer-events: bounding-box; opacity: 1; }

        #${theme.id} rect.legend:hover { opacity: 0.9; }
        #${theme.id} .label { font-family: Helvetica, Arial, sans-serif; }
        #${theme.id} .title {fill: white; font-family: Helvetica, Arial, sans-serif; font-weight: normal; font-style: normal; font-size: 9pt;}
        #${theme.id} .legendText {font-size: 9pt;font-family:  Helvetica, Arial, sans-serif; }
        
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
        str += """</style>"""
        return str
    }


}