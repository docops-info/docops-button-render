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
import io.docops.asciidoc.buttons.theme.ButtonType
import io.docops.asciidoc.buttons.theme.Grouping
import io.docops.asciidoc.buttons.theme.GroupingOrder
import io.docops.asciidoc.buttons.theme.Theme


interface ButtonRenderer {
     fun render(buttons: MutableList<Button>, theme: Theme): String

}

class ButtonRenderImpl : ButtonRenderer {

    override  fun render(buttons: MutableList<Button>, theme: Theme): String {
        groupButtons(buttons, theme.groupBy, theme.groupOrder)
        val rows = toRows(buttons, theme.columns)
        return when (theme.type) {
            ButtonType.SLIM_CARD -> {
                SlimCardRenderer().makeButtons(buttons = rows, theme= theme)
            }
            ButtonType.ROUND -> {
                RoundButtonItemRenderer().makeButtons(buttons = rows, theme = theme)
            }
            ButtonType.BUTTON -> {
                ButtonCardRenderer().makeButtons(buttons = rows, theme = theme)
            }
            ButtonType.LARGE_CARD -> {
                LargeCard().makeButtons(buttons = rows, theme = theme)
            }
        }

    }

    private fun toRows(buttons: MutableList<Button>, cols: Int): MutableList<MutableList<Button>> {
        val rows = mutableListOf<MutableList<Button>>()
        var rowArray = mutableListOf<Button>()
        rows.add(rowArray)
        buttons.forEach {
            if (rowArray.size == cols) {
                rowArray = mutableListOf()
                rows.add(rowArray)
            }
            rowArray.add(it)
        }
        return rows
    }

    private fun groupButtons(buttons: MutableList<Button>, groupBy: Grouping, groupOrder: GroupingOrder) {
        when (groupBy) {
            Grouping.TYPE -> {
                when (groupOrder) {
                    GroupingOrder.DESCENDING -> {
                        buttons.sortByDescending { it.type }
                    }
                    else -> {
                        buttons.sortBy { it.type }
                    }
                }
            }
            Grouping.AUTHOR -> {
                when (groupOrder) {
                    GroupingOrder.DESCENDING -> {
                        buttons.sortByDescending { it.authors[0]}
                    }
                    else -> {
                        buttons.sortBy { it.authors[0] }
                    }
                }
            }
            Grouping.TITLE -> {
                when (groupOrder) {
                    GroupingOrder.DESCENDING -> {
                        buttons.sortByDescending { it.title }
                    }
                    else -> {
                        buttons.sortBy { it.title }
                    }
                }
            }
            Grouping.DATE -> {
                if(groupOrder == GroupingOrder.ASCENDING) {
                    buttons.sortBy { it.dateFromStr() }
                } else {
                    buttons.sortByDescending { it.dateFromStr() }
                }
            }
        }
    }



}



