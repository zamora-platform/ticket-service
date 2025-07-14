package com.revotech.business.book_flight.dto

import com.revotech.business.book_flight.entity.FlightType
import com.revotech.business.book_flight.entity.RequestType
import org.springframework.web.multipart.MultipartFile

data class SaveBookingFlightReq(
    var id: String? = null,
    var requestNumber: String? = null,
    var createdDate: String? = null,
    var officerId: String? = null,
    var goldenLotusCode: String? = null,
    var workContentId: String? = null,
    var flightType: FlightType? = null,
    var cityId: String? = null,
    var flightDate: String? = null,
    var departureAirportId: String? = null,
    var airportToDepartureId: String? = null,
    var returnFlightDate: String? = null,
    var airportDepartureReturnId: String? = null,
    var airportToReturnId: String? = null,
    var requestType: RequestType? = null,
    var departureTime: String? = null,
    var outboundFlightNumber: String? = null,
    var airlineDepartureId: String? = null,
    var returnFlightTime: String? = null,
    var returnFlightNumber: String? = null,
    var airlineReturnId: String? = null,
    var flightScheduleDescription: String? = null,
    var files: List<BookingFlightAttachmentDto>? = null,
)

data class BookingFlightAttachmentDto(
    var id: String? = null,
    var quote: String? = null,
    var attachment: MultipartFile? = null,
)