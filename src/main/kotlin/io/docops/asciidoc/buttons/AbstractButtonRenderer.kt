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
        return joinXmlLines(svgBuilder.toString())
    }


    abstract fun drawButtons(buttonList: MutableList<MutableList<Button>>, theme: Theme): String
    private fun startSvg(height: Float, width: Float, theme: Theme): String {
        return """<svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height" viewBox="0 0 $width $height" xmlns:xlink="http://www.w3.org/1999/xlink" id="${theme.id}">"""
    }

    private fun endSvg(): String {
        return "</svg>"
    }

    open fun height(buttons: MutableList<MutableList<Button>>, theme: Theme): Float {
        if (buttons.size > 1) {
            return (buttons.size * BUTTON_HEIGHT + (buttons.size * 10)) * theme.scale
        }
        val h = BUTTON_HEIGHT + 20
        return h * theme.scale
    }

     open fun width(theme: Theme): Float {
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
        <filter id="Bevel2" filterUnits="objectBoundingBox" x="-10%" y="-10%" width="150%" height="150%">
            <feGaussianBlur in="SourceAlpha" stdDeviation="0.5" result="blur"/>
            <feSpecularLighting in="blur" surfaceScale="5" specularConstant="0.5" specularExponent="10" result="specOut" lighting-color="white">
                <fePointLight x="-5000" y="-10000" z="0000"/>
            </feSpecularLighting>
            <feComposite in="specOut" in2="SourceAlpha" operator="in" result="specOut2"/>
            <feComposite in="SourceGraphic" in2="specOut2" operator="arithmetic" k1="0" k2="1" k3="1" k4="0" result="litPaint" />
        </filter>
            """

        var str = ""
        theme.buttonStyleMap.forEach { (t, u) ->
            str += "#${theme.id} .$u {$t}\n"
        }
        //language="html"
        val style = """
            <style>
            .glass:after,.glass:before{content:"";display:block;position:absolute}.glass{overflow:hidden;color:#fff;text-shadow:0 1px 2px rgba(0,0,0,.7);background-image:radial-gradient(circle at center,rgba(0,167,225,.25),rgba(0,110,149,.5));box-shadow:0 5px 10px rgba(0,0,0,.75),inset 0 0 0 2px rgba(0,0,0,.3),inset 0 -6px 6px -3px rgba(0,129,174,.2);position:relative}.glass:after{background:rgba(0,167,225,.2);z-index:0;height:100%;width:100%;top:0;left:0;backdrop-filter:blur(3px) saturate(400%);-webkit-backdrop-filter:blur(3px) saturate(400%)}.glass:before{width:calc(100% - 4px);height:35px;background-image:linear-gradient(rgba(255,255,255,.7),rgba(255,255,255,0));top:2px;left:2px;border-radius:30px 30px 200px 200px;opacity:.7}.glass:hover{text-shadow:0 1px 2px rgba(0,0,0,.9)}.glass:hover:before{opacity:1}.glass:active{text-shadow:0 0 2px rgba(0,0,0,.9);box-shadow:0 3px 8px rgba(0,0,0,.75),inset 0 0 0 2px rgba(0,0,0,.3),inset 0 -6px 6px -3px rgba(0,129,174,.2)}.glass:active:before{height:25px}
            .raise {pointer-events: bounding-box;opacity: 1;filter: drop-shadow(3px 5px 2px rgb(0 0 0 / 0.4));}.raise:hover {stroke: ${theme.strokeColor};stroke-width: 3px; opacity: ${theme.panelOpacity};}
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
    private fun joinXmlLines(str: String): String {
        val sb = StringBuilder()
        str.lines().forEach {
            sb.append(it.trim())
        }
        return sb.toString()
    }
    companion object {
        const val BUTTON_HEIGHT: Int = 30
        const val BUTTON_WIDTH = 300
        const val BUTTON_PADDING = 5
        const val  BUTTON_SPACING = 10
    }
}
fun generateRectPathData(width: Float, height: Float, topLetRound:Float, topRightRound:Float, bottomRightRound:Float, bottomLeftRound:Float): String {
    return """M 0 $topLetRound A $topLetRound $topLetRound 0 0 1 $topLetRound 0 L ${(width - topRightRound)} 0 A $topRightRound $topRightRound 0 0 1 $width $topRightRound L $width ${(height - bottomRightRound)} A $bottomRightRound $bottomRightRound 0 0 1 ${(width - bottomRightRound)} $height L $bottomLeftRound $height A $bottomLeftRound $bottomLeftRound 0 0 1 0 ${(height - bottomLeftRound)} Z"""
}

fun wrapText(text: String, width: Float) : MutableList<String> {
    val sb = StringBuilder()
    val words = text.split(" ")
    var rowText = ""
    val lines = mutableListOf<String>()
    words.forEachIndexed { index, s ->
        if(rowText.length + s.length > width) {
            lines.add(rowText)
            rowText = s
            //close tspan, initialize rowtext and set s
        } else {
            rowText += " $s"
        }
    }
    if(rowText.trim().isNotEmpty()) {
        lines.add(rowText)
    }
    return lines

}

fun linesToMultiLineText(lines: MutableList<String>, dy: Int, x: Int): String {
    var text = StringBuilder()
    lines.forEach {
        text.append("""<tspan x="$x" dy="$dy">$it</tspan>""")
    }
    return text.toString()
}