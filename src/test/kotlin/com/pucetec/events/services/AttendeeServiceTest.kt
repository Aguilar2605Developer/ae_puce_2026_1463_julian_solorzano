package com.pucetec.events.services

import com.pucetec.events.dto.AttendeeRequest
import com.pucetec.events.entities.Attendee
import com.pucetec.events.exceptions.BlankFieldException
import com.pucetec.events.repositories.AttendeeRepository
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
class AttendeeServiceTest {

    @Mock
    lateinit var attendeeRepository: AttendeeRepository

    @InjectMocks
    lateinit var attendeeService: AttendeeService

    // Camino feliz: obtener todos los asistentes
    @Test
    fun `getAllAttendees returns list of attendees`() {
        val attendee = Attendee(1L, "Juan", "juan@mail.com")
        `when`(attendeeRepository.findAll()).thenReturn(listOf(attendee))
        val result = attendeeService.getAllAttendees()
        assertEquals(1, result.size)
        assertEquals("Juan", result[0].name)
    }

    // Camino feliz: crear asistente
    @Test
    fun `createAttendee returns attendee when valid`() {
        val request = AttendeeRequest("Juan", "juan@mail.com")
        val attendee = Attendee(1L, "Juan", "juan@mail.com")
        `when`(attendeeRepository.save(org.mockito.kotlin.any())).thenReturn(attendee)
        val result = attendeeService.createAttendee(request)
        assertEquals("Juan", result.name)
    }

    // Name en blanco → BlankFieldException
    @Test
    fun `createAttendee throws BlankFieldException when name is blank`() {
        val request = AttendeeRequest("", "juan@mail.com")
        assertThrows(BlankFieldException::class.java) {
            attendeeService.createAttendee(request)
        }
    }

    // Email en blanco → BlankFieldException
    @Test
    fun `createAttendee throws BlankFieldException when email is blank`() {
        val request = AttendeeRequest("Juan", "")
        assertThrows(BlankFieldException::class.java) {
            attendeeService.createAttendee(request)
        }
    }
}