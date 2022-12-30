package io.docops.asciidoc.charts

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BarChartModelsTest
{
    @Test
    fun validateDsl() {
        val dsl = barChart {
            title = "Hello"
            subTitle = "World"
            xAxisLabel=""
            yAxisLabel=""
            data {
                name = "Animals"
                value = 5.0
                nv("Cats", 4.0)
                nv("Dogs", 2.0)
                nv("Cows", 1.0)
                nv("Sheep", 2.0)
                nv("Pigs", 1.0)
            }
            data {
                name = "Fruits"
                value = 2.0
                nv("Apples", 4.0)
                nv("Oranges", 2.0)
            }
        }

        assertEquals("Hello", dsl.title)

    }
}