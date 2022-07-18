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

import io.docops.asciidoc.buttons.dsl.font
import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.buttons.models.ButtonImage
import io.docops.asciidoc.buttons.theme.ButtonType
import io.docops.asciidoc.buttons.theme.Grouping
import io.docops.asciidoc.buttons.theme.GroupingOrder
import io.docops.asciidoc.buttons.theme.theme
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.w3c.dom.Document
import org.w3c.dom.NodeList
import java.io.ByteArrayInputStream
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import kotlin.test.assertEquals


class SVGReferenceTest {

    @ParameterizedTest
    @CsvFileSource(resources = ["/smoke/SVGReferenceTest.csv"], numLinesToSkip = 0)
    fun `should run through a number of item specific scenarios`(
        whenThemeType: String,
        whenThemeNumberOfColumns: Int,
        whenNumberOfItems: Int,
        thenFileName: String
    ) {

        val panel = ButtonRenderImpl()

        val theme = theme {
            type = ButtonType.valueOf(whenThemeType)
            columns = whenThemeNumberOfColumns
            groupBy = Grouping.TITLE
            groupOrder = GroupingOrder.ASCENDING
            fontWeight = "normal"
            newWin = false
            legendOn = true
            isPDF = false
        }

        val item = Button(
            title = "Title",
            link = "https://docops.io/",
            date = "1969-12-22",
            description = "This is a description.",
            authors = mutableListOf("Steve Roach", "Mike Duffy", "Ian Cooper Rose"),
            type = "Render Test",
            font =  font{
                color = "#ffffff"
            },
            backgroundColor = "#007700"
        )

        val itemList = mutableListOf<Button>()
        for(i in 1..whenNumberOfItems) {
            itemList.add(item.copy())
        }

        val svg = panel.render(itemList, theme).replace("\\s+".toRegex(), " ")

        val dir = File("/docs/svg/")
        if(!dir.exists()) {
            dir.mkdir()
        }
        val f = File("docs/xml/${thenFileName}.svg")
        f.writeBytes(svg.toByteArray())

    }

}