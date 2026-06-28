package com.pucetec.events.entities

import jakarta.persistence.*

@Entity
@Table(name = "attendees")
data class Attendee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String = "",
    val email: String = ""
)