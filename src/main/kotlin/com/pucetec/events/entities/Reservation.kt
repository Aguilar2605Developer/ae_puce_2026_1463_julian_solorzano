package com.pucetec.events.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "reservations")
data class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "attendee_id")
    val attendee: Attendee = Attendee(),

    @ManyToOne
    @JoinColumn(name = "event_id")
    val event: Event = Event(),

    var status: String = "ACTIVE",
    val createdAt: LocalDateTime = LocalDateTime.now()
)