package io.docops.asciidoc.buttons

import org.junit.jupiter.api.Test


class RectTests {

    @Test
    fun testRectPathGenerator() {
        val d1 = generateRectPathData(
            150f,
            35f,
            5f,
            topRightRound = 5f,
            bottomRightRound = 0f,
            bottomLeftRound = 0f
        )
        println(d1)
        val d2 = generateRectPathData(
            width = 150f,
            height = 25f,
            topLetRound = 0f,
            topRightRound = 0f,
            bottomRightRound = 5f,
            bottomLeftRound = 5f
        )
        println(d2)
    }

    @Test
    fun wrapTextTest() {
        val text = "The grass is always greener on the other side of the fence"
        val o = wrapText(text, 30f)
        println(o)
        val lines = linesToMultiLineText(o, 10, 2)
        println(lines)
    }
}