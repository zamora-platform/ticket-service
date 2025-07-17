package com.revotech.business.work_content.repository

import com.revotech.business.airline.dto.AirlineProjection
import com.revotech.business.airline.entity.AirlineStatus
import com.revotech.business.work_content.dto.WorkContentProjection
import com.revotech.business.work_content.entity.WorkContent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface WorkContentRepository : JpaRepository<WorkContent, String> {
    fun existsByCodeAndIsDeletedFalse(code: String): Boolean
    fun findByIdAndIsDeletedFalse(id: String): WorkContent?

    @Query("""
        SELECT wc.id                       AS id,
               wc.code                     AS code,
               wc.content                  AS content,
               wc.time_from                AS timeFrom,
               wc.time_to                  AS timeTo,
               wc.open_ticket_registration AS openTicketRegistration,
               wc.created_by               AS createdBy,
               wc.created_time             AS createdTime
        FROM t_work_content wc
        WHERE (:textSearch IS NULL OR
               LOWER(wc.code) LIKE LOWER(CONCAT('%', :textSearch, '%')))
        AND wc.is_deleted = false
        ORDER BY wc.created_time DESC
    """, nativeQuery = true)
    fun searchWorkContent(
        @Param("textSearch") textSearch: String?,
        pageable: Pageable
    ): Page<WorkContentProjection>

    @Query("""
        SELECT wc.id                       AS id,
               wc.code                     AS code,
               wc.content                  AS content,
               wc.time_from                AS timeFrom,
               wc.time_to                  AS timeTo,
               wc.open_ticket_registration AS openTicketRegistration
        FROM t_work_content wc
        WHERE wc.id = :id
        AND wc.is_deleted = false
    """, nativeQuery = true)
    fun getDetailWorkContentById(
        @Param("id") id: String
    ): WorkContentProjection?

    @Query("""
        SELECT wc.id                       AS id,
               wc.code                     AS code,
               wc.content                  AS content,
               wc.time_from                AS timeFrom,
               wc.time_to                  AS timeTo,
               wc.open_ticket_registration AS openTicketRegistration
        FROM t_work_content wc
        WHERE wc.is_deleted = false
        ORDER BY wc.created_time DESC
    """, nativeQuery = true)
    fun getAllWorkContent(): List<WorkContentProjection>
}