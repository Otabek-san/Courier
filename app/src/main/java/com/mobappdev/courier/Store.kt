package com.mobappdev.courier

data class Store(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val contactPerson: String,
    val phone: String,
    var isEnabled: Boolean? = false
)