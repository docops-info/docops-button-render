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
import io.docops.asciidoc.buttons.theme.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Document
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileInputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory


internal class ButtonsKtTest {

    @Test
    fun fromDslToButtonImage() {
        val list = panels {
            theme {
                layout {
                    columns = 4
                    groupBy = Grouping.TITLE
                    groupOrder = GroupingOrder.DESCENDING
                }
                font {
                    color = "#000000"
                    weight = FontWeight.bold
                }

            }
            panel {
                link = "https://www.google.com"
                label = "Google"
            }
            panel {
                link = "https://www.apple.com"
                label = "Apple"
            }
            panel {
                link = "https://www.microsoft.com"
                label = "Microsoft"
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
        renderImage(list,"panels")
    }

    @Test
    fun dslToLargeButtonImage() {
        val list = panels {
            theme {
                layout {
                    columns = 4
                    groupBy = Grouping.TYPE
                    groupOrder = GroupingOrder.ASCENDING
                }
                font {
                    color = "#000000"
                    weight = FontWeight.bold
                }
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
            large {
                link = "https://www.google.com"
                label = "Google"
                type = "Search"
                description = "Google is is an American multinational technology company that specializes in Internet-related services and products "
                author("Sergey Brin")
                author("Larry Page")
            }
            large {
                link = "https://www.apple.com"
                label = "Apple"
                type = "Personal Devices"
                description = "Apple Inc. is an American multinational technology company that specializes in consumer electronics, computer software and online services. "
            }
            large {
                link = "https://www.microsoft.com"
                label = "Microsoft"
                type = "Software"
                description = "Microsoft Corporation is an American multinational technology corporation which produces computer software, consumer electronics, personal computers, and related services."
            }
            large {
                link = "https://www.amazon.com"
                label = "Amazon"
                type = "Super Store"
                description = "Amazon.com, Inc. is an American multinational technology company which focuses on e-commerce, cloud computing, digital streaming, and artificial intelligence"
            }
            large {
                link = "https://www.netflix.com"
                label = "Netflix"
                type = "Movie Theater"
                description = "Netflix, Inc. is an American subscription streaming service and production company."
            }
            large {
                link = "https://www.facebook.com"
                label = "Facebook"
                type = "Dive Bar"
                description = "Facebook is an American online social media and social networking service owned by Meta Platforms."
            }
            large {
                link = "https://www.instagram.com"
                label = "Instagram"
                type = "Beach"
                description = "Instagram is an American photo and video sharing social networking service founded by Kevin Systrom and Mike Krieger. "
            }
        }
        renderImage(list,"largepanels")
    }

    @Test
    fun dslToSlimButtonImage() {
        val list = buttonData()
        renderImage(list, "slimpanels")
    }

    private fun renderImage(list: Panels, fileName: String) {
        val theme = Theme()

        theme.type = list.buttonType
        theme.groupBy = list.buttonTheme.layout.groupBy
        theme.groupOrder = list.buttonTheme.layout.groupOrder
        theme.columns = list.buttonTheme.layout.columns
        theme.colorMap = list.buttonTheme.colorMap.colors
        theme.defs = list.buttonTheme.colorMap.colorDefs
        theme.dropShadow = list.buttonTheme.dropShadow
        val localList = mutableListOf<Button>()

        when (list.buttonType) {
            ButtonType.BUTTON -> {
                list.panelButtons.forEach {
                    val btn = Button(it.label, it.link, it.description, mutableListOf(), it.label, "")
                    localList.add(btn)
                }
                genFile(localList = localList, theme = theme, fileName = fileName)
            }
            ButtonType.SLIM_CARD -> {
                list.slimButtons.forEach {
                    val btn = Button(it.label, it.link, it.description, it.authors, it.type, it.date)
                    localList.add(btn)
                }
                genFile(localList = localList, theme = theme, fileName = fileName)
            }
            ButtonType.LARGE_CARD -> {
                list.largeButtons.forEach {
                    val btn = Button(it.label, it.link, it.description, it.authors, it.type, "")
                    localList.add(btn)
                }
                genFile(localList = localList, theme = theme, fileName = fileName)
            }
            ButtonType.ROUND -> {
                list.roundButtons.forEach {
                    val btn = Button(it.label, it.link, it.description, mutableListOf(), it.label, "")
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

        renderImage(data, "xpathdata")
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
              if(auth == author) {
                  return true
              }
          }
      }
       return false
    }
}

fun buttonData (): Panels {
    return panels {
        theme {
            layout {
                columns = 4
                groupBy = Grouping.AUTHOR
                groupOrder = GroupingOrder.ASCENDING
            }
            font {
                color = "#000000"
                weight = FontWeight.bold
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
            description = "Google is is an American multinational technology company that specializes in Internet-related services and products "
            author("Sergey Brin")
            author("Larry Page")
        }
        slim {
            link = "https://www.apple.com"
            label = "Apple"
            type = "Personal Devices"
            description = "Apple Inc. is an American multinational technology company that specializes in consumer electronics, computer software and online services. "
            author("Steve Jobs")
            author("Steve Wozniak")
        }
        slim {
            link = "https://www.microsoft.com"
            label = "Microsoft"
            type = "Software"
            description = "Microsoft Corporation is an American multinational technology corporation which produces computer software, consumer electronics, personal computers, and related services."
            author("Bill Gates")
        }
        slim {
            link = "https://www.amazon.com"
            label = "Amazon"
            type = "Super Store"
            description = "Amazon.com, Inc. is an American multinational technology company which focuses on e-commerce, cloud computing, digital streaming, and artificial intelligence"
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
            description = "Facebook is an American online social media and social networking service owned by Meta Platforms."
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