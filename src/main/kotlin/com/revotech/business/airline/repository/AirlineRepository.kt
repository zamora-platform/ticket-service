package com.revotech.business.airline.repository

import com.revotech.business.airline.dto.AirlineProjection
import com.revotech.business.airline.entity.Airline
import com.revotech.business.airline.entity.AirlineStatus
import com.revotech.business.airport.dto.AirportProjection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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

    @Query("""
        SELECT al.id           AS id,
               al.code         AS code,
               al.name         AS name,
               am.id            AS logoFileId,
               am.download_path AS logoFileDownloadPath,
               al.type         AS type,
               al.sort_order   AS sortOrder,
               al.status       AS status,
               al.created_by   AS createdBy,
               al.created_time AS createdTime
        FROM t_airline al
        LEFT JOIN t_attachment am ON al.id = am.object_id AND am.object_type = 'AIRLINE_LOGO' AND am.is_deleted = false
        WHERE (:textSearch IS NULL OR
               LOWER(al.code) LIKE LOWER(CONCAT('%', :textSearch, '%')) OR
               LOWER(al.name) LIKE LOWER(CONCAT('%', :textSearch, '%')))
        AND al.status = 'WORKING'
        ORDER BY al.sort_order ASC, al.created_time DESC
    """, nativeQuery = true)
    fun searchAirline(
        @Param("textSearch") textSearch: String?,
        pageable: Pageable
    ): Page<AirlineProjection>

    @Query("""
        SELECT al.id            AS id,
               al.code          AS code,
               al.name          AS name,
               am.id            AS logoFileId,
               am.download_path AS logoFileDownloadPath,
               al.type          AS type,
               al.sort_order    AS sortOrder,
               al.status        AS status,
               al.created_by    AS createdBy,
               al.created_time  AS createdTime
        FROM t_airline al
        LEFT JOIN t_attachment am ON al.id = am.object_id AND am.object_type = 'AIRLINE_LOGO' AND am.is_deleted = false
        WHERE al.id = :id
        AND al.status = 'WORKING'
    """, nativeQuery = true)
    fun getDetailAirlineById(
        @Param("id") id: String
    ): AirlineProjection?

    @Query("""
        SELECT al.id           AS id,
               al.code         AS code,
               al.name         AS name,
               al.type         AS type,
               al.sort_order   AS sortOrder,
               al.status       AS status,
               al.created_by   AS createdBy,
               al.created_time AS createdTime
        FROM t_airline al
        WHERE al.status = 'WORKING'
        ORDER BY al.sort_order ASC, al.created_time DESC
    """, nativeQuery = true)
    fun getAllAirline(): List<AirlineProjection>
}