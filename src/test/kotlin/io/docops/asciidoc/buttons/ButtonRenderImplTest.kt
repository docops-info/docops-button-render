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

package io.docops.asciidoc.buttons

import io.docops.asciidoc.buttons.dsl.panels
import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.buttons.models.ButtonImage
import io.docops.asciidoc.buttons.service.PanelService
import io.docops.asciidoc.buttons.theme.Theme
import io.docops.asciidoc.buttons.theme.Grouping
import io.docops.asciidoc.buttons.theme.GroupingOrder
import io.docops.asciidoc.buttons.theme.SlimCardsTheme

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class ButtonRenderImplTest {
    @Test
    fun renderOrder() {
        val b = ButtonRenderImpl()
        val button = Button(
            title = "title", link = "link1",
            description = "description", authors = mutableListOf("Steve"), type = "Awesome",
            date = "11/16/2021", foregroundColor = null, backgroundColor = null
        )
        val buttons = listOf(
            button.copy(authors = mutableListOf("Mike"), type = "Pizza"),
            Button(
                title = "title", link = "link1",
                description = "description", authors = mutableListOf("Steve"), type = "Awesome",
                date = "11/16/2021", foregroundColor = null, backgroundColor = null
            ),
            Button(
                title = "title2", link = "link0", description = "desc", authors = mutableListOf("Ian"), type = "Dirty",
                date = "10/10/2020"
            ),

            )


        val theme = Theme()
        theme.groupBy= Grouping.AUTHOR
        var localList = buttons.toMutableList()
        b.render(localList, theme)
        assertEquals("Ian", localList[0].authors[0])

        theme.groupBy= Grouping.TYPE
        localList = buttons.toMutableList()
        b.render(localList, theme)
        assertEquals("Awesome", localList[0].type)

        theme.groupBy= Grouping.DATE
        theme.groupOrder = GroupingOrder.DESCENDING
        localList = buttons.toMutableList()
        b.render(localList, theme)
        assertEquals("11/16/2021", localList[0].date)

        theme.groupBy= Grouping.TYPE
        theme.groupOrder = GroupingOrder.DESCENDING
        localList = buttons.toMutableList()
        b.render(localList, theme)
        assertEquals("Pizza", localList[0].type)
    }

    @Test
    fun drawSlimCardsSvg() {
        val b = ButtonRenderImpl()
        val buttons = buttons()
        val theme = SlimCardsTheme
        theme.dropShadow = 4
        val localList = buttons.toMutableList()
        val svg = b.render(localList, theme)
        val dir = File("out")
        if(!dir.exists()) {
            dir.mkdir()
        }
        val f = File("out/slim.svg")
        f.writeBytes(svg.toByteArray())
    }

    @Test
    fun drawButtons() {
        val b = ButtonRenderImpl()
        val buttons = buttons()
        val theme = Theme()
        theme.typeIs("BUTTON")
        theme.groupBy= Grouping.TYPE
        theme.groupOrder = GroupingOrder.ASCENDING
        theme.columns = 2
        val localList = buttons.toMutableList()
        val svg = b.render(localList, theme)
        val dir = File("out")
        if(!dir.exists()) {
            dir.mkdir()
        }
        val f = File("out/buttons.svg")
        f.writeBytes(svg.toByteArray())
    }

    @Test
    fun drawLargeButtons() {
        val b = ButtonRenderImpl()
        val buttons = buttons()
        val theme = Theme()
        theme.typeIs("LARGE_CARD")
        theme.groupBy= Grouping.TYPE
        theme.groupOrder = GroupingOrder.ASCENDING
        theme.columns = 4
        theme.legendOn = false
        theme.isPDF = false
        theme.dropShadow = 4
        val localList = buttons.toMutableList()
        val svg = b.render(localList, theme)
        val dir = File("out")
        if(!dir.exists()) {
            dir.mkdir()
        }
        val f = File("out/large.svg")
        f.writeBytes(svg.toByteArray())
    }

    @Test
    fun drawRound() {
        val b = ButtonRenderImpl()
        val buttons= buttons()
        val theme = Theme()
        theme.typeIs("ROUND")
        theme.groupBy= Grouping.TYPE
        theme.columns = 4
        theme.groupOrder = GroupingOrder.ASCENDING
        val localList = buttons.toMutableList()
        val svg = b.render(localList, theme)
        val dir = File("out")
        if(!dir.exists()) {
            dir.mkdir()
        }
        val f = File("out/round.svg")
        f.writeBytes(svg.toByteArray())
    }
    @Test
    fun drawRoundFromDoc () {
        val d = panels {
            theme {
                colorMap{
                    color("#ff6385")
                    color("#36a3eb")
                    color("#9966ff")
                    color("#ffcf56")
                    color("#4bc0c0")
                    color("#FDCBF1")
                }
                legendOn = true
            }
            round {
                link = "https://www.google.com"
                label = "Google"
            }
            round {
                link = "https://www.apple.com"
                label = "Apple"
            }
            round {
                link = "https://www.microsoft.com"
                label = "Microsoft"
            }
            round {
                link = "https://www.amazon.com"
                label = "Amazon"
            }
            round {
                link = "https://www.netflix.com"
                label = "Netflix"
            }
        }
        val p = PanelService()
        val svg =  p.fromPanelToSvg(d)
        val f = File("out/round2.svg")
        f.writeBytes(svg.toByteArray())
    }

    private fun buttons(): List<Button> {
        val button = Button(
            title = "title",
            link = "https://www.jetbrains.com",
            description = "description",
            authors = mutableListOf("Steve Roach","Ian Rose", "Mike Duffy"),
            type = "Awesome",
            date = "11/16/2021",
            foregroundColor = "black",
            backgroundColor = null
        )
        return listOf(
            button.copy(
                authors = mutableListOf("Mike"),
                type = "Pizza",
                title = "Hamburger",
                link = "https://cooking.nytimes.com/recipes/1016595-hamburgers-diner-style",
                buttonImage = ButtonImage(ref = "ayaan.png")
            ),
            Button(
                title = "Google", link = "https://google.com",
                description = "description trying & < >. Bug: if a word in the input is longer than maxLineLength it will be appended to the current line instead of on a too-long line of its own. I assume your line length is something like 80 or 120 characters, in which case this is unlikely to be a problem.", authors = mutableListOf("Steve"), type = "Green",
                date = "11/16/2021", foregroundColor = "black", backgroundColor = "red"
            ),
            Button(
                title = "Maryland's Crab & Snack Shop",
                link = "http://www.maryland.gov",
                description = "desc",
                authors = mutableListOf("Ian"),
                type = "Crabby",
                date = "10/10/2020"
            ),
            button.copy(title = "Bahama", type = "Pizza", description = "Should the description be long?",
                buttonImage = ButtonImage(ref = "ayaan.png")
            ),
            button.copy(title = "PyCharm Edu", type = "Awesome", description = "Should the description be long maybe? Is this going on"),
            button.copy(title = "Rider", type = "Awesome", description = "Should the description be long maybe?"),
            button.copy(title = "Fleet", type = "Awesome", description = "Should the description be long maybe?"),
            button.copy(
                title = "Datagrip",
                type = "Awesome Little Lengthy Description For this tile still has 2 rows?",
                description = "Should the description be long maybe?",
                backgroundColor = "red"
            )

        )
    }
}