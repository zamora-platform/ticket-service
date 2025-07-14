package com.revotech.business.book_flight.service

import com.revotech.business.attachment.service.TicketAttachmentService
import com.revotech.business.book_flight.repository.BookingFlightAttachmentRepository
import com.revotech.business.book_flight.repository.BookingFlightRepository
import com.revotech.util.WebUtil
import org.springframework.stereotype.Service

@Service
class BookingFlightService(
    private val bookingFlightRepository: BookingFlightRepository,
    private val bookingFlightAttachmentRepository: BookingFlightAttachmentRepository,
    private val ticketAttachmentService: TicketAttachmentService,
    private val webUtil: WebUtil
) {
}