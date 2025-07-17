package com.revotech.business.book_flight.entity

import com.revotech.audit.JpaActivityInfo
import jakarta.persistence.*

@Entity
@Table(name = "t_booking_flight_attachment")
class BookingFlightAttachment(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    var bookingFlightId: String? = null,
    var quote: String? = null,
    var attachment: String? = null,
    var isDeleted: Boolean = false,
) : JpaActivityInfo()