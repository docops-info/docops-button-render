package io.docops.asciidoc.stackbar

import io.docops.asciidoc.buttons.ButtonRenderImpl
import io.docops.asciidoc.buttons.theme.Grouping
import io.docops.asciidoc.buttons.theme.GroupingOrder
import io.docops.asciidoc.buttons.theme.Theme
import io.docops.asciidoc.stackbar.model.StackModel
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class StackedBarMakerTest {

    @Test
    fun testGenStack() {
        val s = StackedBarMaker(false)
        val str = """
8261.68	|Cherry St. |4810.34	1536.57
7875.87	 | Strawberry Mall|	3126.58	2019.81
4990.23	|Peach St.	|4923.48	1472.59
4658.42	|Lime Av.|2955.55	1390.55
3952.00	|Apple Rd. |1858.46	917.90
        """.trimIndent()
        val svg = s.makeStackedBar(strToStackedModels(str), "Demo Color")
        val dir = File("out")
        if(!dir.exists()) {
            dir.mkdir()
        }
        val f = File("out/stack.svg")
        f.writeBytes(svg.toByteArray())
    }

    @Test
    fun longerStack() {
        val str = """
            3235.65|Pils| Pilsner (also pilsener or simply pils) is a type of pale lager. It takes its name from the Bohemian city of Plzeň (Pilsen), where it was first produced in 1842 by Bavarian brewer Josef Groll. The world's first pale lager, the original Pilsner Urquell, is still produced there today.
            470.25|Export|Export beer is darker and a little heavier flavored beer.
            454.85|Wheat Beer|Wheat beer is a top-fermented beer which is brewed with a large proportion of wheat relative to the amount of malted barley. The two main varieties are German Weizenbier and Belgian witbier; other types include Lambic (made with wild yeast), Berliner Weisse (a cloudy, sour beer), and Gose (a sour, salty beer).
            371.25|mixed Beer|mixing beer with other ingredients, such as a distilled beverage or another style of beer
            343.75|Non-Alcoholic|Non or Low-alcohol beer is beer with little or no alcohol content and aims to reproduce the taste of beer while eliminating the inebriating effects of standard alcoholic brews. Most low-alcohol beers are lagers, but there are some low-alcohol ales.
            330.55|Light Beer|Light beer is a beer, usually a pale lager, that is reduced in alcohol content or in calories compared to regular beers. In 1967, the Rheingold Brewery marketed a 4.2% pale lager, Gablinger's Diet Beer, developed by American biochemist Joseph Owades, as a beer for people dieting.
            293.15|Craft Beer|A craft brewery or microbrewery is a brewery that produces small amounts of beer, typically less than large breweries, and is often independently owned. Such breweries are generally perceived and marketed as having an emphasis on enthusiasm, new flavours, and varied brewing techniques
            0.55|Dark Beer |Dark beers typically range in color from amber to dark reddish brown. They are characterized by their smooth malty flavor
        """.trimIndent()
        val s = StackedBarMaker(false)
        val svg = s.makeStackedBar(strToStackedModels(str), "Demo Color")
        val dir = File("out")
        if(!dir.exists()) {
            dir.mkdir()
        }
        val f = File("out/stack2.svg")
        f.writeBytes(svg.toByteArray())
    }

    private fun strToStackedModels(str: String): MutableList<StackModel> {
        val result = mutableListOf<StackModel>()
        str.lines().forEach { line ->
            val items = line.split("|")
            result.add(StackModel(value = items[0].trim().toDouble(), description = items[1].trim(), fullDescription = items[2].trim()))
        }
        return result
    }
}