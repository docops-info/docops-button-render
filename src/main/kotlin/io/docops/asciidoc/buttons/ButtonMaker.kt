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


abstract class ButtonMaker {
    abstract fun makeButtons(buttons: MutableList<MutableList<Button>>, theme: Theme): String
    protected val types = mutableMapOf<String, String>()
    private var startLegendHeight = 0
    private fun height(
        buttons: MutableList<MutableList<Button>>,
        heightFactor: Int,
        defaultHeight: Int,
        theme: Theme
    ): Int {
        var height = defaultHeight
        if (buttons.size > 1) {
            height = heightFactor * buttons.size
        }
        if (theme.legendOn) {
            startLegendHeight = height + 30
            val unique = buttonTypes(buttons = buttons, theme = theme)
            height += 20 * (unique.size)
            height += 20
        }
        return height
    }

    private fun buttonTypes(buttons: MutableList<MutableList<Button>>, theme: Theme): MutableMap<String, String> {
        buttons.forEach {
            it.forEach { btn ->
                types[theme.buttonColor(btn)] = (btn.type)
            }
        }
        return types
    }

    fun makeSvgHead(
        buttons: MutableList<MutableList<Button>>,
        heightFactor: Int = 170,
        defaultHeight: Int = 250,
        widthFactor: Int = 100,
        theme: Theme
    ): String {
        val height = height(buttons, heightFactor = heightFactor, defaultHeight = defaultHeight, theme = theme)
        val maxWidth = theme.columns * widthFactor + theme.columns * 10
        // language=svg
        return """<?xml version="1.0" standalone="no"?>
                <svg xmlns="http://www.w3.org/2000/svg" width="$maxWidth" height="${height+20}"
                xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 $maxWidth ${height+20}"
                >"""
    }

    fun makeDefs(theme: Theme): String {
        // language=svg
        return """
        <defs>
        <filter id="dropShadow" height="130%">
            <feGaussianBlur in="SourceAlpha" stdDeviation="1"/>
            <feOffset dx="5" dy="5" result="offsetblur"/>
            <feComponentTransfer>
                <feFuncA type="linear" slope="0.9"/>
            </feComponentTransfer>
            <feMerge>
                <feMergeNode/>
                <feMergeNode in="SourceGraphic"/>
            </feMerge>
        </filter>
        <filter id="filter">
            <feMorphology in="SourceAlpha" operator="dilate" radius="2" result="OUTLINE"/>
            <feComposite operator="out" in="OUTLINE" in2="SourceAlpha"/>
        </filter>
        <linearGradient id="linear-gradient-0" gradientUnits="userSpaceOnUse" x1="162.375" y1="40.053" x2="162.375" y2="9.99">
            <stop offset="0" stop-color="#ff857a" stop-opacity="1"/>
            <stop offset="1" stop-color="#f0a2b7" stop-opacity="1"/>
        </linearGradient>
        <linearGradient id="linear-gradient-1" gradientUnits="userSpaceOnUse" x1="471.482" y1="39.988" x2="471.482" y2="9.983">
            <stop offset="0" stop-color="#ffa801" stop-opacity="1"/>
            <stop offset="1" stop-color="#ffcc01" stop-opacity="1"/>
        </linearGradient>
        <linearGradient id="linear-gradient-2" gradientUnits="userSpaceOnUse" x1="781.482" y1="39.988" x2="781.482" y2="9.983">
            <stop offset="0" stop-color="#cb43f6" stop-opacity="1"/>
            <stop offset="1" stop-color="#ec4cbd" stop-opacity="1"/>
        </linearGradient>
        <linearGradient id="linear-gradient-3" gradientUnits="userSpaceOnUse" x1="162.375" y1="80.053" x2="162.375" y2="49.99">
            <stop offset="0" stop-color="#ff857a" stop-opacity="1"/>
            <stop offset="1" stop-color="#f0a2b7" stop-opacity="1"/>
        </linearGradient>
        <linearGradient id="linear-gradient-4" gradientUnits="userSpaceOnUse" x1="471.482" y1="79.988" x2="471.482"
                        y2="49.983">
            <stop offset="0" stop-color="#ffa801" stop-opacity="1"/>
            <stop offset="1" stop-color="#ffcc01" stop-opacity="1"/>
        </linearGradient>
        <linearGradient id="linear-gradient-5" gradientUnits="userSpaceOnUse" x1="781.482" y1="79.988" x2="781.482" y2="49.983">
            <stop offset="0" stop-color="#cb43f6" stop-opacity="1"/>
            <stop offset="1" stop-color="#ec4cbd" stop-opacity="1"/>
        </linearGradient>
        <circle id="myCircle" cx="0" cy="0"  r="60" class="card"/>
        <rect id="legendRect" x="0" y="0" width="10" height="10"  class="legend"/>
        <rect id="mySlimRect" x="0" y="0" width="150" height="150" rx="5" ry="5" class="card"/>
        <rect id="myLargeRect" fill="#fffefa" x="0" y="0" width="300" height="400" rx="5" ry="5" class="card" filter="url(#dropShadow)">
            <title class="description"/>
        </rect>
        <rect id="myLargerHeroRect" x="0" y="0" width="300" height="191" rx="5" ry="5" class="card"/>
        <rect id="myPanel" x="0" y="0" width="300" height="30" rx="5" ry="5" class="card"/>
        ${theme.defs}
    </defs>
    """
    }

    fun makeSvgEnd(): String {
        return "</svg>"
    }



    fun drawLegend(types: MutableMap<String, String>): String {
        val sb = StringBuilder("<svg>")
        var recXpos = "10"
        var textXPos = "30"
        var yPos = startLegendHeight - 20
        val size = types.size/2

        var count = 0

        types.forEach { (color, desc) ->
            if(count == size) {
                recXpos = "50%"
                yPos = startLegendHeight - 20
                textXPos = "54%"
            }
            // language=svg
            sb.append(
                """
                <use x="$recXpos" y="$yPos" style="fill:$color" class="card" xlink:href="#legendRect"/>
                <text x="$textXPos" y="${yPos + 10}" class="legendText">$desc</text>
            """.trimIndent()
            )
            yPos += 20
            count++
        }
        sb.append("</svg>")
        return sb.toString()
    }


}

