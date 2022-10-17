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
        div {
            color: #000000;
            font: 15px "Inter var", system-ui, "Helvetica Neue", Helvetica, Arial, sans-serif;
            height: 100%;
            overflow: auto;
        }
        li {
            text-underline: none;
        }
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


        .shadow {
            -webkit-filter: drop-shadow(3px 3px 2px rgba(0, 0, 0, .7));
            filter: drop-shadow(3px 3px 2px rgba(0, 0, 0, .7));
        }
    </style>"""
    }

    private fun createItem(button: Button, row: Int, column: Int, theme: Theme): String {
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
        <foreignObject x="${itemWidth + 15 + 105}" y="${itemHeight + 30}" width="180" height="200">
                <div xmlns="http://www.w3.org/1999/xhtml">${linksToList(button)}</div>
        </foreignObject>
    </g>
        """.trimIndent()
    }
//${button.description.escapeXml().take(80)}
    private fun linksToList(button: Button): String {
        val sb = StringBuilder()
        button.links?.forEach {
            sb.append("""<li>
                <a href="${it.href}">${it.label}</a>
                </li>""".trimMargin())
        }
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
        return """<defs>
    <filter id="duotone" x="-10%" y="-10%" width="120%" height="120%" filterUnits="objectBoundingBox" primitiveUnits="userSpaceOnUse" color-interpolation-filters="sRGB">
        <feColorMatrix type="matrix" values="1 0 0 0 0
1 0 0 0 0
1 0 0 0 0
0 0 0 1 0" in="SourceGraphic" result="colormatrix"/>
        <feComponentTransfer in="colormatrix" result="componentTransfer">
            <feFuncR type="table" tableValues="0.27 0.86 0.97"/>
            <feFuncG type="table" tableValues="0.01 0.08 0.81"/>
            <feFuncB type="table" tableValues="0.02 0.24 0.05"/>
            <feFuncA type="table" tableValues="0 1"/>
        </feComponentTransfer>
    </filter>
        <filter id="redsunset" x="-10%" y="-10%" width="120%" height="120%" filterUnits="objectBoundingBox" primitiveUnits="userSpaceOnUse" color-interpolation-filters="sRGB">
            <feColorMatrix type="matrix" values="1 0 0 0 0
1 0 0 0 0
1 0 0 0 0
0 0 0 1 0" in="SourceGraphic" result="colormatrix"/>
            <feComponentTransfer in="colormatrix" result="componentTransfer">
                <feFuncR type="table" tableValues="0.43 0.97"/>
                <feFuncG type="table" tableValues="0.06 0.88"/>
                <feFuncB type="table" tableValues="0.37 0.79"/>
                <feFuncA type="table" tableValues="0 1"/>
            </feComponentTransfer>
            <feBlend mode="screen" in="componentTransfer" in2="SourceGraphic" result="blend"/>
        </filter>
    </defs>"""
    }
}