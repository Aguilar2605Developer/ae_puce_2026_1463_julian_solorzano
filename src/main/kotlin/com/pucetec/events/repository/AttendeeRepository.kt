package com.pucetec.events.repositories

import com.pucetec.events.entities.Attendee
import org.springframework.data.jpa.repository.JpaRepository

// Acceso a la tabla attendees en BD
interface AttendeeRepository : JpaRepository<Attendee, Long>