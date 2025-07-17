package com.revotech.business.book_flight.controller

import com.revotech.business.book_flight.dto.SaveBookingFlightReq
import com.revotech.business.book_flight.service.BookingFlightService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/booking-flight")
class BookingFlightController(
    private val bookingFlightService: BookingFlightService
) {
    @PostMapping
    fun bookingFlight(@ModelAttribute @Valid saveBookingFlightReq: SaveBookingFlightReq): Boolean {
        return bookingFlightService.saveBookingFlight(saveBookingFlightReq)
    }
}