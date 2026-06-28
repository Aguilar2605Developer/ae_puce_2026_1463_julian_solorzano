package com.pucetec.events.dto

import java.time.LocalDateTime

// Datos que llegan en el request para crear una reserva
data class ReservationRequest(
    val attendeeId: Long = 0,
    val eventId: Long = 0
)

// Datos que se devuelven en el response
data class ReservationResponse(
    val id: Long,
    val attendeeName: String,
    val eventName: String,
    val status: String,
    val createdAt: LocalDateTime
)