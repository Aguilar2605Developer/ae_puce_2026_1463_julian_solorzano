package com.pucetec.events.repositories

import com.pucetec.events.entities.Event
import org.springframework.data.jpa.repository.JpaRepository

// Acceso a la tabla events en BD
interface EventRepository : JpaRepository<Event, Long>