package com.pucetec.events.entities

import jakarta.persistence.*

@Entity
@Table(name = "events")
data class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String = "",
    val venue: String = "",
    val totalTickets: Int = 0,
    var availableTickets: Int = 0
)