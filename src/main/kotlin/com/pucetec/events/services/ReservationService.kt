package com.pucetec.events.services

import com.pucetec.events.dto.ReservationRequest
import com.pucetec.events.dto.ReservationResponse
import com.pucetec.events.entities.Reservation
import com.pucetec.events.exceptions.*
import com.pucetec.events.mappers.toResponse
import com.pucetec.events.repositories.AttendeeRepository
import com.pucetec.events.repositories.EventRepository
import com.pucetec.events.repositories.ReservationRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val attendeeRepository: AttendeeRepository,
    private val eventRepository: EventRepository
) {

    // Devuelve todas las reservas
    fun getAllReservations(): List<ReservationResponse> =
        reservationRepository.findAll().map { it.toResponse() }

    // Crea una reserva con todas las validaciones de negocio
    fun createReservation(request: ReservationRequest): ReservationResponse {
        // 1. El asistente debe existir
        val attendee = attendeeRepository.findById(request.attendeeId)
            .orElseThrow { AttendeeNotFoundException("Attendee with id ${request.attendeeId} not found") }

        // 2. El evento debe existir
        val event = eventRepository.findById(request.eventId)
            .orElseThrow { EventNotFoundException("Event with id ${request.eventId} not found") }

        // 3. El evento debe tener tickets disponibles
        if (event.availableTickets <= 0) {
            throw SoldOutException("Event ${event.name} is sold out")
        }

        // 4. El asistente no puede tener más de 4 reservas activas
        val activeReservations = reservationRepository.countByAttendeeIdAndStatus(request.attendeeId, "ACTIVE")
        if (activeReservations >= 4) {
            throw ReservationLimitExceededException("Attendee ${attendee.name} has reached the reservation limit of 4")
        }

        // 5. Crear la reserva, decrementar tickets y guardar
        event.availableTickets--
        eventRepository.save(event)

        val reservation = Reservation(
            attendee = attendee,
            event = event,
            status = "ACTIVE",
            createdAt = LocalDateTime.now()
        )
        return reservationRepository.save(reservation).toResponse()
    }

    // Cancela una reserva validando que exista y esté activa
    fun cancelReservation(reservationId: Long): ReservationResponse {
        // 1. La reserva debe existir
        val reservation = reservationRepository.findById(reservationId)
            .orElseThrow { ReservationNotFoundException("Reservation with id $reservationId not found") }

        // 2. La reserva debe estar ACTIVE
        if (reservation.status == "CANCELLED") {
            throw ReservationAlreadyCancelledException("Reservation with id $reservationId is already cancelled")
        }

        // 3. Cancelar y devolver el ticket al evento
        reservation.status = "CANCELLED"
        reservation.event.availableTickets++
        eventRepository.save(reservation.event)

        return reservationRepository.save(reservation).toResponse()
    }
}