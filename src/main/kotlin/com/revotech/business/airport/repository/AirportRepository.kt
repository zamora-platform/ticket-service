package com.revotech.business.airport.repository

import com.revotech.business.airport.dto.AirportDetailProjection
import com.revotech.business.airport.dto.AirportProjection
import com.revotech.business.airport.entity.Airport
import com.revotech.business.airport.entity.AirportStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AirportRepository : JpaRepository<Airport, String> {
    @Query(
        "SELECT a " +
                "FROM Airport a " +
                "WHERE a.id = :id " +
                "AND a.status = 'WORKING'"
    )
    fun findAirportById(@Param("id") id: String): Airport?

    fun existsByCodeAndStatus(code: String, status: AirportStatus): Boolean
    fun existsByNameAndStatus(name: String, status: AirportStatus): Boolean

    @Query(
        """
            SELECT COUNT(id) + 1 
            FROM t_airport
            WHERE status = 'WORKING'
        """, nativeQuery = true
    )
    fun getNextAirportSortOrder(): Int

    @Query(
        """
            SELECT a.id           AS id,
                   a.code         AS code,
                   a.name         AS name,
                   c.name         AS countryName,
                   ct.name        AS cityName,
                   a.status       AS status,
                   a.sort_order   AS sortOrder,
                   a.is_default   AS isDefault,
                   a.created_by   AS createdBy,
                   a.created_time AS createdTime
            FROM t_airport a
            LEFT JOIN t_country c ON a.country_id = c.id
            LEFT JOIN t_city ct ON a.city_id = ct.id
            WHERE (:textSearch IS NULL OR
                   LOWER(a.code) LIKE LOWER(CONCAT('%', :textSearch, '%')) OR
                   LOWER(a.name) LIKE LOWER(CONCAT('%', :textSearch, '%')))
            AND a.status = 'WORKING'
            ORDER BY a.is_default DESC, a.sort_order ASC, a.created_time DESC
        """, nativeQuery = true
    )
    fun searchAirport(
        @Param("textSearch") textSearch: String?,
        pageable: Pageable
    ): Page<AirportProjection>

    @Query(
        """
            SELECT a.id           AS id,
                   a.code         AS code,
                   a.name         AS name,
                   c.id           AS countryId,
                   c.name         AS countryName,
                   ct.id          AS cityId,
                   ct.name        AS cityName,
                   a.status       AS status,
                   a.sort_order   AS sortOrder,
                   a.is_default   AS isDefault,
                   a.created_by   AS createdBy,
                   a.created_time AS createdTime
            FROM t_airport a
            LEFT JOIN t_country c ON a.country_id = c.id
            LEFT JOIN t_city ct ON a.city_id = ct.id
            WHERE a.id = :id
            AND a.status = 'WORKING'
        """, nativeQuery = true
    )
    fun getDetailAirportById(
        @Param("id") id: String,
    ): AirportDetailProjection?

    @Modifying
    @Query("UPDATE Airport c SET c.isDefault = false WHERE c.isDefault = true")
    fun unsetAirportIsDefaultTrueToFalse(): Int

    fun findByCountryIdAndStatus(countryId: String, status: AirportStatus): Airport?
}