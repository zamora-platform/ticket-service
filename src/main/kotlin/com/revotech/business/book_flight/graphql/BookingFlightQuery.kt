package com.revotech.business.book_flight.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.revotech.business.book_flight.service.BookingFlightService

@DgsComponent
class BookingFlightQuery(private val bookingFlightService: BookingFlightService) {
}