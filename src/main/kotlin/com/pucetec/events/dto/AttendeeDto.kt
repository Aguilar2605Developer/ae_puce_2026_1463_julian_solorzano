package com.pucetec.events.dto

// Datos que llegan en el request para crear un asistente
data class AttendeeRequest(
    val name: String = "",
    val email: String = ""
)

// Datos que se devuelven en el response
data class AttendeeResponse(
    val id: Long,
    val name: String,
    val email: String
)