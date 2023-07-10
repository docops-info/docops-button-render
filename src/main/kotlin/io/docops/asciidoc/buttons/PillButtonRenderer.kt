package io.docops.asciidoc.buttons

import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.buttons.theme.Theme
import io.docops.asciidoc.utils.escapeXml

class PillButtonRenderer : AbstractButtonRenderer() {



    override fun drawButtons(buttonList: MutableList<MutableList<Button>>, theme: Theme): String {
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
                <a xlink:href="${button.link}" href="${button.link}" target="$win" style="text-decoration: none;">
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
    override fun height(buttons: MutableList<MutableList<Button>>, theme: Theme): Float {
        if (buttons.size > 1) {
            return (buttons.size * BUTTON_HEIGHT + (buttons.size * 10)) * theme.scale
        }
        return (BUTTON_HEIGHT + 20) * theme.scale
    }

     override fun width(theme: Theme): Float {
        return (theme.columns * BUTTON_WIDTH + theme.columns * BUTTON_PADDING + theme.columns * BUTTON_PADDING) * theme.scale
    }

    companion object {
        const val BUTTON_HEIGHT: Int = 56
        const val BUTTON_WIDTH = 300
        const val BUTTON_PADDING = 12
    }
}