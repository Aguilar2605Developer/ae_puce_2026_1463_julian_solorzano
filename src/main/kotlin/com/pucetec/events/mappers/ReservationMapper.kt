package com.pucetec.events.mappers

import com.pucetec.events.dto.ReservationResponse
import com.pucetec.events.entities.Reservation

// Convierte una Entity Reservation a un ReservationResponse para devolver al cliente
fun Reservation.toResponse(): ReservationResponse = ReservationResponse(
    id = this.id,
    attendeeName = this.attendee.name,
    eventName = this.event.name,
    status = this.status,
    createdAt = this.createdAt
)