package com.revotech.business.airline.repository

import com.revotech.business.airline.entity.Airline
import com.revotech.business.airline.entity.AirlineStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AirlineRepository : JpaRepository<Airline, String> {
    @Query("SELECT al " +
            "FROM Airline al " +
            "WHERE al.id = :id " +
            "AND al.status = 'WORKING' ")
    fun findAirlineById(@Param("id") id: String): Airline?

    fun existsByCodeAndStatus(code: String, status: AirlineStatus): Boolean
    fun existsByNameAndStatus(name: String, status: AirlineStatus): Boolean

    @Query(
        """
            SELECT COUNT(id) + 1 
            FROM t_airline
            WHERE status = 'WORKING'
        """, nativeQuery = true
    )
    fun getNextAirlineSortOrder(): Int
}