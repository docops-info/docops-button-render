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

import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.buttons.models.ButtonImage
import io.docops.asciidoc.buttons.theme.ButtonType
import io.docops.asciidoc.buttons.theme.Grouping
import io.docops.asciidoc.buttons.theme.GroupingOrder
import io.docops.asciidoc.buttons.theme.theme
import org.junit.jupiter.api.Assertions.assertEquals
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


class PanelPermutationTest {

    @ParameterizedTest
    @CsvFileSource(resources = ["/smoke/SingleItemPermutationTest.csv"], numLinesToSkip = 1)
    fun `should run through a number of item specific scenarios`(
        scenario: String,
        whenThemeType: String,
        whenThemeGroupBy: String,
        whenThemeGroupOrder: String,
        whenThemeFontWeight: String,
        whenThemeNewWindow: Boolean,
        whenTitle: String,
        whenLink: String,
        whenDate: String,
        whenDescription: String,
        whenAuthor: String,
        whenItemType: String,
        whenForegroundColor: String,
        whenBackgroundColor: String,
        whenBase64Image: String?,
        thenThemeType: String,
        thenThemeGroupBy: String,
        thenThemeGroupOrder: String,
        thenThemeFontWeight: String,
        thenThemeNewWindow: String,
        thenTitle: String,
        thenLink: String,
        thenDate: String,
        thenDescription: String,
        thenAuthor: String,
        thenItemType: String,
        thenForegroundColor: String,
        thenBackgroundColor: String,
        thenBase64Image: String?
    ) {

        val panel = ButtonRenderImpl()

        val theme = theme {
            columns = 1
            groupBy = Grouping.valueOf(whenThemeGroupBy)
            groupOrder = GroupingOrder.valueOf(whenThemeGroupOrder)
            fontWeight = whenThemeFontWeight
            type = ButtonType.valueOf(whenThemeType)
            newWin = whenThemeNewWindow
        }

        fun imageValue(whenBase64Image: String?): ButtonImage? {

            return if (whenBase64Image.isNullOrBlank()) {
                null
            } else {
                ButtonImage(base64Str = whenBase64Image, type = "image/png")
            }
        }

        val item = Button(
            title = whenTitle,
            link = whenLink,
            date = whenDate,
            description = whenDescription,
            authors = mutableListOf(whenAuthor),
            type = whenItemType,
            foregroundColor = whenForegroundColor,
            backgroundColor = whenBackgroundColor,
            base64Image = imageValue(whenBase64Image)
        )

        val dir = File("src/test/resources/smoke/result")
        if(!dir.exists()) { dir.mkdir() }
        val svg = panel.render(mutableListOf(item), theme)
        val f = File("src/test/resources/smoke/result/$scenario.svg")
        f.writeBytes(svg.toByteArray())

        val builderFactory = DocumentBuilderFactory.newInstance()
        val builder = builderFactory.newDocumentBuilder()
        val xmlDocument: Document = builder.parse(ByteArrayInputStream(svg.toByteArray()))
        val xPath: XPath = XPathFactory.newInstance().newXPath()

        //title
        val titleNodeList =
            xPath.compile("//*[@class=\"title\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList

        println("title test: ${titleNodeList.item(0).textContent} --> $thenTitle")
        assertEquals(titleNodeList.item(0).textContent, thenTitle)

        //link
        val linkNodeList = xPath.compile("//@href").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(linkNodeList.item(0).textContent, thenLink)

        //description
        val descriptionNodeList =
            xPath.compile("//*[@class=\"description\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(thenDescription, descriptionNodeList.item(1).textContent.trim())

        //date
        val dateNodeList =
            xPath.compile("//*[@class=\"date\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(thenDate, dateNodeList.item(0).textContent, thenDate)

        //first author
        val authorNodeList =
            xPath.compile("//*[@class=\"author\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(authorNodeList.item(0).textContent, thenAuthor)

        val typeNodeList =
            xPath.compile("//*[@class=\"type\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(typeNodeList.item(0).textContent, thenItemType)

//        Assertions.assertTrue(result.contains(thenThemeGroupBy))
//        println("theme type test: ${typeNodeList.item(0).textContent} --> $thenThemeType")
//        Assertions.assertTrue(result.contains(thenThemeGroupOrder))
//        Assertions.assertTrue(result.contains(thenThemeFontWeight))
//        Assertions.assertTrue(result.contains(thenThemeNewWindow))
//        Assertions.assertTrue(svg.contains(thenTitle))
//        Assertions.assertTrue(svg.contains(thenLink))
//        if(!thenDate.isNullOrBlank()) {
//            Assertions.assertTrue(svg.contains(thenDate)) }
//        if(!thenDescription.isNullOrBlank()) {
//            Assertions.assertTrue(svg.contains(thenDescription)) }
//        if(!thenAuthor.isNullOrBlank()) {
//            val svgAuthor = "class=\"author\">(\\w+\\s+\\w)</tspan>".toRegex().find(svg)?.groups?.get(1)
//
//        val expression = "//*[@class=\"author\"]"
//        val nodeList = xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
//
//        println(nodeList)
//
//            Assertions.assertEquals(thenAuthor, svgAuthor) }
//        Assertions.assertTrue(result.contains(thenItemType))
//        Assertions.assertTrue(result.contains(thenForegroundColor))
//        Assertions.assertTrue(result.contains(thenBackgroundColor))
//
//        if(!thenBase64Image.isNullOrBlank()) {
//            Assertions.assertTrue(svg.contains(thenBase64Image))
//        }


    }

}