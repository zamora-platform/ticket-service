package com.revotech.business.book_flight.repository

import com.revotech.business.book_flight.entity.BookingFlight
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingFlightRepository : JpaRepository<BookingFlight, String> {
    fun existsByRequestNumberAndIsDeletedFalse(requestNumber: String): Boolean
    fun existsByWorkContentIdAndIsDeletedFalse(workContentId: String): Boolean
    fun existsByCityIdAndIsDeletedFalse(cityId: String): Boolean
    fun existsByDepartureAirportIdAndIsDeletedFalse(airportId: String): Boolean
    fun existsByAirportToDepartureIdAndIsDeletedFalse(airportId: String): Boolean
    fun existsByAirportDepartureReturnIdAndIsDeletedFalse(airportId: String): Boolean
    fun existsByAirportToReturnIdAndIsDeletedFalse(airportId: String): Boolean
    fun existsByAirlineDepartureIdAndIsDeletedFalse(airlineId: String): Boolean
    fun existsByAirlineReturnId(airlineId: String): Boolean
    fun findBookingFlightsByIdAndIsDeletedFalse(id: String): BookingFlight?
}