package com.revotech.business.book_flight.controller

import com.revotech.business.book_flight.service.BookingFlightService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/booking-flight")
class BookingFlightController(
    private val bookingFlightService: BookingFlightService
) {
}