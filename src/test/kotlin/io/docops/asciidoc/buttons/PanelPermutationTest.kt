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

import io.docops.asciidoc.buttons.dsl.Case
import io.docops.asciidoc.buttons.dsl.Font
import io.docops.asciidoc.buttons.dsl.font
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
import kotlin.test.Ignore
import kotlin.test.assertEquals


@Ignore
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
        whenButtonImage: String?,
        thenTitle: String,
        thenLink: String,
        thenDate: String,
        thenDescription: String,
        thenAuthor: String,
        thenItemType: String,
        thenForegroundColor: String,
        thenBackgroundColor: String,
        thenButtonImage: String?
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
            font = Font(),
            backgroundColor = whenBackgroundColor,
            buttonImage = imageValue(whenButtonImage)
        )

        val svg = panel.render(mutableListOf(item), theme)

        val builderFactory = DocumentBuilderFactory.newInstance()
        val builder = builderFactory.newDocumentBuilder()
        val xmlDocument: Document = builder.parse(ByteArrayInputStream(svg.toByteArray()))
        val xPath: XPath = XPathFactory.newInstance().newXPath()

        //title
        val titleNodeList = xPath.compile("//*[contains(@class, \"title\")]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(titleNodeList.item(0).textContent.trim(), thenTitle)

        //type
        val typeNodeList = xPath.compile("//*[contains(@class, \"category\")]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(typeNodeList.item(0).textContent.trim(), thenItemType)

        //link
        val linkNodeList = xPath.compile("//@href").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(linkNodeList.item(0).textContent, thenLink)

        //description
        val expr = xPath.compile("//path/title[@class='description']")
        val expr2 = xPath.compile("//use/title[@class='description']")
        var result = expr.evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        if(result.length==0) {
            result = expr2.evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        }
        assertEquals(result.item(0).textContent, thenDescription)

        //date
        val dateNodeList = xPath.compile("//*[contains(@class, \"date\")]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(dateNodeList.item(0).textContent, thenDate)

        //author
        val authorNodeList = xPath.compile("//*[contains(@class, \"author\")]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(authorNodeList.item(0).textContent, thenAuthor)

        //background color
//        val authorNodeList = xPath.compile("//*[@class=\"author\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
//        assertEquals(authorNodeList.item(0).textContent, thenAuthor)

        //foreground color
//        val authorNodeList = xPath.compile("//*[@class=\"author\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
//        assertEquals(authorNodeList.item(0).textContent, thenAuthor)

        //image
//        val authorNodeList = xPath.compile("//*[@class=\"author\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
//        assertEquals(authorNodeList.item(0).textContent, thenAuthor)

        //write the svg
        val path = "src/test/resources/smoke/result/"
        val dir = File(path)
        if(!dir.exists()) { dir.mkdir() }
        val file = File("$path/$scenario.svg")
        file.writeBytes(svg.toByteArray())
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/smoke/SingleItemFontPermutationTest.csv"], numLinesToSkip = 1)
    fun `should run through a number of font specific scenarios`(
        scenario: String,
        whenThemeType: String,
        whenFamily: String?,
        whenSize: String?,
        whenFontColor: String?,
        whenSpacing: String?,
        whenBold: Boolean?,
        whenItalic: Boolean?,
        whenUnderline: Boolean?,
        whenVertical: Boolean?,
        whenCase: String?,
        thenFamily: String,
        thenSize: String,
        thenFontColor: String,
        thenSpacing: String,
        thenBold: Boolean?,
        thenItalic: Boolean?,
        thenUnderline: Boolean?,
        thenVertical: Boolean?,
        thenCase: String?
    ) {

        val panel = ButtonRenderImpl()

        val theme = theme {
            columns = 1
            dropShadow = 3

            type = ButtonType.valueOf(whenThemeType)
        }

        val item = Button(
            title = "Title",
            link = "https://docops.io/",
            date = "1969-12-22",
            description = "This is the description of the item.",
            authors = mutableListOf("Ian Cooper Rose"),
            type = "Category",
            font = font {
                if (whenFamily != null) family = whenFamily
                if (whenSize != null) size = whenSize
                if (whenFontColor != null) color = whenFontColor
                if (whenSpacing != null) spacing = whenSpacing
                if (whenBold != null) bold = whenBold
                if (whenItalic != null) italic = whenItalic
                if (whenUnderline != null) underline = whenUnderline
                if (whenVertical != null) vertical = whenVertical
                if (whenCase != null) case = Case.valueOf(whenCase)
            },
            backgroundColor = "#000000"
        )



        val svg = panel.render(mutableListOf(item), theme)

        val builderFactory = DocumentBuilderFactory.newInstance()
        val builder = builderFactory.newDocumentBuilder()
        val xmlDocument: Document = builder.parse(ByteArrayInputStream(svg.toByteArray()))
        val xPath: XPath = XPathFactory.newInstance().newXPath()

        val styleNodeList = xPath.compile("//style").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        val styleMap = mutableMapOf<String, String>()
        styleNodeList.item(0)
            .firstChild.textContent
            .split("\n")
            .filter { it.contains("btnclass") }[0]
            .substringAfter("{")
            .substringBefore("}")
            .split(";").dropLast(1)
            .forEach{ styleMap[it.split(":")[0]] = it.split(":")[1] }

        assertEquals(styleMap["font-family"], thenFamily)
        assertEquals(styleMap["font-size"], thenSize)
        assertEquals(styleMap["fill"], thenFontColor)
        assertEquals(styleMap["letter-spacing"], thenSpacing)
//        val typeNodeList = xPath.compile("//*[@class=\"category\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
//        assertEquals(typeNodeList.item(0).textContent, thenItemType)

        //color
        //spacing
//        val descriptionNodeList = xPath.compile("//use/title[@class=\"description\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
//        assertEquals(descriptionNodeList.item(0).textContent, thenDescription)

        //bold
//        val dateNodeList = xPath.compile("//*[@class=\"date\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
//        assertEquals(dateNodeList.item(0).textContent, thenDate)

        //italic
//        val authorNodeList = xPath.compile("//*[@class=\"author\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
//        assertEquals(authorNodeList.item(0).textContent, thenAuthor)

        //underline
//        val authorNodeList = xPath.compile("//*[@class=\"author\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
//        assertEquals(authorNodeList.item(0).textContent, thenAuthor)

        //vertical
//        val authorNodeList = xPath.compile("//*[@class=\"author\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
//        assertEquals(authorNodeList.item(0).textContent, thenAuthor)

        //case
//        val authorNodeList = xPath.compile("//*[@class=\"author\"]").evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
//        assertEquals(authorNodeList.item(0).textContent, thenAuthor)

        //write the svg
        val path = "src/test/resources/smoke/result/"
        val dir = File(path)
        if(!dir.exists()) { dir.mkdir() }
        val file = File("$path/$scenario.svg")
        file.writeBytes(svg.toByteArray())
    }

}