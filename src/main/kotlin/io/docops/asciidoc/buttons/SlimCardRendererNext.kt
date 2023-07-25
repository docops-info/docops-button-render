package io.docops.asciidoc.buttons

import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.buttons.theme.Theme
import io.docops.asciidoc.utils.escapeXml

class SlimCardRendererNext : AbstractButtonRenderer() {
    override fun drawButtons(buttonList: MutableList<MutableList<Button>>, theme: Theme): String {
        val sb = StringBuilder("<g transform=\"scale(${theme.scale})\">")
        buttonList.forEachIndexed { index, buttons ->
            sb.append(drawButton(index, buttons, theme))
        }
        sb.append("</g>")
        return sb.toString()
    }

    private fun drawButton(index: Int, buttons: MutableList<Button>, theme: Theme): String {
        val btns = StringBuilder()
        val win = if (!theme.newWin) {
            "_top"
        } else {
            "_blank"
        }
        var startX = 10

        var startY = 10
        if (index > 0) {
            startY = index * BUTTON_HEIGHT + (index * BUTTON_PADDING) + BUTTON_SPACING
        }
        buttons.forEach { button: Button ->
            val color = theme.buttonColor(button)
            val lines = linesToMultiLineText(wrapText( button.description.escapeXml(), 30f), 10, 2)
            val title = linesToMultiLineText(wrapText(button.title.escapeXml(), 30f), 12, 75)

            btns.append(
        """
        <g transform="translate($startX,$startY)" cursor="pointer">
        <a xlink:href="${button.link}" target="$win" style='text-decoration: none; font-family:Arial; fill: #fcfcfc;'>
        <rect x="0" y="0" fill="#fcfcfc" width="$BUTTON_HEIGHT" height="$BUTTON_HEIGHT" rx="5" ry="5"  stroke="#000000" class="${button.id}_cls raise">
            <title>${button.title.escapeXml()}</title>
        </rect>
        <path filter="url(#buttonBlur)" class="${button.id}_cls"  d="M 0 5.0 A 5.0 5.0 0 0 1 5.0 0 L 145.0 0 A 5.0 5.0 0 0 1 150.0 5.0 L 150.0 35.0 A 0.0 0.0 0 0 1 150.0 35.0 L 0.0 35.0 A 0.0 0.0 0 0 1 0 35.0 Z"/>
        <path fill="url(#overlayGrad)" class="${button.id}_cls"  d="M 0 5.0 A 5.0 5.0 0 0 1 5.0 0 L 145.0 0 A 5.0 5.0 0 0 1 150.0 5.0 L 150.0 35.0 A 0.0 0.0 0 0 1 150.0 35.0 L 0.0 35.0 A 0.0 0.0 0 0 1 0 35.0 Z"/>
        <text text-anchor="middle" x="75" y="8" class="glass" style="fill: #fcfcfc; font-family: Arial,Helvetica, sans-serif; font-size: 10px; font-weight: bold;">
            $title
        </text>
         <text x="0" y="38" class="${theme.buttonTextColor(button)}">
            $lines
        </text>
        <path class="${button.id}_cls" transform="translate(0,125)" d="M 0 0.0 A 0.0 0.0 0 0 1 0.0 0 L 150.0 0 A 0.0 0.0 0 0 1 150.0 0.0 L 150.0 20.0 A 5.0 5.0 0 0 1 144.0 25.0 L 5.0 25.0 A 5.0 5.0 0 0 1 0 20.0 Z"/>
        <text x="145" y="135" style="fill: #555555; font-family: Arial,Helvetica, sans-serif; font-size: 10px; font-weight: bold; font-style: italic;"  text-anchor="end">${authorsToTSpans(button.authors, "145")}</text>
        <text x="145" y="145" style="fill: #555555; font-family: Arial,Helvetica, sans-serif; font-size: 10px; font-weight: bold;"  text-anchor="end">${button.date}</text>
        </a>
    </g>
        """.trimIndent())
            startX += BUTTON_WIDTH + BUTTON_PADDING

        }
        return btns.toString()
    }

    fun authorsToTSpans(authors: List<String>, x: String): String {
        val s = StringBuilder()
        authors.forEach {
            s.append("""
                <tspan x="$x" dy="-12">$it</tspan>
            """.trimIndent())
        }
        return s.toString()
    }

    override fun height(buttons: MutableList<MutableList<Button>>, theme: Theme): Float {
        if (buttons.size > 1) {
            return (buttons.size * BUTTON_HEIGHT + (buttons.size * 10)) * theme.scale + 10
        }
        val h = BUTTON_HEIGHT + 30
        return h * theme.scale
    }

    override fun width(theme: Theme): Float {
        return (theme.columns * BUTTON_WIDTH + theme.columns * BUTTON_PADDING + theme.columns * BUTTON_PADDING) * theme.scale
    }

    companion object {
       const val BUTTON_HEIGHT = 150
        const val BUTTON_WIDTH = 150
        const val BUTTON_PADDING = 10
    }
}