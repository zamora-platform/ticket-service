package com.revotech.business.book_flight.repository

import com.revotech.business.book_flight.entity.BookingFlight
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingFlightRepository : JpaRepository<BookingFlight, String> {
    fun existsByRequestNumber(requestNumber: String): Boolean

    fun findBookingFlightsByIdAndIsDeletedFalse(id: String): BookingFlight?
}