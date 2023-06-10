package io.docops.asciidoc.buttons

import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.buttons.theme.Theme
import io.docops.asciidoc.utils.addLinebreaks
import io.docops.asciidoc.utils.escapeXml

class LargeCardNext {


    fun makeButtons(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        val svgBuilder = StringBuilder()
        svgBuilder.append(startSvg(height = height(buttons), width = width(theme)))
        svgBuilder.append(makeDefs(buttons = buttons, theme = theme))
        svgBuilder.append(drawButtons(buttonList = buttons, theme = theme))
        svgBuilder.append(endSvg())
        return svgBuilder.toString()
    }

    private fun startSvg(height: Int, width: Int): String {
        return """<svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height"
     viewBox="0 0 $width $height" xmlns:xlink="http://www.w3.org/1999/xlink">"""
    }

    private fun endSvg(): String {
        return "</svg>"
    }

    private fun makeDefs(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        val filters = """
            <filter id="Bevel" filterUnits="objectBoundingBox" x="-10%" y="-10%" width="150%" height="150%">
            <feGaussianBlur in="SourceAlpha" stdDeviation="3" result="blur"/>
            <feSpecularLighting in="blur" surfaceScale="5" specularConstant="0.5" specularExponent="10" result="specOut" lighting-color="white">
                <fePointLight x="-5000" y="-10000" z="20000"/>
            </feSpecularLighting>
            <feComposite in="specOut" in2="SourceAlpha" operator="in" result="specOut2"/>
            <feComposite in="SourceGraphic" in2="specOut2" operator="arithmetic" k1="0" k2="1" k3="1" k4="0" result="litPaint" />
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
        val btnGrad = StringBuilder()
        buttons.forEach { button ->
            button.forEach { item ->
                theme.buttonTextColor(item)
                btnGrad.append(theme.buildGradientStyle(item))
            }
        }
        //language="html"
        val style = """
            <style>
            .basecard {
                -webkit-filter: drop-shadow( 3px 3px 2px rgba(0, 0, 0, .3));
                filter: drop-shadow( 3px 3px 2px rgba(0, 0, 0, .3));
            }
            .basecard:hover {
                cursor: pointer;
                stroke-width: 3;
                stroke-dasharray: 8;
            }
            $btnGrad
        </style>
        """

        var linear = ""
        theme.gradientStyle?.let { linear = it.gradientIdToXml() }

        val btnDef = StringBuilder("")
        buttons.forEach { btn ->
            btn.forEach {
                btnDef.append(theme.buildGradientDef(it))
            }
        }


        val uses = """
            <path id="outerBox" fill="#ffffff"  d="M 0 18.0 A 18.0 18.0 0 0 1 18.0 0 L 282.0 0 A 18.0 18.0 0 0 1 300.0 18.0 L 300.0 382.0 A 18.0 18.0 0 0 1 282.0 400.0 L 18.0 400.0 A 18.0 18.0 0 0 1 0 382.0 Z">
                        <title>Hello</title>
                    </path>
            <g id="topTextBox">
                <path fill="#ffffff" d="M 0 18.0 A 18.0 18.0 0 0 1 18.0 0 L 282.0 0 A 18.0 18.0 0 0 1 300.0 18.0 L 300.0 95.5 A 0.0 0.0 0 0 1 300.0 95.5 L 0.0 95.5 A 0.0 0.0 0 0 1 0 95.5 Z"/>
            </g>
            <path id="bottomTextBox" d="M 0 0.0 A 0.0 0.0 0 0 1 0.0 0 L 300.0 0 A 0.0 0.0 0 0 1 300.0 0.0 L 300.0 95.5 A 0.0 0.0 0 0 1 300.0 95.5 L 0.0 95.5 A 0.0 0.0 0 0 1 0 95.5 Z"/>
            <path id="singleBox" d="M 0 18.0 A 18.0 18.0 0 0 1 18.0 0 L 282.0 0 A 18.0 18.0 0 0 1 300.0 18.0 L 300.0 191.0 A 0.0 0.0 0 0 1 300.0 191.0 L 0.0 191.0 A 0.0 0.0 0 0 1 0 191.0 Z"/>
        """
        return """
            <defs>
                $filters
                $linear
                $btnDef
                ${theme.defs}
                $uses
                $style
            </defs>
            """
    }

    private fun drawButtons(buttonList: MutableList<MutableList<Button>>, theme: Theme): String {
        val sb = StringBuilder()
        buttonList.forEachIndexed { index, buttons ->
            sb.append(drawButtonRow(index, buttons, theme))
        }
        return sb.toString()
    }

    private fun drawButtonRow(index: Int, buttons: MutableList<Button>, theme: Theme): String {
        val btns = StringBuilder()
        val win = if (!theme.newWin) {
            "_top"
        } else {
            "_blank"
        }
        var startX = 10

        var startY = 10
        if (index > 0) {
            startY = index * 400 + 20
        }
        buttons.forEach { button: Button ->
            val color = theme.buttonColor(button)
            btns.append(
                """
                <a xlink:href="${button.link}" href="${button.link}" target="$win">
                <g transform="translate($startX,$startY)" class="basecard ${theme.buttonTextColor(button)}">
                <use xlink:href="#outerBox" stroke="$color"><title>${button.title.escapeXml()}</title></use>
                <use xlink:href="#topTextBox"/>
                ${determineLineText(button, color)}
                ${drawText(button, color)}
                </g>
                </a>
                """.trimIndent()
            )

            startX += 300 + BUTTON_PADDING

        }
        return btns.toString()
    }

    private fun determineLineText(button: Button, color: String): String {
        if(button.buttonImage != null) {
            val img = button.buttonImage
            return """<image x="0" y="0" width="300" height="191" xlink:href="${img?.ref}" href="${img?.ref}" clip-path="inset(1px round 18px 18px 0px 0px)"/>"""
        }
        else if ((button.line1 == null) || (button.line2 == null)) {
            return """<use xlink:href="#singleBox"  fill="url(#${button.id})"/>
            """.trimMargin()
        } else {
            return """
            <text text-anchor="middle" x="150" y="67.75" filter="url(#Bevel2)"
                style="fill: $color; font: bold ${button.line1Size} Arial, Helvetica, sans-serif;">${button.line1?.escapeXml()}
            </text>
            <g transform="translate(0,95.5)">
            <use xlink:href="#bottomTextBox" stroke="$color" fill="url(#${button.id})"/>

            <text text-anchor="middle" x="150" y="67.75" filter="url(#Bevel2)"
                  style="fill: #ffffff; font: bold ${button.line2Size} Arial, Helvetica, sans-serif;" >${button.line2?.escapeXml()}
            </text>
        </g>
            """.trimIndent()
        }
    }

    private fun drawText(button: Button, color: String): String {
        val desc = addLinebreaks(button.description, 35)
        val descList = StringBuilder()
        desc.forEach {
            descList.append("""<tspan x="10" dy="14">${it.toString().escapeXml()}</tspan>""")
        }
        val authors = StringBuilder()
        button.authors.forEach {
            authors.append("""<tspan x="10" dy="14">${it.escapeXml()}</tspan>""")
        }
        val title = addLinebreaks(button.type, 40)
        val titleList = StringBuilder()
        title.forEach {
            titleList.append("""<tspan x="10" dy="14" font-style="italic" font-weight="bold" fill="$color">${it.toString().escapeXml()}</tspan>""")
        }
        return """
            <g transform="translate(0,190)">
            <text x="10" y="20" style="font: 12px Arial, Helvetica, sans-serif;">
                <tspan font-weight="bold">${button.title.escapeXml()}</tspan>
                $titleList
                $descList
                $authors
                <tspan x="10" dy="14">${button.date}</tspan>
            </text>
        </g>
        """.trimIndent()
    }

    private fun height(buttons: MutableList<MutableList<Button>>): Int {
        if (buttons.size > 1) {
            return buttons.size * BUTTON_HEIGHT + 20

        }
        return BUTTON_HEIGHT + 20
    }

    private fun width(theme: Theme): Int {
        return theme.columns * BUTTON_WIDTH + theme.columns * BUTTON_PADDING + theme.columns * BUTTON_PADDING
    }


    companion object {
        const val BUTTON_HEIGHT: Int = 410
        const val BUTTON_WIDTH = 300
        const val BUTTON_PADDING = 12
    }
}