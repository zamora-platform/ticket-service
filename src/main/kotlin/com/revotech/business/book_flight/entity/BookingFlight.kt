package com.revotech.business.book_flight.entity

import com.revotech.audit.JpaActivityInfo
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "t_booking_flight")
class BookingFlight(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    var requestNumber: String? = null,
    var officerId: String? = null,
    var goldenLotusCode: String? = null,
    var workContentId: String? = null,
    @Enumerated(EnumType.STRING)
    var flightType: FlightType? = null,
    var cityId: String? = null,
    var flightDate: LocalDate? = null,
    var departureAirportId: String? = null,
    var airportToDepartureId: String? = null,
    var returnFlightDate: LocalDate? = null,
    var airportDepartureReturnId: String? = null,
    var airportToReturnId: String? = null,
    @Enumerated(EnumType.STRING)
    var requestType: RequestType? = null,
    var departureTime: LocalTime? = null,
    var outboundFlightNumber: String? = null,
    var airlineDepartureId: String? = null,
    var returnFlightTime: LocalTime? = null,
    var returnFlightNumber: String? = null,
    var airlineReturnId: String? = null,
    var flightScheduleDescription: String? = null,
    @Enumerated(EnumType.STRING)
    var status: BookingFlightStatus? = null,
    var isDeleted: Boolean? = null
) : JpaActivityInfo()

enum class FlightType {
    INTERNATIONAL,
    DOMESTIC
}

enum class RequestType {
    BOOK_SPECIFIC_FLIGHT,
    AGENT_CHOOSE_FLIGHT
}

enum class BookingFlightStatus {
    DRAFT,
    WAITING_FOR_APPROVAL,
    APPROVED,
    COMPLETED
}