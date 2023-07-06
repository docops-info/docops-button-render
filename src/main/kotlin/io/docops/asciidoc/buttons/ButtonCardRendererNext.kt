package io.docops.asciidoc.buttons

import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.buttons.theme.Theme
import io.docops.asciidoc.utils.escapeXml

class ButtonCardRendererNext : AbstractButtonRenderer() {
    override fun drawButtons(buttonList: MutableList<MutableList<Button>>, theme: Theme): String {
        val sb = StringBuilder("<g transform=\"scale(${theme.scale})\">")
        buttonList.forEachIndexed { index, buttons ->
            sb.append(drawButton(index, buttons, theme))
        }
        sb.append("</g>")
        return sb.toString()
    }

    private fun drawButton(index: Int, buttons: MutableList<Button>, theme: Theme) : String {
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
            btns.append(
                """
        <g transform="translate($startX,$startY)" cursor="pointer">
            <a xlink:href="${button.link}" target="$win" style='text-decoration: none; font-family:Arial; fill: #fcfcfc;'>
            <rect x="0" y="0" fill="#29b119" width="300" height="30" class="raise ${button.id}_cls" filter="url(#Bevel2)" rx="10" ry="10"/>
            <text class="category" visibility="hidden">${button.type.escapeXml()}</text>
            <text class="author" visibility="hidden">${button.authors.firstOrNull()}</text>
            <text class="date" visibility="hidden">${button.date.escapeXml()}</text>
            <text x="150" y="20" text-anchor="middle" class="glass ${theme.buttonTextColor(button)}" >${button.title.escapeXml()}</text>
            </a>
        </g>
        """.trimIndent()
            )

            startX += PillButtonRenderer.BUTTON_WIDTH + BUTTON_PADDING

        }
        return btns.toString()
    }
}