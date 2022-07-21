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

package io.docops.asciidoc.buttons.service

import io.docops.asciidoc.buttons.ButtonRenderImpl
import io.docops.asciidoc.buttons.dsl.*
import io.docops.asciidoc.buttons.models.Button
import io.docops.asciidoc.buttons.theme.ButtonType
import io.docops.asciidoc.buttons.theme.Theme

class PanelService {
    
    fun fromPanelToSvg(panel: Panels) : String {
        val theme = Theme()

        theme.type = panel.buttonType
        theme.groupBy = panel.buttonTheme.layout.groupBy
        theme.groupOrder = panel.buttonTheme.layout.groupOrder
        theme.columns = panel.buttonTheme.layout.columns
        if(panel.buttonTheme.colorMap.colors.isNotEmpty()) {
                theme.colorMap = mutableListOf()
                theme.colorMap.addAll(panel.buttonTheme.colorMap.colors)
        }
        theme.defs = panel.buttonTheme.colorMap.colorDefs
        theme.isPDF = panel.isPdf
        theme.legendOn = panel.buttonTheme.legendOn
        theme.newWin = panel.buttonTheme.newWin
        theme.dropShadow = panel.buttonTheme.dropShadow
        panel.buttonTheme.font?.let {
            theme.font = it
        }
        val localList = mutableListOf<Button>()
        val b = ButtonRenderImpl()

        when (panel.buttonType) {
            ButtonType.BUTTON -> {
                panel.panelButtons.forEach {
                    val btn = Button(
                        title = it.label,
                        link = it.link,
                        description = it.description,
                        authors = mutableListOf(),
                        type = it.label,
                        date = "",
                        font = panel.buttonTheme.font
                    )
                    localList.add(btn)
                }
            }
            ButtonType.SLIM_CARD -> {
                panel.slimButtons.forEach {
                    val btn = Button(
                        title = it.label,
                        link = it.link,
                        description = it.description,
                        authors = it.authors,
                        type = it.type,
                        date = it.date,
                        font = panel.buttonTheme.font
                    )
                    localList.add(btn)
                }
            }
            ButtonType.LARGE_CARD -> {
                panel.largeButtons.forEach {
                    val btn = Button(
                        title = it.label,
                        link = it.link,
                        description = it.description,
                        authors = mutableListOf(),
                        type = it.type,
                        date = it.date,
                        font = panel.buttonTheme.font,
                        buttonImage = it.buttonImage
                    )
                    localList.add(btn)
                }
            }
            ButtonType.ROUND -> {
                panel.roundButtons.forEach {
                    val btn = Button(
                        title = it.label,
                        link = it.link,
                        description = it.description,
                        authors = mutableListOf(),
                        type = it.label,
                        date = "",
                        font = panel.buttonTheme.font)
                    localList.add(btn)
                }
            }
        }
        return b.render(localList, theme)

    }

    fun toLines(filename: String, panels: Panels): MutableList<String> {
        val lines = mutableListOf<String>()
        // language=Asciidoc
        lines.add(".$filename")
        lines.add("[options=header]")
        lines.add("|===")
        lines.add("|Label |Link")
        val buttons = toPanelForPdfLinks(panels)
        buttons.forEach {
            lines.add("a|${it.label} | ${it.link}")
        }
        lines.add("|===")
        return lines
    }

    private fun toPanelForPdfLinks(panel: Panels): MutableList<ButtonItem> {
        val localList = mutableListOf<ButtonItem>()

        when (panel.buttonType) {
            ButtonType.BUTTON -> {
                localList.addAll(panel.panelButtons)
            }
            ButtonType.SLIM_CARD -> {
                localList.addAll(panel.slimButtons)
            }
            ButtonType.LARGE_CARD -> {
                localList.addAll(panel.largeButtons)
            }
            ButtonType.ROUND -> {
                localList.addAll(panel.roundButtons)
            }
        }
        return localList
    }

}