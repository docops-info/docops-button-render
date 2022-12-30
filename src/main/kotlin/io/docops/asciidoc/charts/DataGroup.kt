package io.docops.asciidoc.charts

import io.docops.asciidoc.charts.Data
import kotlinx.serialization.Serializable

@Serializable
class DataGroup(val dataGroupId: String, val value: Double, val data: MutableList<MutableList<Data>>)
