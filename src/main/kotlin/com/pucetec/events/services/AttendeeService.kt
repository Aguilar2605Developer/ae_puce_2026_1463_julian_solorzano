package com.pucetec.events.services

import com.pucetec.events.dto.AttendeeRequest
import com.pucetec.events.dto.AttendeeResponse
import com.pucetec.events.exceptions.BlankFieldException
import com.pucetec.events.mappers.toAttendee
import com.pucetec.events.mappers.toResponse
import com.pucetec.events.repositories.AttendeeRepository
import org.springframework.stereotype.Service

@Service
class AttendeeService(private val attendeeRepository: AttendeeRepository) {

    // Devuelve todos los asistentes
    fun getAllAttendees(): List<AttendeeResponse> =
        attendeeRepository.findAll().map { it.toResponse() }

    // Crea un asistente validando que name y email no estén en blanco
    fun createAttendee(request: AttendeeRequest): AttendeeResponse {
        if (request.name.isBlank() || request.email.isBlank()) {
            throw BlankFieldException("Name and email cannot be blank")
        }
        return attendeeRepository.save(request.toAttendee()).toResponse()
    }
}