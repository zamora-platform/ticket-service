package com.revotech.business.book_flight.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.revotech.business.book_flight.service.BookingFlightService

@DgsComponent
class BookingFlightMutation(private val bookingFlightService: BookingFlightService) {
    @DgsMutation
    fun deleteBookingFlight(id: String): Boolean {
        return bookingFlightService.deleteBookingFlight(id)
    }
}