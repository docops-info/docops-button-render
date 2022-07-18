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


class PanelPermutationTest {

    @ParameterizedTest
    @CsvFileSource(resources = ["/smoke/SingleItemPermutationTest.csv"], numLinesToSkip = 1)
    fun `should run through a number of item specific scenarios`(
        scenario: String,
        whenThemeType: String,
        whenTitle: String,
        whenLink: String,
        whenDate: String,
        whenDescription: String,
        whenAuthor: String,
        whenItemType: String,
        whenForegroundColor: String,
        whenBackgroundColor: String,
        whenBase64Image: String?,
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
            dropShadow = 3

            type = ButtonType.valueOf(whenThemeType)
        }

        fun imageValue(whenBase64Image: String?): ButtonImage? {

            return if(whenBase64Image.isNullOrBlank()) {
                null
            } else {
                ButtonImage(ref = whenBase64Image, type = "image/png")
            }
        }

        val item = Button(
            title = whenTitle,
            link = whenLink,
            date = whenDate,
            description = whenDescription,
            authors = mutableListOf( whenAuthor),
            type = whenItemType,
            foregroundColor = whenForegroundColor,
            backgroundColor = whenBackgroundColor,
            buttonImage = imageValue(whenBase64Image)
        )

        val svg = panel.render(mutableListOf(item), theme)

        val builderFactory = DocumentBuilderFactory.newInstance()
        val builder = builderFactory.newDocumentBuilder()
        val xmlDocument: Document = builder.parse(ByteArrayInputStream(svg.toByteArray()))
        val xPath: XPath = XPathFactory.newInstance().newXPath()

        //title
        val titleNodeList = xPath.compile("//*[@class=\"title\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(titleNodeList.item(0).textContent, thenTitle)

        //type
        val typeNodeList = xPath.compile("//*[@class=\"category\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(typeNodeList.item(0).textContent, thenItemType)

        //link
        val linkNodeList = xPath.compile("//@href").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(linkNodeList.item(0).textContent, thenLink)

        //description
        val descriptionNodeList = xPath.compile("//use/title[@class=\"description\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(descriptionNodeList.item(0).textContent, thenDescription)

        //date
        val dateNodeList = xPath.compile("//*[@class=\"date\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(dateNodeList.item(0).textContent, thenDate)

        //author
        val authorNodeList = xPath.compile("//*[@class=\"author\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(authorNodeList.item(0).textContent, thenAuthor)

        //write the svg
        val path = "src/test/resources/smoke/result/"
        val dir = File(path)
        if(!dir.exists()) { dir.mkdir() }
        val file = File("$path/$scenario.svg")
        file.writeBytes(svg.toByteArray())
    }

}