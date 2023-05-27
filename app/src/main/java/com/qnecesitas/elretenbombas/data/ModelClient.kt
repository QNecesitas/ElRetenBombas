package com.qnecesitas.elretenbombas.data

data class ModelClient(
    var c_client: Int,
    var name: String,
    var day: Int,
    var month: Int,
    var year: Int,
    var phone: String,
    var waterBomb: String,
    var copli: String,
    var imperente: String,
    var price: Double,
    var warranty: Int,
    var descWork: String,
    var descClient: String
)