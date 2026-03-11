package org.noblecow.requestprocessing.domain

import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val isNew: Boolean = true,
    val heading: String,
    val body: String
)
