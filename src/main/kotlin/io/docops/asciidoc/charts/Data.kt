package io.docops.asciidoc.charts

import kotlinx.serialization.Serializable

@Serializable
data class Data(val value: Double, val groupId: String)