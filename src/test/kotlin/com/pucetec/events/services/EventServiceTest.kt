package com.pucetec.events.services

import com.pucetec.events.dto.EventRequest
import com.pucetec.events.entities.Event
import com.pucetec.events.exceptions.BlankFieldException
import com.pucetec.events.exceptions.EventNotFoundException
import com.pucetec.events.exceptions.InvalidCapacityException
import com.pucetec.events.mappers.toResponse
import com.pucetec.events.repositories.EventRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class EventServiceTest {

    @Mock
    lateinit var eventRepository: EventRepository

    @InjectMocks
    lateinit var eventService: EventService

    // Camino feliz: obtener todos los eventos
    @Test
    fun `getAllEvents returns list of events`() {
        val event = Event(1L, "Concert", "Stadium", 100, 100)
        `when`(eventRepository.findAll()).thenReturn(listOf(event))
        val result = eventService.getAllEvents()
        assertEquals(1, result.size)
        assertEquals("Concert", result[0].name)
    }

    // Camino feliz: obtener evento por id
    @Test
    fun `getEventById returns event when found`() {
        val event = Event(1L, "Concert", "Stadium", 100, 100)
        `when`(eventRepository.findById(1L)).thenReturn(Optional.of(event))
        val result = eventService.getEventById(1L)
        assertEquals("Concert", result.name)
    }

    // Evento no encontrado → EventNotFoundException
    @Test
    fun `getEventById throws EventNotFoundException when not found`() {
        `when`(eventRepository.findById(1L)).thenReturn(Optional.empty())
        assertThrows(EventNotFoundException::class.java) {
            eventService.getEventById(1L)
        }
    }

    // Camino feliz: crear evento
    @Test
    fun `createEvent returns event when valid`() {
        val request = EventRequest("Concert", "Stadium", 100)
        val event = Event(1L, "Concert", "Stadium", 100, 100)
        `when`(eventRepository.save(org.mockito.kotlin.any())).thenReturn(event)
        val result = eventService.createEvent(request)
        assertEquals("Concert", result.name)
    }

    // Name en blanco → BlankFieldException
    @Test
    fun `createEvent throws BlankFieldException when name is blank`() {
        val request = EventRequest("", "Stadium", 100)
        assertThrows(BlankFieldException::class.java) {
            eventService.createEvent(request)
        }
    }

    // Venue en blanco → BlankFieldException
    @Test
    fun `createEvent throws BlankFieldException when venue is blank`() {
        val request = EventRequest("Concert", "", 100)
        assertThrows(BlankFieldException::class.java) {
            eventService.createEvent(request)
        }
    }

    // totalTickets < 1 → InvalidCapacityException
    @Test
    fun `createEvent throws InvalidCapacityException when totalTickets is less than 1`() {
        val request = EventRequest("Concert", "Stadium", 0)
        assertThrows(InvalidCapacityException::class.java) {
            eventService.createEvent(request)
        }
    }
}