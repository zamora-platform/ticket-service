package com.revotech.business.book_flight.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.revotech.business.book_flight.dto.BookingFlightDetail
import com.revotech.business.book_flight.dto.SearchBookingFlightInput
import com.revotech.business.book_flight.dto.SearchBookingFlightResult
import com.revotech.business.book_flight.service.BookingFlightService
import com.revotech.graphql.GraphqlUtil
import com.revotech.graphql.type.CustomPageable

@DgsComponent
class BookingFlightQuery(private val bookingFlightService: BookingFlightService) {
    @DgsQuery
    fun getDetailBookingFlightById(id: String): BookingFlightDetail {
        return bookingFlightService.getDetailBookingFlightById(id)
    }

    @DgsQuery
    fun searchBookingFlight(
        searchBookingFlightInput: SearchBookingFlightInput,
        pageable: CustomPageable
    ): SearchBookingFlightResult {
        val adjustedPageable = pageable.copy(page = (pageable.page ?: 1).coerceAtLeast(1) - 1)
        val page = GraphqlUtil.toPageable(adjustedPageable)
        return bookingFlightService.searchBookingFlight(searchBookingFlightInput, page)
    }
}