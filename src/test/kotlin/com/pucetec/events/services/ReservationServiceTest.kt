package com.pucetec.events.services

import com.pucetec.events.dto.ReservationRequest
import com.pucetec.events.entities.Attendee
import com.pucetec.events.entities.Event
import com.pucetec.events.entities.Reservation
import com.pucetec.events.exceptions.*
import com.pucetec.events.repositories.AttendeeRepository
import com.pucetec.events.repositories.EventRepository
import com.pucetec.events.repositories.ReservationRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class ReservationServiceTest {

    @Mock
    lateinit var reservationRepository: ReservationRepository

    @Mock
    lateinit var attendeeRepository: AttendeeRepository

    @Mock
    lateinit var eventRepository: EventRepository

    @InjectMocks
    lateinit var reservationService: ReservationService

    private val attendee = Attendee(1L, "Juan", "juan@mail.com")
    private val event = Event(1L, "Concert", "Stadium", 100, 10)

    // Camino feliz: crear reserva
    @Test
    fun `createReservation returns reservation when valid`() {
        val request = ReservationRequest(1L, 1L)
        val reservation = Reservation(1L, attendee, event, "ACTIVE", LocalDateTime.now())
        `when`(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        `when`(eventRepository.findById(1L)).thenReturn(Optional.of(event))
        `when`(reservationRepository.countByAttendeeIdAndStatus(1L, "ACTIVE")).thenReturn(0)
        `when`(eventRepository.save(org.mockito.kotlin.any())).thenReturn(event)
        `when`(reservationRepository.save(org.mockito.kotlin.any())).thenReturn(reservation)
        val result = reservationService.createReservation(request)
        assertEquals("ACTIVE", result.status)
    }

    // Asistente no existe → AttendeeNotFoundException
    @Test
    fun `createReservation throws AttendeeNotFoundException when attendee not found`() {
        val request = ReservationRequest(1L, 1L)
        `when`(attendeeRepository.findById(1L)).thenReturn(Optional.empty())
        assertThrows(AttendeeNotFoundException::class.java) {
            reservationService.createReservation(request)
        }
    }

    // Evento no existe → EventNotFoundException
    @Test
    fun `createReservation throws EventNotFoundException when event not found`() {
        val request = ReservationRequest(1L, 1L)
        `when`(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        `when`(eventRepository.findById(1L)).thenReturn(Optional.empty())
        assertThrows(EventNotFoundException::class.java) {
            reservationService.createReservation(request)
        }
    }

    // Evento agotado → SoldOutException
    @Test
    fun `createReservation throws SoldOutException when event is sold out`() {
        val request = ReservationRequest(1L, 1L)
        val soldOutEvent = Event(1L, "Concert", "Stadium", 100, 0)
        `when`(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        `when`(eventRepository.findById(1L)).thenReturn(Optional.of(soldOutEvent))
        assertThrows(SoldOutException::class.java) {
            reservationService.createReservation(request)
        }
    }

    // Límite de reservas superado → ReservationLimitExceededException
    @Test
    fun `createReservation throws ReservationLimitExceededException when limit reached`() {
        val request = ReservationRequest(1L, 1L)
        `when`(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        `when`(eventRepository.findById(1L)).thenReturn(Optional.of(event))
        `when`(reservationRepository.countByAttendeeIdAndStatus(1L, "ACTIVE")).thenReturn(4)
        assertThrows(ReservationLimitExceededException::class.java) {
            reservationService.createReservation(request)
        }
    }

    // Camino feliz: cancelar reserva
    @Test
    fun `cancelReservation returns cancelled reservation when valid`() {
        val reservation = Reservation(1L, attendee, event, "ACTIVE", LocalDateTime.now())
        `when`(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation))
        `when`(eventRepository.save(org.mockito.kotlin.any())).thenReturn(event)
        `when`(reservationRepository.save(org.mockito.kotlin.any())).thenReturn(
            reservation.copy(status = "CANCELLED")
        )
        val result = reservationService.cancelReservation(1L)
        assertEquals("CANCELLED", result.status)
    }

    // Reserva no existe → ReservationNotFoundException
    @Test
    fun `cancelReservation throws ReservationNotFoundException when not found`() {
        `when`(reservationRepository.findById(1L)).thenReturn(Optional.empty())
        assertThrows(ReservationNotFoundException::class.java) {
            reservationService.cancelReservation(1L)
        }
    }

    // Reserva ya cancelada → ReservationAlreadyCancelledException
    @Test
    fun `cancelReservation throws ReservationAlreadyCancelledException when already cancelled`() {
        val reservation = Reservation(1L, attendee, event, "CANCELLED", LocalDateTime.now())
        `when`(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation))
        assertThrows(ReservationAlreadyCancelledException::class.java) {
            reservationService.cancelReservation(1L)
        }
    }

    // Camino feliz: obtener todas las reservas
    @Test
    fun `getAllReservations returns list of reservations`() {
        val reservation = Reservation(1L, attendee, event, "ACTIVE", LocalDateTime.now())
        `when`(reservationRepository.findAll()).thenReturn(listOf(reservation))
        val result = reservationService.getAllReservations()
        assertEquals(1, result.size)
    }
}