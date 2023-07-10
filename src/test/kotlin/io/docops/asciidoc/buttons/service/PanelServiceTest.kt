package io.docops.asciidoc.buttons.service

import io.docops.asciidoc.buttons.dsl.Panels
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

class PanelServiceTest {

    private val src = """
        import io.docops.asciidoc.buttons.dsl.*
        import io.docops.asciidoc.buttons.models.*
        import io.docops.asciidoc.buttons.theme.*
        import io.docops.asciidoc.buttons.*
        
        panels{
         theme {
             colorMap {
                color("#1D9364")
                color("#9A69B8")
                color("#54C041")
                color("#B7FE87")
                color("#552646")
                color("#857318")
                color("#019310")
                color("#146BC5")
            }
            scale = 1.2f
            legendOn = true
             layout {
                
                 columns = 3
                 groupBy = Grouping.TYPE
                 groupOrder = GroupingOrder.ASCENDING
             }
             font = font {
                  family = "Arial, Helvetica, sans-serif"
                  size = "8pt"
                  color = "black"
                  spacing = "normal"
                  bold = false
                  italic = false
                  underline = false
                  vertical = false
                  case = Case.NONE
             }
             newWin = false
             dropShadow = 2
     }
    rectangle{
        link = "https://www.apple.com"
        label = "#1D9364"
        date = "11/11/2022"
        type = "Advertising 0"
    link {
        	href = "https://www.apple.com"
        	label = "Devices"
        }
        	}
        	rectangle{
        		link = "https://www.apple.com"
        		label = "#9A69B8"
        		date = "11/10/2022"
        		type = "Advertising 1"
        link {
        	href = "https://www.apple.com"
        	label = "Devices"
        }
        	}
        	rectangle{
        		link = "https://www.apple.com"
        		label = "#54C041"
        		date = "11/09/2022"
        		type = "Advertising 2"
        link {
        	href = "https://www.apple.com"
        	label = "Devices"
        }
        	}
        	rectangle{
        		link = "https://www.apple.com"
        		label = "#B7FE87"
        		date = "11/08/2022"
        		type = "Advertising 3"
        link {
        	href = "https://www.apple.com"
        	label = "Devices"
        }
        	}
        	rectangle{
        		link = "https://www.apple.com"
        		label = "#552646"
        		date = "11/07/2022"
        		type = "Advertising 4"
        link {
        	href = "https://www.apple.com"
        	label = "Devices"
        }
        	}
        	rectangle{
        		link = "https://www.apple.com"
        		label = "#857318"
        		date = "11/06/2022"
        		type = "Advertising 0"
        link {
        	href = "https://www.apple.com"
        	label = "Devices"
        }
        	}
        	rectangle{
        		link = "https://www.apple.com"
        		label = "#019310"
        		date = "11/05/2022"
        		type = "Advertising 1"
        link {
        	href = "https://www.apple.com"
        	label = "Devices"
        }
        	}
        	rectangle{
        		link = "https://www.apple.com"
        		label = "#146BC5"
        		date = "11/04/2022"
        		type = "Advertising 2"
        link {
        	href = "https://www.apple.com"
        	label = "Devices"
        }
        	}
        }
    """.trimIndent()
    @Test
    fun fromPanelToSvg() {
        val panels = ScriptLoader().parseKotlinScript<Panels>(src)
        val svc = PanelService()
        val svg = svc.fromPanelToSvg(panels)
        val f = File("out/rectangleFromSource.svg")
        f.writeBytes(svg.toByteArray())
    }
}