package com.pucetec.events.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

// Objeto que se devuelve en el cuerpo de cada error HTTP
data class ExceptionResponse(val message: String, val source: String)

// Intercepta todas las excepciones lanzadas desde cualquier controller
@RestControllerAdvice
class GlobalExceptionHandler {

    // Campos vacíos → 400 Bad Request
    @ExceptionHandler(BlankFieldException::class)
    fun handleBlankField(ex: BlankFieldException): ResponseEntity<ExceptionResponse> =
        ResponseEntity(ExceptionResponse(ex.message ?: "Blank field", "BlankFieldException"), HttpStatus.BAD_REQUEST)

    // Capacidad inválida (tickets < 1) → 400 Bad Request
    @ExceptionHandler(InvalidCapacityException::class)
    fun handleInvalidCapacity(ex: InvalidCapacityException): ResponseEntity<ExceptionResponse> =
        ResponseEntity(ExceptionResponse(ex.message ?: "Invalid capacity", "InvalidCapacityException"), HttpStatus.BAD_REQUEST)

    // Asistente no encontrado → 404 Not Found
    @ExceptionHandler(AttendeeNotFoundException::class)
    fun handleAttendeeNotFound(ex: AttendeeNotFoundException): ResponseEntity<ExceptionResponse> =
        ResponseEntity(ExceptionResponse(ex.message ?: "Attendee not found", "AttendeeNotFoundException"), HttpStatus.NOT_FOUND)

    // Evento no encontrado → 404 Not Found
    @ExceptionHandler(EventNotFoundException::class)
    fun handleEventNotFound(ex: EventNotFoundException): ResponseEntity<ExceptionResponse> =
        ResponseEntity(ExceptionResponse(ex.message ?: "Event not found", "EventNotFoundException"), HttpStatus.NOT_FOUND)

    // Reserva no encontrada → 404 Not Found
    @ExceptionHandler(ReservationNotFoundException::class)
    fun handleReservationNotFound(ex: ReservationNotFoundException): ResponseEntity<ExceptionResponse> =
        ResponseEntity(ExceptionResponse(ex.message ?: "Reservation not found", "ReservationNotFoundException"), HttpStatus.NOT_FOUND)

    // Evento sin tickets disponibles → 409 Conflict
    @ExceptionHandler(SoldOutException::class)
    fun handleSoldOut(ex: SoldOutException): ResponseEntity<ExceptionResponse> =
        ResponseEntity(ExceptionResponse(ex.message ?: "Event sold out", "SoldOutException"), HttpStatus.CONFLICT)

    // Asistente ya tiene 4 reservas activas → 409 Conflict
    @ExceptionHandler(ReservationLimitExceededException::class)
    fun handleReservationLimitExceeded(ex: ReservationLimitExceededException): ResponseEntity<ExceptionResponse> =
        ResponseEntity(ExceptionResponse(ex.message ?: "Reservation limit exceeded", "ReservationLimitExceededException"), HttpStatus.CONFLICT)

    // Reserva ya fue cancelada previamente → 409 Conflict
    @ExceptionHandler(ReservationAlreadyCancelledException::class)
    fun handleReservationAlreadyCancelled(ex: ReservationAlreadyCancelledException): ResponseEntity<ExceptionResponse> =
        ResponseEntity(ExceptionResponse(ex.message ?: "Reservation already cancelled", "ReservationAlreadyCancelledException"), HttpStatus.CONFLICT)
}