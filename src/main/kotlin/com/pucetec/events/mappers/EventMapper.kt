package com.pucetec.events.mappers

import com.pucetec.events.dto.EventRequest
import com.pucetec.events.dto.EventResponse
import com.pucetec.events.entities.Event

// Convierte un EventRequest a una Entity lista para guardar en BD
fun EventRequest.toEvent(): Event = Event(
    name = this.name,
    venue = this.venue,
    totalTickets = this.totalTickets,
    availableTickets = this.totalTickets
)

// Convierte una Entity a un EventResponse para devolver al cliente
fun Event.toResponse(): EventResponse = EventResponse(
    id = this.id,
    name = this.name,
    venue = this.venue,
    totalTickets = this.totalTickets,
    availableTickets = this.availableTickets
)