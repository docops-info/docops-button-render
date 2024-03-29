/*
 * Copyright 2020 The DocOps Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.docops.asciidoc.buttons.dsl

import io.docops.asciidoc.buttons.ButtonRenderImpl
import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.buttons.theme.BlueTheme
import io.docops.asciidoc.buttons.theme.ButtonType
import io.docops.asciidoc.buttons.theme.DarkTheme
import io.docops.asciidoc.buttons.theme.DarkTheme2
import io.docops.asciidoc.buttons.theme.GradientStyle
import io.docops.asciidoc.buttons.theme.GreenTheme
import io.docops.asciidoc.buttons.theme.Grouping
import io.docops.asciidoc.buttons.theme.GroupingOrder
import io.docops.asciidoc.buttons.theme.LightGreyTheme
import io.docops.asciidoc.buttons.theme.LightPurpleTheme
import io.docops.asciidoc.buttons.theme.MagentaTheme
import io.docops.asciidoc.buttons.theme.OrangeTheme
import io.docops.asciidoc.buttons.theme.PurpleTheme
import io.docops.asciidoc.buttons.theme.RedTheme
import io.docops.asciidoc.buttons.theme.Theme
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.w3c.dom.Document
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileInputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory


class ButtonsKtTest {

    @Test
    fun fromDslToButtonImage() {
        val list = panels {
            theme {
                colorMap {
                    color("#9C1AB1")
                    color("#4918B8")
                    color("#C861DF")
                    color("#B8D7D5")
                    color("#6526FE")
                    color("#3FA3B2")
                }
                layout {
                    columns = 4
                    groupBy = Grouping.TITLE
                    groupOrder = GroupingOrder.DESCENDING
                }
                font = font {
                    color="#000000"
                }
            }
            panel {
                link = "https://www.google.com"
                label = "Google"
                font = font {
                    color = "#000000"
                    size = "9pt"
                    underline = true
                }
            }
            panel {
                link = "https://www.apple.com"
                label = "Apple"
                font = font {
                    color = "#000000"
                    size = "10pt"
                    underline = true
                }
            }
            panel {
                link = "https://www.microsoft.com"
                label = "Microsoft"
                font = font {
                    color = "#00ff00"
                    size = "10pt"
                    underline = true
                }
            }
            panel {
                link = "https://www.amazon.com"
                label = "Amazon"
            }
            panel {
                link = "https://www.netflix.com"
                label = "Netflix"
            }
            panel {
                link = "https://www.facebook.com"
                label = "Facebook"
            }
            panel {
                link = "https://www.instagram.com"
                label = "Instagram"
            }
        }
        renderImage(list, "panels", BlueTheme)
    }

    @Test
    fun fromDslToButtonImageAlternate() {
        val list = panel {
            theme {
                colorMap {
                    color("#9C1AB1")
                    color("#4918B8")
                    color("#C861DF")
                    color("#B8D7D5")
                    color("#6526FE")
                    color("#3FA3B2")
                }
                layout {
                    columns = 4
                    groupBy = Grouping.DATE
                    groupOrder = GroupingOrder.ASCENDING
                }
                dropShadow = 4
            }
            button {
                link = "https://www.google.com"
                label = "Google"
                font = font {
                    color = "#000000"
                    size = "9pt"
                    underline = true
                }
                date = "06/05/2022"
            }
            button {
                link = "https://www.apple.com"
                label = "Apple"
                font = font {
                    color = "#0000ff"
                    size = "10pt"
                    underline = true
                }
                date = "06/01/2022"
            }
            button {
                link = "https://www.microsoft.com"
                label = "Microsoft"
                font = font {
                    color = "#000000"
                    size = "10pt"
                    underline = true
                }
                date = "05/01/2022"
            }
            button {
                link = "https://www.amazon.com"
                label = "Amazon"
                date = "06/02/2022"
            }
            button {
                link = "https://www.netflix.com"
                label = "Netflix"
                date = "02/01/2022"
            }
            button {
                link = "https://www.facebook.com"
                label = "Facebook"
                date = "04/01/2022"
            }
            button {
                link = "https://www.instagram.com"
                label = "Instagram"
                date = "03/01/2022"
            }
        }
        renderImage(list, "panel", BlueTheme)
    }

    @Test
    fun dslToLargeButtonImage() {
        val list = panels {
            theme {
                layout {
                    columns = 4
                }
                font = font {
                    color = "#3C3939"
                    family = "Calibri, Candara, Segoe, Segoe UI, Optima, Arial, sans-serif"
                    size = "11pt"
                }

            }
            large {
                link = "https://www.google.com"
                label = "Google"
                type = "Search"
                description =
                    "Google is is an American multinational technology company that specializes in Internet-related services and products "
                author("Sergey Brin")
                author("Larry Page")
            }
            large {
                link = "https://www.apple.com"
                label = "Apple"
                type = "Personal Devices"
                description =
                    "Apple Inc. is an American multinational technology company that specializes in consumer electronics, computer software and online services. "
            }
            large {
                link = "https://www.microsoft.com"
                label = "Microsoft"
                type = "Software"
                description =
                    "Microsoft Corporation is an American multinational technology corporation which produces computer software, consumer electronics, personal computers, and related services."
            }
            large {
                link = "https://www.amazon.com"
                label = "Amazon"
                type = "Super Store"
                description =
                    "Amazon.com, Inc. is an American multinational technology company which focuses on e-commerce, cloud computing, digital streaming, and artificial intelligence"
            }
            large {
                link = "https://www.netflix.com"
                label = "Netflix"
                type = "Movie Theater"
                description = "Netflix, Inc. is an American subscription streaming service and production company."
                line1 = Line("Netflix")
                line2 = Line("Movie Theater")
            }
            large {
                link = "https://www.facebook.com"
                label = "Facebook"
                type = "Dive Bar"
                description =
                    "Facebook is an American online social media and social networking service owned by Meta Platforms."
            }
            large {
                link = "https://www.instagram.com"
                label = "Instagram"
                type = "Beach"
                description =
                    "Instagram is an American photo and video sharing social networking service founded by Kevin Systrom and Mike Krieger. "
            }
        }
        renderImage(list, "largepanels", BlueTheme)
    }

    @Test
    fun dslToSlimButtonImage() {
        val list = buttonData()
        renderImage(list, "slimpanels", BlueTheme)
        renderImage(list, "slimpanels2", MagentaTheme)
        renderImage(list, "slimpanels3", LightPurpleTheme)
        renderImage(list, "slimpanels9", PurpleTheme)
        renderImage(list, "slimpanels4", GreenTheme)
        renderImage(list, "slimpanels5", DarkTheme)
        renderImage(list, "slimpanels6", LightGreyTheme)
        renderImage(list, "slimpanels7", OrangeTheme)
        renderImage(list, "slimpanels8", DarkTheme2)
        renderImage(list, "slimpanels10", RedTheme)
    }

    private fun renderImage(list: Panels, fileName: String, gradientStyle: GradientStyle) {
        val theme = Theme()

        theme.gradientStyle = gradientStyle
        theme.type = list.buttonType
        theme.groupBy = list.buttonTheme.layout.groupBy
        theme.groupOrder = list.buttonTheme.layout.groupOrder
        theme.columns = list.buttonTheme.layout.columns
        if (list.buttonTheme.colorMap.colors.isNotEmpty()) {
            theme.colorMap = mutableListOf<String>()
            theme.colorMap.addAll(list.buttonTheme.colorMap.colors)
        }
        theme.defs = list.buttonTheme.colorMap.colorDefs
        theme.dropShadow = list.buttonTheme.dropShadow
        list.buttonTheme.font?.let {
            theme.font = it
        }
        val localList = mutableListOf<Button>()

        when (list.buttonType) {
            ButtonType.BUTTON -> {
                list.panelButtons.forEach {
                    val btn = Button(
                        it.label,
                        it.link,
                        it.description,
                        mutableListOf(),
                        it.label,
                        date = it.date,
                        font = it.font
                    )
                    localList.add(btn)
                }
                genFile(localList = localList, theme = theme, fileName = fileName)
            }

            ButtonType.SLIM_CARD -> {
                list.slimButtons.forEach {
                    val btn = Button(
                        it.label,
                        it.link,
                        it.description,
                        it.authors,
                        it.type,
                        it.date,
                        font = it.font,
                        gradientStyle = it.gradientStyle
                    )
                    localList.add(btn)
                }
                genFile(localList = localList, theme = theme, fileName = fileName)
            }

            ButtonType.LARGE_CARD -> {
                list.largeButtons.forEach {
                    val btn = Button(
                        it.label,
                        it.link,
                        it.description,
                        it.authors,
                        it.type,
                        "",
                        font = it.font,
                        line1 = it.line1?.line,
                        line2 = it.line2?.line
                    )
                    localList.add(btn)
                }
                genFile(localList = localList, theme = theme, fileName = fileName)
            }

            ButtonType.ROUND -> {
                list.roundButtons.forEach {
                    val btn = Button(it.label, it.link, it.description, mutableListOf(), it.label, "", font = it.font)
                    localList.add(btn)
                }
                genFile(localList = localList, theme = theme, fileName = fileName)
            }

            ButtonType.RECTANGLE -> {
                list.rectangleButtons.forEach {
                    val btn = Button(it.label, it.link, it.description, mutableListOf(), it.label, "", font = it.font)
                    localList.add(btn)
                }
                genFile(localList = localList, theme = theme, fileName = fileName)
            }
            ButtonType.PILL -> {
                list.panelButtons.forEach {
                    val btn = Button(
                        it.label,
                        it.link,
                        it.description,
                        mutableListOf(),
                        it.label,
                        date = it.date,
                        font = it.font
                    )
                    localList.add(btn)
                }
                genFile(localList = localList, theme = theme, fileName = fileName)
            }
        }
    }

    fun genFile(localList: MutableList<Button>, theme: Theme, fileName: String) {
        val b = ButtonRenderImpl()
        val svg = b.render(localList, theme)
        val dir = File("out")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val f = File("out/$fileName.svg")
        f.writeBytes(svg.toByteArray())
    }

    @Test
    fun testFailMultipleButtonTypes() {
        assertThrows(IllegalArgumentException::class.java) {
            panels {
                slim {
                    link = "https://www.google.com"
                    label = "Google"
                }
                panel {
                    link = "https://www.google.com"
                    label = "Google"
                }
            }
        }
    }

    @Test
    fun testFailEmptyButtonTypes() {
        assertThrows(IllegalArgumentException::class.java) {
            panels {

            }
        }
    }

    @Test
    fun testAuthorExistsInSvg() {
        val data = buttonData()

        renderImage(data, "xpathdata", BlueTheme)
        val xml = File("out/xpathdata.svg")
        assert(xml.exists())
        val fileIS = FileInputStream(xml)
        val builderFactory = DocumentBuilderFactory.newInstance()
        val builder = builderFactory.newDocumentBuilder()
        val xmlDocument: Document = builder.parse(fileIS)
        val xPath: XPath = XPathFactory.newInstance().newXPath()
        val expression = "//*[@class=\"author\"]"
        val nodeList = xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        for (i in 0 until nodeList.length) {
            //ensure author is found in the document
            assert(contains(data, nodeList.item(i).firstChild.textContent))
        }
    }


    private fun contains(slimButtons: Panels, author: String): Boolean {
        slimButtons.slimButtons.forEach {
            it.authors.forEach { auth ->
                if (auth == author) {
                    return true
                }
            }
        }
        return false
    }

    @Test
    fun newTest() {
        val p = panelButtonData()
        renderImage(p, "largePanelFont", BlueTheme)
    }

    @Test
    fun pillTest() {
        val p = panelButtonData()
        p.buttonType = ButtonType.PILL
        renderImage(p, "largePillFont", BlueTheme)
    }
}

fun panelButtonData() :Panels {
    return panels {
            theme {
                colorMap {
                    color("#3FD9F7")
                    color("#8636A4")
                    color("#CFA118")
                    color("#2F39CF")
                    color("#482902")
                    color("#AD80C2")
                }
                legendOn = false
                layout {
                    columns = 3
                    groupBy = Grouping.TYPE
                    groupOrder = GroupingOrder.ASCENDING
                }
                font = font {
                    color = "#000000"
                    family = "Arial, Helvetica, sans-serif"
                    size = "8pt"
                    underline = true
                }
                dropShadow = 1
            }
            panel {
                link = "https://www.apple.com"
                label = "#3FD9F7"
            }
            panel {
                link = "https://www.apple.com"
                label = "#8636A4"
            }
            panel {
                link = "https://www.apple.com"
                label = "#CFA118"
            }
            panel {
                link = "https://www.apple.com"
                label = "#2F39CF"
            }
            panel {
                link = "https://www.apple.com"
                label = "#482902"
            }
            panel {
                link = "https://www.apple.com"
                label = "#AD80C2"
            }
        }
}
fun buttonData(): Panels {
    return panels {
        theme {
            layout {
                columns = 4
                groupBy = Grouping.AUTHOR
                groupOrder = GroupingOrder.ASCENDING
            }
            font = font {
                color = "#000000"
                bold = false
                italic = false
                size = "10px"
            }
            dropShadow = 2
            colorMap {
                color("#c8dfcc")
                color("#b2a2eb")
                color("#9bf6da")
                color("#eea1d3")
                color("#eccfa1")
                color("#a3e6d4")
                color("#fbb394")
                color("#d5d2b6")
                color("#eedbbf")
                color("#dce5b7")
                color("#b0f6bf")
                color("#f0abb7")
                color("#a7b5f5")
                color("#c7a0f9")
                color("#d4d4b0")
                color("#e1bdbc")
                color("#accdec")
                color("#b5e4dd")
                color("#a1fb88")
                color("#eefab4")
            }

        }
        slim {
            link = "https://www.google.com"
            label = "Google"
            type = "Search"
            description =
                "Google is is an American multinational technology company that specializes in Internet-related services and products "
            author("Sergey Brin")
            author("Larry Page")
        }
        slim {
            link = "https://www.apple.com"
            label = "Apple"
            type = "Personal Devices"
            gradientStyle = BlueTheme
            description =
                "Apple Inc. is an American multinational technology company that specializes in consumer electronics, computer software and online services. "
            author("Steve Jobs")
            author("Steve Wozniak")
            date = "08/20/1978"
        }
        slim {
            link = "https://www.microsoft.com"
            label = "Microsoft"
            type = "Software"
            description =
                "Microsoft Corporation is an American multinational technology corporation which produces computer software, consumer electronics, personal computers, and related services."
            author("Bill Gates")
        }
        slim {
            link = "https://www.amazon.com"
            label = "Amazon"
            type = "Super Store"
            description =
                "Amazon.com, Inc. is an American multinational technology company which focuses on e-commerce, cloud computing, digital streaming, and artificial intelligence"
            author("Jeff Bezos")
        }
        slim {
            link = "https://www.netflix.com"
            label = "Netflix"
            type = "Movie Theater"
            description = "Netflix, Inc. is an American subscription streaming service and production company."
            author("Reed")
            author("Marc")
        }
        slim {
            link = "https://www.facebook.com"
            label = "Facebook"
            type = "Dive Bar"
            description =
                "Facebook is an American online social media and social networking service owned by Meta Platforms."
            author("Mark Zukerberg")
        }
        slim {
            link = "https://www.instagram.com"
            label = "Instagram"
            type = "Beach"
            description = "Instagram is an American photo and video sharing social networking service. "
            author("Kevin")
            author("Mike")
        }
    }
}