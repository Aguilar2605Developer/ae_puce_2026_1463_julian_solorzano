package com.pucetec.events.dto

// Datos que llegan en el request para crear un evento
data class EventRequest(
    val name: String = "",
    val venue: String = "",
    val totalTickets: Int = 0
)

// Datos que se devuelven en el response (nunca se expone la entity directamente)
data class EventResponse(
    val id: Long,
    val name: String,
    val venue: String,
    val totalTickets: Int,
    val availableTickets: Int
)