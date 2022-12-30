package io.docops.asciidoc.charts


@ChartDsl
class BarChartModels {
    var width: Int = 800
    var height: Int = 400
    var title = ""
    var subTitle = ""
    var yAxisLabel = ""
    var xAxisLabel = ""
    val legend = mutableListOf<String>()
    val xAxisData = mutableListOf<String>()
    val seriesData = mutableListOf<Data>()
    var barChartData: MutableList<BarChartData> = mutableListOf()
    var dg = mutableListOf<DataGroup>()

    fun data(inputData: BarChartData.() -> Unit) {
        val dt = BarChartData().apply(inputData)
        barChartData.add(dt)
    }

    private fun fromBcToDataGroup() {
        this.barChartData.forEach {
            val list = mutableListOf<MutableList<Data>>()
            it.nameValues.forEach { values ->
                val m = mutableMapOf(values.name to values.value.toDouble())
                list.add(mutableListOf(Data(values.value.toDouble(), values.name)))
            }
            dg.add(DataGroup(dataGroupId = it.name, value = it.value.toDouble(), data = list))
            seriesData.add(Data(it.value.toDouble(), it.name))
            legend.add(it.name)
        }
    }

    private fun xAxisData() {
        barChartData.forEach {
            xAxisData.add(it.name)
        }

    }


    fun validate(): BarChartModels {
        fromBcToDataGroup()
        xAxisData()
        return this
    }

}

@ChartDsl
class BarChartData {
    var name: String = ""
    var value: Number = 0.0
    var nameValues = mutableListOf<NameValue>()
    fun nv(name: String, value: Number) {
        nameValues.add(NameValue(name, value))
    }
}

@ChartDsl
class NameValue(val name: String, val value: Number)

fun barChart(barChartModels: BarChartModels.() -> Unit): BarChartModels {
    return BarChartModels().apply(barChartModels).validate()
}

