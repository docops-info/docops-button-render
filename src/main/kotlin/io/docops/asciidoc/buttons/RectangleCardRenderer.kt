package io.docops.asciidoc.buttons

import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.buttons.theme.Theme
import io.docops.asciidoc.utils.escapeXml

class RectangleCardRenderer : ButtonMaker() {
    private var spacer = 15
    override fun makeButtons(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        //turn of legend as this does not support legend
        theme.legendOn = false
        val sb = StringBuilder(
            makeSvgHead(
                buttons = buttons,
                heightFactor = 135,
                defaultHeight = 110,
                widthFactor = 300,
                theme = theme
            )
        )
        val styles =  makeStyles(buttons, theme)
        if(!theme.isPDF) {
            sb.append(styles)
        }
        sb.append( """
    ${createDefs(buttons, theme)}
    """)
        var row = 0
        var column = 0
        var count = 0
        buttons.forEachIndexed { index, btns ->

            btns.forEachIndexed { current, item ->
                sb.append(createItem(button = item, row, column, theme, count++))
                column++
            }
            row++
            column = 0

        }
        sb.append("</svg>")
        return sb.toString()
    }

    private fun makeStyles(buttonList: MutableList<MutableList<Button>>, theme: Theme): String {
        buttonList.forEach { buttons ->
            buttons.forEach {
                    item -> theme.buttonTextColor(item)
            }
        }
        //language=html
        var str =  """
        <style>
        #${theme.id} .myrect {
            filter: drop-shadow(3px 5px 2px rgb(0 0 0 / 0.${theme.dropShadow}));  
            stroke: #d2ddec;
        }
        #${theme.id} .mybox:hover {
             -webkit-animation: 0.5s draw linear forwards; 
            animation: 0.5s draw linear forwards;
        }

        #${theme.id} .linkText {
            fill: #4076ff;
            font-size: 15px;
            font-family: "Inter var", system-ui, "Helvetica Neue", Helvetica, Arial, sans-serif;
            font-weight: normal;
            cursor: pointer;
        }
        #${theme.id} .linkText:hover {
            fill: #ea0606;
            border: #d2ddec solid;
        }
        @keyframes draw{ 0%{ stroke-dasharray: 140 540; stroke-dashoffset: -474; stroke-width:3px; } 100%{ stroke-dasharray: 760; stroke-dashoffset:0; stroke-width:5px; } }
        .shape{ stroke:black;}


""".trimIndent()
        theme.buttonStyleMap.forEach { (t, u) ->
            str += "#${theme.id} .$u {$t}\n"
        }
        str += """</style>"""
        return str
    }


    private fun createItem(button: Button, row: Int, column: Int, theme: Theme, itemIndex: Int): String {
        var window = "_top"
        if(theme.newWin) {
            window = "_blank"
        }
        val rowHeight = 120
        val itemHeight = (rowHeight * (row)) + 10 + (row * spacer)
        val width = 293
        val itemWidth = (column) * width + 10 + (spacer*column)
        val contentBox  = if(button.buttonImage!= null) {
            makeButtonImage(button,x = itemWidth+12,y = itemHeight+15, itemIndex )
        } else {
            makeNumberedButtonImage(button, x = itemWidth+12,y = itemHeight+15, itemIndex, window)
        }
        //language=svg
        return """
        <g>
        <rect x="$itemWidth" y="$itemHeight" width="$width" class="myrect" height="$rowHeight" rx="12" ry="12" fill="#ffffff" fill-opacity='0.3'/>
        <a xlink:href="${button.link}" class="linkText" target="$window">
        <text x="${itemWidth+15+105}" y="${itemHeight+25}" class="${theme.buttonTextColor(button)}">${button.title.escapeXml()}</text>
        </a>
        <a xlink:href="${button.link}" class="linkText" target="$window">
        <rect x="${itemWidth+10}" y="${itemHeight+10}" height="98" width="98" class="mybox shape" rx="12" ry="12" fill="${theme.buttonColor(button)}"/>
        </a>
        ${contentBox}
        ${makeLinks(itemWidth+15+105, itemHeight + 30, button, window)}
        </g>
        """.trimIndent()
    }


    private fun makeLinks(x: Int, y: Int, button: Button, window: String): String {
        val sb = StringBuilder()
        sb.append("""<text x="$x" y="$y">""".trimIndent())
        var downBy = 16
        button.links?.forEachIndexed { index, link ->
            if(index > 0) {
                downBy = 20
            }
            sb.append("""<tspan  x="$x" dy="$downBy"><a xlink:href="${link.href}" class="linkText" target="$window">${link.label}</a></tspan>""")
        }
        sb.append("</text>")
        return sb.toString()
    }
    private fun makeButtonImage(button: Button, x: Int, y: Int, itemIndex: Int): String {
        button.buttonImage?.let {
            return """
            <image x="${x+10}" y="${y+9}" width="75" height="75" href="${button.buttonImage?.ref}"/>
        """.trimIndent()
        }
        return ""
    }

    private fun makeNumberedButtonImage(button: Button, x: Int, y: Int, itemIndex: Int, window: String) : String {
        var disp = "${itemIndex+1}"
        if(button.leadingZeroNumbersOn && itemIndex < 9) {
            disp = "0${disp}"
        }
        return """
            <g transform="translate(20,20)">
                <text x="${x-2 + 29}" y="${y-4 + 43}" text-anchor="middle" alignment-baseline="central" font-family="Helvetica, sans-serif" font-size="60px"  >
                <a xlink:href="${button.link}" target="$window" fill="${button.numberColor}">$disp</a>
                </text>
            </g>
        """.trimIndent()
    }
    private fun createDefs(buttons: MutableList<MutableList<Button>>, theme: Theme): String {
        return """<defs/>"""
    }
}