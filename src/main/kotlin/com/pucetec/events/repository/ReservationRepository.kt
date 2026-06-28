package com.pucetec.events.repositories

import com.pucetec.events.entities.Reservation
import org.springframework.data.jpa.repository.JpaRepository

// Acceso a la tabla reservations en BD
// Cuenta las reservas ACTIVE de un asistente (para validar el límite de 4)
interface ReservationRepository : JpaRepository<Reservation, Long> {
    fun countByAttendeeIdAndStatus(attendeeId: Long, status: String): Int
}