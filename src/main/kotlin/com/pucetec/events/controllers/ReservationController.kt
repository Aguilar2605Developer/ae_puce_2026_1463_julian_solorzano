package com.pucetec.events.controllers

import com.pucetec.events.dto.ReservationRequest
import com.pucetec.events.dto.ReservationResponse
import com.pucetec.events.services.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

// El controller solo recibe el request, llama al service y devuelve el response
@RestController
@RequestMapping("/api/reservations")
class ReservationController(private val reservationService: ReservationService) {

    // GET /api/reservations → privado, lista todas las reservas
    @GetMapping
    fun getAllReservations(): ResponseEntity<List<ReservationResponse>> =
        ResponseEntity.ok(reservationService.getAllReservations())

    // POST /api/reservations → privado, crea una reserva
    @PostMapping
    fun createReservation(@RequestBody request: ReservationRequest): ResponseEntity<ReservationResponse> =
        ResponseEntity(reservationService.createReservation(request), HttpStatus.CREATED)

    // PUT /api/reservations/{id}/cancel → privado, cancela una reserva
    @PutMapping("/{id}/cancel")
    fun cancelReservation(@PathVariable id: Long): ResponseEntity<ReservationResponse> =
        ResponseEntity.ok(reservationService.cancelReservation(id))
}