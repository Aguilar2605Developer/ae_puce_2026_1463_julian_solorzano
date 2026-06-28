package com.pucetec.events.services

import com.pucetec.events.dto.EventRequest
import com.pucetec.events.dto.EventResponse
import com.pucetec.events.exceptions.BlankFieldException
import com.pucetec.events.exceptions.EventNotFoundException
import com.pucetec.events.exceptions.InvalidCapacityException
import com.pucetec.events.mappers.toEvent
import com.pucetec.events.mappers.toResponse
import com.pucetec.events.repositories.EventRepository
import org.springframework.stereotype.Service

@Service
class EventService(private val eventRepository: EventRepository) {

    // Devuelve todos los eventos disponibles
    fun getAllEvents(): List<EventResponse> =
        eventRepository.findAll().map { it.toResponse() }

    // Devuelve un evento por su id
    fun getEventById(id: Long): EventResponse =
        eventRepository.findById(id)
            .orElseThrow { EventNotFoundException("Event with id $id not found") }
            .toResponse()

    // Crea un evento validando nombre, venue y capacidad
    fun createEvent(request: EventRequest): EventResponse {
        // Valida que name y venue no estén en blanco
        if (request.name.isBlank() || request.venue.isBlank()) {
            throw BlankFieldException("Name and venue cannot be blank")
        }
        // Valida que totalTickets sea al menos 1
        if (request.totalTickets < 1) {
            throw InvalidCapacityException("Total tickets must be at least 1")
        }
        return eventRepository.save(request.toEvent()).toResponse()
    }
}