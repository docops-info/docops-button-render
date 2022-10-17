package io.docops.asciidoc.buttons

import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.buttons.theme.Theme
import io.docops.asciidoc.utils.escapeXml

class RectangleCardRenderer : ButtonMaker() {
    private var spacer = 10
    override fun makeButtons(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        val sb = StringBuilder(
            makeSvgHead(
                buttons = buttons,
                heightFactor = 410,
                defaultHeight = 500,
                widthFactor = 410,
                theme = theme
            )
        )
        //language=svg
        sb.append( """
    ${makeStyles(buttons, theme)}
    ${createDefs(buttons, theme)}
    """)
        var row = 0
        var column = 0
        buttons.forEachIndexed { index, btns ->

            btns.forEach {
                sb.append(createItem(button = it, row, column, theme))
                column++
            }
            row++
            column = 0

        }
        sb.append("</svg>")
        return sb.toString()
    }

    private fun makeStyles(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        //language=html
        return """<style>
        rect {
            stroke: #d2ddec;
        }

        .titleText {
            fill: #181b38;
            font-size: 16px;
            font-family: "Inter var", system-ui, "Helvetica Neue", Helvetica, Arial, sans-serif;
            font-weight: 600;
            cursor: pointer;
        }

        .linkText {
            fill: #265301;;
            font-size: 15px;
            font-family: "Inter var", system-ui, "Helvetica Neue", Helvetica, Arial, sans-serif;
            font-weight: normal;
            cursor: pointer;
            text-decoration: underline;
        }

        .shadow {
            -webkit-filter: drop-shadow(3px 3px 2px rgba(0, 0, 0, .7));
            filter: drop-shadow(3px 3px 2px rgba(0, 0, 0, .7));
        }
    </style>"""
    }

    private fun createItem(button: Button, row: Int, column: Int, theme: Theme): String {
        var window = "_top"
        if(theme.newWin) {
            window = "_blank"
        }
        val rowHeight = 120
        val itemHeight = (rowHeight * (row)) + 10 + (row * spacer)
        val width = 293
        val itemWidth = (column) * width + 10 + (spacer*column)
        //language=svg
        return """
        <g>
        <rect x="$itemWidth" y="$itemHeight" width="$width" height="$rowHeight" rx="12" ry="12" fill="#ffffff" fill-opacity='0.3;'/>
        <text x="${itemWidth+15+105}" y="${itemHeight+25}" class="titleText">${button.title.escapeXml()}</text>
        <rect x="${itemWidth+10}" y="${itemHeight+10}" height="98" width="98" rx="12" ry="12" fill="${theme.buttonColor(button)}"/>
        ${makeButtonImage(button, x = itemWidth+12,y = itemHeight+15)}
        ${makeLinks(itemWidth+15+105, itemHeight + 30, button, window)}
        </g>
        """.trimIndent()
    }


    private fun makeLinks(x: Int, y: Int, button: Button, window: String): String {
        val sb = StringBuilder()
        sb.append("""<text x="$x" y="$y" class="linkText">""".trimIndent())
        button.links?.forEach {
            sb.append("""<tspan x="$x" dy="16"><a xlink:href="${it.href}" target="$window">${it.label}</a></tspan>""")
        }
        sb.append("</text>")
        return sb.toString()
    }
    private fun makeButtonImage(button: Button, x: Int, y: Int): String {
        button.buttonImage?.let {
            return """
            <image x="${x+10}" y="${y+5}" width="75" height="75" href="${button.buttonImage?.ref}"/>
        """.trimIndent()
        }
        return ""
    }

    private fun createDefs(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        return """<defs/>"""
    }
}