package io.docops.asciidoc.buttons

import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.buttons.theme.Theme
import io.docops.asciidoc.utils.escapeXml

class PillButtonRenderer {

    fun makeButtons(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        val svgBuilder = StringBuilder()
        svgBuilder.append(startSvg(height = height(buttons = buttons), width = width(theme = theme), theme = theme))
        svgBuilder.append(makeDefs(buttons, theme))
        svgBuilder.append(drawButtons(buttonList = buttons, theme = theme))
        svgBuilder.append(endSvg())
        return svgBuilder.toString()
    }

    private fun startSvg(height: Int, width: Int, theme: Theme): String {
        return """<svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height"
     viewBox="0 0 $width $height" xmlns:xlink="http://www.w3.org/1999/xlink" id="${theme.id}">"""
    }

    private fun endSvg(): String {
        return "</svg>"
    }

    private fun makeDefs(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        val btnGrad = StringBuilder()
        buttons.forEach { button ->
            button.forEach { item ->
                theme.buttonTextColor(item)
                btnGrad.append(theme.buildGradientStyle(item))
            }
        }
        var linear = ""
        theme.gradientStyle?.let { linear = it.gradientIdToXml() }

        val btnDef = StringBuilder("")
        buttons.forEach { btn ->
            btn.forEach {
                btnDef.append(theme.buildGradientDef(it))
            }
        }

        val filters = """
        <filter id="buttonBlur">
        <feGaussianBlur in="SourceAlpha" stdDeviation="2" result="blur"/>
        <feOffset in="blur" dy="2" result="offsetBlur"/>
        <feMerge>
            <feMergeNode in="offsetBlur"/>
            <feMergeNode in="SourceGraphic"/>
        </feMerge>
        </filter>

        <linearGradient id="overlayGrad" gradientUnits="userSpaceOnUse" x1="95" y1="-20" x2="95" y2="70">
            <stop offset="0" stop-color="#000000" stop-opacity="0.5"/>
            <stop offset="1" stop-color="#000000" stop-opacity="0"/>
        </linearGradient>

        <filter id="topshineBlur">
            <feGaussianBlur stdDeviation="0.93"/>
        </filter>

        <linearGradient id="topshineGrad" gradientUnits="userSpaceOnUse" x1="95" y1="0" x2="95" y2="40">
            <stop offset="0" stop-color="#ffffff" stop-opacity="1"/>
            <stop offset="1" stop-color="#ffffff" stop-opacity="0"/>
        </linearGradient>

        <filter id="bottomshine">
            <feGaussianBlur stdDeviation="0.95"/>
        </filter>
            """

        var str = ""
        theme.buttonStyleMap.forEach { (t, u) ->
            str += "#${theme.id} .$u {$t}\n"
        }
        //language="html"
        val style = """
            <style>
            $btnGrad
            $str
        </style>
        """
        return """
            <defs>
            $filters
            $linear
            $btnDef
            ${theme.defs}
            $style
            </defs>
        """.trimIndent()
    }

    private fun drawButtons(buttonList: MutableList<MutableList<Button>>, theme: Theme): String {
        val sb = StringBuilder()
        buttonList.forEachIndexed { index, buttons ->
            sb.append(drawPills(index, buttons, theme))
        }
        return sb.toString()
    }

    private fun drawPills(index: Int, buttons: MutableList<Button>, theme: Theme) : String {
        val btns = StringBuilder()
        val win = if (!theme.newWin) {
            "_top"
        } else {
            "_blank"
        }
        var startX = 0

        var startY = 0
        if (index > 0) {
            startY = index * BUTTON_HEIGHT + (index * 10)
        }
        buttons.forEach { button: Button ->
            val color = theme.buttonColor(button)
            btns.append(
                """
                <a xlink:href="${button.link}" href="${button.link}" target="$win">
                <g role="button" cursor="pointer" transform="translate($startX,$startY)">
                    <rect id="button" x="5" y="5" width="$BUTTON_WIDTH" height="$BUTTON_HEIGHT" ry="26" rx="26" class="${button.id}_cls" filter="url(#buttonBlur)" />
            
                    <rect id="buttongrad" x="5" y="5" width="$BUTTON_WIDTH" height="$BUTTON_HEIGHT" ry="26" rx="26" fill="url(#overlayGrad)"/>
                    <text id="label" x="150" y="43" text-anchor="middle" class="${theme.buttonTextColor(button)}">${button.title.escapeXml()}</text>
            
                    <rect id="buttontop" x="15" y="10.5" width="280" height="25" ry="24" rx="24" fill="url(#topshineGrad)" filter="url(#topshineBlur)"/>
                    <rect id="buttonbottom" x="25" y="50" width="260" height="7" fill="#ffffff" ry="24" rx="24" fill-opacity="0.3" filter="url(#bottomshine)"/>
                </g>
                </a>
                """.trimIndent()
            )

            startX += BUTTON_WIDTH + BUTTON_PADDING

        }
        return btns.toString()
    }
    private fun height(buttons: MutableList<MutableList<Button>>): Int {
        if (buttons.size > 1) {
            return buttons.size * BUTTON_HEIGHT + (buttons.size * 10)
        }
        return BUTTON_HEIGHT + 20
    }

    private fun width(theme: Theme): Int {
        return theme.columns * BUTTON_WIDTH + theme.columns * BUTTON_PADDING + theme.columns * BUTTON_PADDING
    }

    companion object {
        const val BUTTON_HEIGHT: Int = 56
        const val BUTTON_WIDTH = 300
        const val BUTTON_PADDING = 12
    }
}