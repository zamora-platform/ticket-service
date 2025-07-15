package com.revotech.business.book_flight.repository

import com.revotech.business.book_flight.dto.BookingFlightDetailProjection
import com.revotech.business.book_flight.entity.BookingFlight
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
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

    @Query("""
        SELECT
               -- ID yêu cầu đặt vé máy bay
               bf.id                          AS id,
        
               -- Số phiếu
               bf.request_number              AS requestNumber,
               
               -- Ngày lập
               bf.created_time                AS createdDate,
        
               -- Cán bộ
               bf.officer_id                  AS officerId,
        
               -- Mã bông sen vàng
               bf.golden_lotus_code           AS goldenLotusCode,
        
               -- Nội dung làm việc (id, code, content)
               wc.id                          AS workContentId,
               wc.code                        AS workContentCode,
               wc.content                     AS workContentContent,
        
               -- Loại chuyến bay
               bf.flight_type                 AS flightType,
        
               -- Thành phố (id, name)
               c.id                           AS cityId,
               c.name                         AS cityName,
        
               -- Ngày bay đi
               bf.flight_date                 AS flightDate,
               -- Sân bay khởi hành chiều đi (id, name)
               bf.departure_airport_id        AS departureAirportId,
               a_departure.name               AS departureAirportName,
               -- Sân bay đến chiều đi (id, name)
               bf.airport_to_departure_id     AS airportToDepartureId,
               a_to_departure.name            AS airportToDepartureName,
        
               -- Ngày bay về
               bf.return_flight_date          AS returnFlightDate,
               -- Sân bay khởi hành chiều về (id, name)
               bf.airport_departure_return_id AS airportDepartureReturnId,
               a_return_departure.name        AS airportDepartureReturnName,
               -- Sân bay đến chiều về (id, name)
               bf.airport_to_return_id        AS airportToReturnId,
               a_return_arrival.name          AS airportToReturnName,
        
               -- Loại yêu cầu
               bf.request_type                as requestType,
        
               -- Giờ bay chiều đi
               bf.departure_time              as departureTime,
               -- Số hiệu chuyến bay chiều đi
               bf.outbound_flight_number      as outboundFlightNumber,
               -- Hãng hàng không chiều đi (id, name)
               bf.airline_departure_id        as airlineDepartureId,
               airline_departure.name         as airlineDepartureName,
        
               -- Giờ bay chiều về
               bf.return_flight_time          as returnFlightTime,
               -- Số hiệu chuyến bay chiều về
               bf.return_flight_number        as returnFlightNumber,
               -- Hãng hàng không chiều về (id, name)
               bf.airline_return_id           as airlineReturnId,
               airline_return.name            as airlineReturnName,
        
               -- Mô tả khung giờ bay
               bf.flight_schedule_description as flightScheduleDescription
        FROM t_booking_flight bf
                 LEFT JOIN t_work_content wc ON bf.work_content_id = wc.id
                 LEFT JOIN t_city c ON bf.city_id = c.id
                 LEFT JOIN t_airport a_departure ON bf.departure_airport_id = a_departure.id
                 LEFT JOIN t_airport a_to_departure ON bf.airport_to_departure_id = a_to_departure.id
                 LEFT JOIN t_airport a_return_departure ON bf.airport_departure_return_id = a_return_departure.id
                 LEFT JOIN t_airport a_return_arrival ON bf.airport_to_return_id = a_return_arrival.id
                 LEFT JOIN t_airline airline_departure ON bf.airline_departure_id = airline_departure.id
                 LEFT JOIN t_airline airline_return ON bf.airline_return_id = airline_return.id
        WHERE bf.id = :id
        AND bf.is_deleted = false
    """, nativeQuery = true)
    fun getDetailBookingFlightById(
        @Param("id") id: String
    ): BookingFlightDetailProjection?

    @Query("""
        SELECT
               -- ID yêu cầu đặt vé máy bay
               bf.id                          AS id,
        
               -- Số phiếu
               bf.request_number              AS requestNumber,
               
               -- Ngày lập
               bf.created_time                AS createdDate,
        
               -- Cán bộ
               bf.officer_id                  AS officerId,
        
               -- Mã bông sen vàng
               bf.golden_lotus_code           AS goldenLotusCode,
        
               -- Nội dung làm việc (id, code, content)
               wc.id                          AS workContentId,
               wc.code                        AS workContentCode,
               wc.content                     AS workContentContent,
        
               -- Loại chuyến bay
               bf.flight_type                 AS flightType,
        
               -- Thành phố (id, name)
               c.id                           AS cityId,
               c.name                         AS cityName,
        
               -- Ngày bay đi
               bf.flight_date                 AS flightDate,
               -- Sân bay khởi hành chiều đi (id, name)
               bf.departure_airport_id        AS departureAirportId,
               a_departure.name               AS departureAirportName,
               -- Sân bay đến chiều đi (id, name)
               bf.airport_to_departure_id     AS airportToDepartureId,
               a_to_departure.name            AS airportToDepartureName,
        
               -- Ngày bay về
               bf.return_flight_date          AS returnFlightDate,
               -- Sân bay khởi hành chiều về (id, name)
               bf.airport_departure_return_id AS airportDepartureReturnId,
               a_return_departure.name        AS airportDepartureReturnName,
               -- Sân bay đến chiều về (id, name)
               bf.airport_to_return_id        AS airportToReturnId,
               a_return_arrival.name          AS airportToReturnName,
        
               -- Loại yêu cầu
               bf.request_type                as requestType,
        
               -- Giờ bay chiều đi
               bf.departure_time              as departureTime,
               -- Số hiệu chuyến bay chiều đi
               bf.outbound_flight_number      as outboundFlightNumber,
               -- Hãng hàng không chiều đi (id, name)
               bf.airline_departure_id        as airlineDepartureId,
               airline_departure.name         as airlineDepartureName,
        
               -- Giờ bay chiều về
               bf.return_flight_time          as returnFlightTime,
               -- Số hiệu chuyến bay chiều về
               bf.return_flight_number        as returnFlightNumber,
               -- Hãng hàng không chiều về (id, name)
               bf.airline_return_id           as airlineReturnId,
               airline_return.name            as airlineReturnName,
        
               -- Mô tả khung giờ bay
               bf.flight_schedule_description as flightScheduleDescription
        FROM t_booking_flight bf
                 LEFT JOIN t_work_content wc ON bf.work_content_id = wc.id
                 LEFT JOIN t_city c ON bf.city_id = c.id
                 LEFT JOIN t_airport a_departure ON bf.departure_airport_id = a_departure.id
                 LEFT JOIN t_airport a_to_departure ON bf.airport_to_departure_id = a_to_departure.id
                 LEFT JOIN t_airport a_return_departure ON bf.airport_departure_return_id = a_return_departure.id
                 LEFT JOIN t_airport a_return_arrival ON bf.airport_to_return_id = a_return_arrival.id
                 LEFT JOIN t_airline airline_departure ON bf.airline_departure_id = airline_departure.id
                 LEFT JOIN t_airline airline_return ON bf.airline_return_id = airline_return.id
        WHERE (:textSearch IS NULL OR
               LOWER(bf.request_number) LIKE LOWER(CONCAT('%', :textSearch, '%')))
        AND bf.is_deleted = false
    """, nativeQuery = true)
    fun searchBookingFlight(
        @Param("textSearch") textSearch: String?,
        pageable: Pageable
    ): Page<BookingFlightDetailProjection>
}