package com.revotech.business.book_flight.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.revotech.business.book_flight.dto.BookingFlightDetail
import com.revotech.business.book_flight.service.BookingFlightService

@DgsComponent
class BookingFlightQuery(private val bookingFlightService: BookingFlightService) {
    @DgsQuery
    fun getDetailBookingFlightById(id: String): BookingFlightDetail {
        return bookingFlightService.getDetailBookingFlightById(id)
    }
}