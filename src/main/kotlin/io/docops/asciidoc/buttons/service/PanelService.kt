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
import java.net.URLEncoder

class PanelService {

    fun fromPanelToSvg(panel: Panels) : String {
        val theme = extractThemeFromPanel(panel)
        val localList = extractButtonList(panel)
        val b = ButtonRenderImpl()
        return b.render(localList, theme)
    }

    private fun extractButtonList(panel: Panels): MutableList<Button> {
        val localList = mutableListOf<Button>()

        when (panel.buttonType) {
            ButtonType.BUTTON -> {
                panel.panelButtons.forEach {
                    val f: Font? = determineFont(panel, it)

                    val btn = Button(
                        title = it.label,
                        link = it.link,
                        description = it.description,
                        authors = it.authors,
                        type = it.label,
                        date = it.date,
                        font = f
                    )
                    localList.add(btn)
                }
            }

            ButtonType.SLIM_CARD -> {
                panel.slimButtons.forEach {
                    val f: Font? = determineFont(panel, it)
                    val btn = Button(
                        title = it.label,
                        link = it.link,
                        description = it.description,
                        authors = it.authors,
                        type = it.type,
                        date = it.date,
                        font = f
                    )
                    localList.add(btn)
                }
            }

            ButtonType.LARGE_CARD -> {
                panel.largeButtons.forEach {
                    val f: Font? = determineFont(panel, it)
                    val btn = Button(
                        title = it.label,
                        link = it.link,
                        description = it.description,
                        authors = it.authors,
                        type = it.type,
                        date = it.date,
                        font = f,
                        buttonImage = it.buttonImage,
                        line1 = it.line1?.line,
                        line2 = it.line2?.line,
                        line1Size = it.line1?.size,
                        line2Size = it.line2?.size
                    )
                    localList.add(btn)
                }
            }

            ButtonType.ROUND -> {
                panel.roundButtons.forEach {
                    val f: Font? = determineFont(panel, it)
                    val btn = Button(
                        title = it.label,
                        link = it.link,
                        description = it.description,
                        authors = it.authors,
                        type = it.label,
                        date = "",
                        font = f
                    )
                    localList.add(btn)
                }
            }

            ButtonType.RECTANGLE -> {
                panel.rectangleButtons.forEach {
                    val f: Font? = determineFont(panel, it)
                    val btn = Button(
                        title = it.label,
                        link = it.link,
                        description = it.description,
                        authors = it.authors,
                        type = it.label,
                        date = "",
                        links = it.links,
                        buttonImage = it.buttonImage,
                        font = f,
                        leadingZeroNumbersOn = it.leadingZeroNumbersOn,
                        numberColor = it.numberColor
                    )
                    localList.add(btn)
                }
            }
            ButtonType.PILL -> {
                panel.panelButtons.forEach {
                    val f: Font? = determineFont(panel, it)

                    val btn = Button(
                        title = it.label,
                        link = it.link,
                        description = it.description,
                        authors = it.authors,
                        type = it.label,
                        date = it.date,
                        font = f
                    )
                    localList.add(btn)
                }
            }
        }
        return localList
    }

    private fun extractThemeFromPanel(panel: Panels): Theme {
        val theme = Theme()

        theme.type = panel.buttonType
        theme.gradientStyle = panel.buttonTheme.gradientStyle
        theme.groupBy = panel.buttonTheme.layout.groupBy
        theme.groupOrder = panel.buttonTheme.layout.groupOrder
        theme.columns = panel.buttonTheme.layout.columns
        if (panel.buttonTheme.colorMap.colors.isNotEmpty()) {
            theme.colorMap = mutableListOf()
            theme.colorMap.addAll(panel.buttonTheme.colorMap.colors)
        }
        theme.defs = panel.buttonTheme.colorMap.colorDefs
        theme.isPDF = panel.isPdf
        theme.legendOn = panel.buttonTheme.legendOn
        theme.newWin = panel.buttonTheme.newWin
        theme.dropShadow = panel.buttonTheme.dropShadow
        theme.scale = panel.buttonTheme.scale
        theme.panelOpacity = panel.buttonTheme.panelOpacity
        theme.strokeColor = panel.buttonTheme.strokeColor
        panel.buttonTheme.font?.let {
            theme.font = it
        }
        return theme
    }

    private fun determineFont(
        panel: Panels,
        pb: ButtonItem
    ): Font? {
        var f: Font? = panel.buttonTheme.font
        if (pb.font != null) {
            f = pb.font
        }
        return f
    }

    fun toLines(filename: String, panels: Panels, server: String): MutableList<String> {
        val theme = extractThemeFromPanel(panels)
        val buttons = extractButtonList(panels)
        val lines = mutableListOf<String>()
        // language=Asciidoc
        lines.add(".$filename")
        lines.add("[options=header]")
        lines.add("|===")
        lines.add("| |Label |Link")
        buttons.forEach {
            lines.add("a|image::$server/api/panel/pancolor?color=${URLEncoder.encode(theme.buttonColor(it), "utf-8")}&label=${URLEncoder.encode(it.title, "utf-8")}&fname=abc.svg[] a|${it.title} a|link:[${it.title}]")
            it.links?.forEach { item ->
                lines.add(" 3+a| link:${item.href}[${item.label}]")
            }
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

            ButtonType.RECTANGLE -> {
                localList.addAll(panel.rectangleButtons)
            }
            ButtonType.PILL -> {
                localList.addAll(panel.panelButtons)
            }
        }
        return localList
    }

}