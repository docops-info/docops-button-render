package io.docops.asciidoc.buttons

import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.buttons.theme.Theme
import kotlin.time.times

abstract class AbstractButtonRenderer {
    fun makeButtons(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        val svgBuilder = StringBuilder()
        svgBuilder.append(startSvg(height = height(buttons = buttons, theme = theme), width = width(theme = theme), theme = theme))
        svgBuilder.append(makeDefs(buttons, theme))
        svgBuilder.append(drawButtons(buttonList = buttons, theme = theme))
        svgBuilder.append(endSvg())
        return svgBuilder.toString()
    }

    abstract fun drawButtons(buttonList: MutableList<MutableList<Button>>, theme: Theme): String
    private fun startSvg(height: Float, width: Float, theme: Theme): String {
        return """<svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height"
     viewBox="0 0 $width $height" xmlns:xlink="http://www.w3.org/1999/xlink" id="${theme.id}">"""
    }

    private fun endSvg(): String {
        return "</svg>"
    }

    private fun height(buttons: MutableList<MutableList<Button>>, theme: Theme): Float {
        if (buttons.size > 1) {
            return (buttons.size * BUTTON_HEIGHT + (buttons.size * 10)) * theme.scale
        }
        val h = BUTTON_HEIGHT + 20
        return h * theme.scale
    }

    private fun width(theme: Theme): Float {
        return (theme.columns * BUTTON_WIDTH + theme.columns * BUTTON_PADDING + theme.columns * BUTTON_PADDING) * theme.scale
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
            .raise {pointer-events: bounding-box;opacity: 1;}.raise:hover {stroke: ${theme.strokeColor};stroke-width: 3px; opacity: 0.9;}
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
    companion object {
        const val BUTTON_HEIGHT: Int = 30
        const val BUTTON_WIDTH = 300
        const val BUTTON_PADDING = 5
        const val  BUTTON_SPACING = 10
    }
}