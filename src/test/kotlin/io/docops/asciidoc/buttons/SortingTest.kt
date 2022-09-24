package io.docops.asciidoc.buttons

import io.docops.asciidoc.buttons.dsl.*
import io.docops.asciidoc.buttons.service.PanelService
import io.docops.asciidoc.buttons.theme.Grouping
import io.docops.asciidoc.buttons.theme.GroupingOrder
import org.junit.jupiter.api.Test
import org.w3c.dom.Document
import org.w3c.dom.NodeList
import java.io.ByteArrayInputStream
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import kotlin.test.assertEquals

class SortingTest {

    @Test
    fun whenSortByAuthorAscending() {
        val t = ButtonTheme()
        t.layout = Layout()
        t.layout.groupBy = Grouping.AUTHOR
        t.layout.groupOrder = GroupingOrder.ASCENDING
        val p = makePanel(t)
        val b = PanelService()
        val svg = b.fromPanelToSvg(p)
        ensureOrder("/svg/g/a/text[@class='author']", svg, "Ginny")
    }

    @Test
    fun whenSortByAuthorDescending() {
        val t = ButtonTheme()
        t.layout = Layout()
        t.layout.groupBy = Grouping.AUTHOR
        t.layout.groupOrder = GroupingOrder.DESCENDING
        val p = makePanel(t)
        val b = PanelService()
        val svg = b.fromPanelToSvg(p)
        ensureOrder("/svg/g/a/text[@class='author']", svg, "Steve Jobs")
    }
    @Test
    fun whenSortByDateAscending() {
        val t = ButtonTheme()
        t.layout = Layout()
        t.layout.groupBy = Grouping.DATE
        t.layout.groupOrder = GroupingOrder.ASCENDING
        val p = makePanel(t)
        val b = PanelService()
        val svg = b.fromPanelToSvg(p)
        ensureOrder("/svg/g/a/text[@class='date']", svg, "08/01/2022")
    }
    @Test
    fun whenSortByDateDescending() {
        val t = ButtonTheme()
        t.layout = Layout()
        t.layout.groupBy = Grouping.DATE
        t.layout.groupOrder = GroupingOrder.DESCENDING
        val p = makePanel(t)
        val b = PanelService()
        val svg = b.fromPanelToSvg(p)
        ensureOrder("/svg/g/a/text[@class='date']", svg, "08/07/2022")
    }
    private fun ensureOrder(path: String, svg: String, compareTo: String) {
        val builderFactory = DocumentBuilderFactory.newInstance()
        val builder = builderFactory.newDocumentBuilder()
        val xmlDocument: Document = builder.parse(ByteArrayInputStream(svg.toByteArray()))
        val xPath: XPath = XPathFactory.newInstance().newXPath()

        val authorNodeList = xPath.compile(path).evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
        assertEquals(authorNodeList.item(0).textContent, compareTo)

    }
    @Test
    fun whenGroupByInsertionOrder() {
        val t = ButtonTheme()
        t.layout = Layout()
        t.layout.groupBy = Grouping.ORDER
        t.layout.groupOrder = GroupingOrder.ASCENDING
        val p = makePanel(t)
        val b = PanelService()
        val svg = b.fromPanelToSvg(p)
        ensureOrder("/svg/g/a/text[@class='date']", svg, "08/07/2022")
    }
    private fun makePanel(t: ButtonTheme) : Panels {
        return panels {
            buttonTheme = t
            panel {
                link = "https://ibm.com"
                label = "IBM"
                date = "08/07/2022"
                author("Ginny")
            }
            panel {
                link = "https://apple.com"
                label = "Apple"
                date = "08/01/2022"
                author("Steve Jobs")
            }
            panel {
                link = "https://www.amazon.com"
                label = "Amazon"
                date = "08/02/2022"
                author("Jeff Bezos")
            }
            panel {
                link = "https://ibm.com"
                label = "IBM"
                date = "08/03/2022"
                author("Ginny")
            }
        }
    }
}