package com.revotech.business.book_flight.repository

import com.revotech.business.book_flight.entity.BookingFlightAttachment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingFlightAttachmentRepository : JpaRepository<BookingFlightAttachment, String> {
    fun findAllByBookingFlightIdAndIsDeletedFalse(bookingFlightId: String): List<BookingFlightAttachment>
    fun findByIdAndIsDeletedFalse(id: String): BookingFlightAttachment?
}