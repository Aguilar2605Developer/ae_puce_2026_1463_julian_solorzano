package com.pucetec.events.controllers

import com.pucetec.events.dto.EventRequest
import com.pucetec.events.dto.EventResponse
import com.pucetec.events.services.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

// El controller solo recibe el request, llama al service y devuelve el response
@RestController
@RequestMapping("/api/events")
class EventController(private val eventService: EventService) {

    // GET /api/events → público, lista todos los eventos
    @GetMapping
    fun getAllEvents(): ResponseEntity<List<EventResponse>> =
        ResponseEntity.ok(eventService.getAllEvents())

    // GET /api/events/{id} → público, detalle de un evento
    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: Long): ResponseEntity<EventResponse> =
        ResponseEntity.ok(eventService.getEventById(id))

    // POST /api/events → privado, crea un evento
    @PostMapping
    fun createEvent(@RequestBody request: EventRequest): ResponseEntity<EventResponse> =
        ResponseEntity(eventService.createEvent(request), HttpStatus.CREATED)
}