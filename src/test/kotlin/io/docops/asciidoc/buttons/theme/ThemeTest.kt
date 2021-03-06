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

package io.docops.asciidoc.buttons.theme


import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class ThemeTest {
    @Test
    fun testFontWeight() {
        val t = theme {
            columns = 5
            fontWeight = "bold"
            dropShadow = 1
        }
        assertEquals("bold", t.fontWeight)
    }

    @Test
    fun invalidFontWeightTest() {
        val thrown: IllegalArgumentException = assertThrows(IllegalArgumentException::class.java, Executable {
            theme {
                columns = 5
                fontWeight = "random"
                typeIs("BUTTON")
            }
        })

        assertEquals("not a valid font weight random", thrown.message)
    }

    @Test
    fun `valid button type`() {
        val t = theme {
            typeIs("BUTTON")
        }
        assertEquals(ButtonType.BUTTON, t.type)
    }

    @Test
    fun `invalid button type`() {
        val thrown: IllegalArgumentException = assertThrows(IllegalArgumentException::class.java, Executable {
           theme {
                typeIs("BUTTOX")
            }
        })
        assertEquals("No enum constant io.docops.asciidoc.buttons.theme.ButtonType.BUTTOX", thrown.message)
    }

    @Test
    fun `invalid drop shadow value`() {
        val thrown: IllegalArgumentException = assertThrows(IllegalArgumentException::class.java, Executable {
            val t = theme {
                dropShadow = 11
            }
        })
        assertEquals("dropShadow value 11 does not fall in the range 0..9", thrown.message)
    }
}