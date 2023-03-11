package io.docops.asciidoc.buttons

import io.docops.asciidoc.buttons.dsl.Case
import io.docops.asciidoc.buttons.dsl.Panels
import io.docops.asciidoc.buttons.dsl.font
import io.docops.asciidoc.buttons.dsl.panel
import io.docops.asciidoc.buttons.service.PanelService
import org.junit.jupiter.api.Test
import java.io.File

class RectangleTests {

    @Test
    fun defaultTest() {
        val p =   panel {
            theme {
            }
            rectangle {
                label = "This Is One"
                link = "https://docops.io/"
            }
            rectangle {
                label = "This Is Two"
                link = "https://docops.io/"
            }
            rectangle {
                label = "This Is Three"
                link = "https://docops.io/"
            }
        }
        makeRectangles(p, "out/rectdefault.svg")
    }

    @Test
    fun purpleTest() {
        val p = panel {
            theme {
                font = font {
                    family = "Arial, Helvetica, sans-serif"
                    size = "11pt"
                    color = "purple"
                    spacing = "normal"
                    bold = true
                    case = Case.SMALLCAPS
                }
            }
            rectangle {
                label = "This Is One"
                link = "https://docops.io/"
            }
            rectangle {
                label = "This Is Two"
                link = "https://docops.io/"
            }
            rectangle {
                label = "This Is Three"
                link = "https://docops.io/"
            }
        }
        makeRectangles(p, "out/rectpurple.svg")
    }

    @Test
    fun blueTest() {
        val p = panel {
            theme {
                font = font {
                    family = "Arial, Helvetica, sans-serif"
                    size = "11pt"
                    color = "blue"
                    spacing = "normal"
                    bold = true
                    case = Case.SMALLCAPS
                }
            }
            rectangle {
                label = "This Is One"
                link = "https://docops.io/"
                numberColor = "#ffffff"
            }
            rectangle {
                label = "This Is Two"
                link = "https://docops.io/"
                numberColor = "#ffffff"
            }
            rectangle {
                label = "This Is Three"
                link = "https://docops.io/"
                leadingZeroNumbersOn = true
            }
        }
        makeRectangles(p, "out/rectblue.svg")
    }
    fun makeRectangles(panel: Panels, file: String) {
        val svc = PanelService()
        val svg = svc.fromPanelToSvg(panel)
        val f = File(file)
        f.writeBytes(svg.toByteArray())
    }
}