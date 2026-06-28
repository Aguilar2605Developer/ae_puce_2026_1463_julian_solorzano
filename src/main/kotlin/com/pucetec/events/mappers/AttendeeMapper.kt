package com.pucetec.events.mappers

import com.pucetec.events.dto.AttendeeRequest
import com.pucetec.events.dto.AttendeeResponse
import com.pucetec.events.entities.Attendee

// Convierte un AttendeeRequest a una Entity lista para guardar en BD
fun AttendeeRequest.toAttendee(): Attendee = Attendee(
    name = this.name,
    email = this.email
)

// Convierte una Entity a un AttendeeResponse para devolver al cliente
fun Attendee.toResponse(): AttendeeResponse = AttendeeResponse(
    id = this.id,
    name = this.name,
    email = this.email
)