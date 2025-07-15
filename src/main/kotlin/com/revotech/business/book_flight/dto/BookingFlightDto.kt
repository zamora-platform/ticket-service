package com.revotech.business.book_flight.dto

import com.revotech.business.book_flight.entity.FlightType
import com.revotech.business.book_flight.entity.RequestType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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

data class BookingFlightDetail(
    var id: String? = null,
    var requestNumber: String? = null,
    var createdDate: LocalDate? = null,
    var officerId: String? = null,
    var goldenLotusCode: String? = null,
    var workContentId: String? = null,
    var workContentCode: String? = null,
    var workContentContent: String? = null,
    var flightType: FlightType? = null,
    var cityId: String? = null,
    var cityName: String? = null,
    var flightDate: LocalDate? = null,
    var departureAirportId: String? = null,
    var departureAirportName: String? = null,
    var airportToDepartureId: String? = null,
    var airportToDepartureName: String? = null,
    var returnFlightDate: LocalDate? = null,
    var airportDepartureReturnId: String? = null,
    var airportDepartureReturnName: String? = null,
    var airportToReturnId: String? = null,
    var airportToReturnName: String? = null,
    var requestType: RequestType? = null,
    var departureTime: LocalTime? = null,
    var outboundFlightNumber: String? = null,
    var airlineDepartureId: String? = null,
    var airlineDepartureName: String? = null,
    var returnFlightTime: LocalTime? = null,
    var returnFlightNumber: String? = null,
    var airlineReturnId: String? = null,
    var airlineReturnName: String? = null,
    var flightScheduleDescription: String? = null,
    var attachments: List<BookingFlightAttachmentsDetail>? = null,
)

data class BookingFlightAttachmentsDetail(
    var id: String? = null,
    var quote: String? = null,
    var attachment: String? = null,
    var size: String? = null,
    var downloadPath: String? = null,
)

interface BookingFlightDetailProjection {
    fun getId(): String?
    fun getRequestNumber(): String?
    fun getCreatedDate(): LocalDateTime?
    fun getOfficerId(): String?
    fun getGoldenLotusCode(): String?
    fun getWorkContentId(): String?
    fun getWorkContentCode(): String?
    fun getWorkContentContent(): String?
    fun getFlightType(): FlightType?
    fun getCityId(): String?
    fun getCityName(): String?
    fun getFlightDate(): LocalDate?
    fun getDepartureAirportId(): String?
    fun getDepartureAirportName(): String?
    fun getAirportToDepartureId(): String?
    fun getAirportToDepartureName(): String?
    fun getReturnFlightDate(): LocalDate?
    fun getAirportDepartureReturnId(): String?
    fun getAirportDepartureReturnName(): String?
    fun getAirportToReturnId(): String?
    fun getAirportToReturnName(): String?
    fun getRequestType(): RequestType?
    fun getDepartureTime(): LocalTime?
    fun getOutboundFlightNumber(): String?
    fun getAirlineDepartureId(): String?
    fun getAirlineDepartureName(): String?
    fun getReturnFlightTime(): LocalTime?
    fun getReturnFlightNumber(): String?
    fun getAirlineReturnId(): String?
    fun getAirlineReturnName(): String?
    fun getFlightScheduleDescription(): String?
}

data class BookingFlightList(
    var id: String? = null,
    var requestNumber: String? = null,
    var createdDate: LocalDate? = null,
    var officerId: String? = null,
    var officerName: String? = null,
    var goldenLotusCode: String? = null,
    var workContentId: String? = null,
    var workContentCode: String? = null,
    var workContentContent: String? = null,
    var flightType: FlightType? = null,
    var cityId: String? = null,
    var cityName: String? = null,
    var flightDate: LocalDate? = null,
    var departureAirportId: String? = null,
    var departureAirportName: String? = null,
    var airportToDepartureId: String? = null,
    var airportToDepartureName: String? = null,
    var returnFlightDate: LocalDate? = null,
    var airportDepartureReturnId: String? = null,
    var airportDepartureReturnName: String? = null,
    var airportToReturnId: String? = null,
    var airportToReturnName: String? = null,
    var requestType: RequestType? = null,
    var departureTime: LocalTime? = null,
    var outboundFlightNumber: String? = null,
    var airlineDepartureId: String? = null,
    var airlineDepartureName: String? = null,
    var returnFlightTime: LocalTime? = null,
    var returnFlightNumber: String? = null,
    var airlineReturnId: String? = null,
    var airlineReturnName: String? = null,
    var flightScheduleDescription: String? = null
)

data class SearchBookingFlightInput(
    val textSearch: String? = null
)

data class SearchBookingFlightResult(
    val content: List<BookingFlightList>,
    val page: Int,
    val pageSize: Int,
    val totalRecords: Int,
    val totalPages: Int
)