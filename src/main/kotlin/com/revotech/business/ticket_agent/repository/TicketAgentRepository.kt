package com.revotech.business.ticket_agent.repository

import com.revotech.business.ticket_agent.dto.TicketAgentListProjection
import com.revotech.business.ticket_agent.entity.TicketAgent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TicketAgentRepository : JpaRepository<TicketAgent, String> {
    @Query("""
        SELECT tta.*
        FROM t_ticket_agent tta
        WHERE tta.id = :id
        AND tta.status = 'WORKING'
    """, nativeQuery = true)
    fun findTicketAgentById(id: String): TicketAgent?

    fun existsByCode(code: String): Boolean
    fun existsByName(name: String): Boolean
    fun existsByTaxCode(taxCode: String): Boolean
    fun existsByPhone(phone: String): Boolean
    fun existsByEmail(email: String): Boolean

    @Query(
        """
            SELECT ta.id           AS id,
                   ta.code         AS code,
                   ta.name         AS name,
                   ta.adddress     AS address,
                   ta.tax_code     AS taxCode,
                   ta.phone        AS phone,
                   ta.email        AS email,
                   ta.note         AS note,
                   ta.status       AS status,
                   ta.created_by   AS createdBy,
                   ta.created_time AS createdTime
            FROM t_ticket_agent ta
            WHERE (:textSearch IS NULL OR
                   LOWER(ta.code) LIKE LOWER(CONCAT('%', :textSearch, '%')) OR
                   LOWER(ta.name) LIKE LOWER(CONCAT('%', :textSearch, '%')) OR
                   LOWER(ta.email) LIKE LOWER(CONCAT('%', :textSearch, '%')))
            AND ta.status = 'WORKING'
            ORDER BY ta.created_time DESC
        """, nativeQuery = true)
    fun searchTicketAgent(
        @Param("textSearch") textSearch: String?,
        pageable: Pageable
    ): Page<TicketAgentListProjection>

    @Query(
        """
            SELECT ta.id           AS id,
                   ta.code         AS code,
                   ta.name         AS name,
                   ta.adddress     AS address,
                   ta.tax_code     AS taxCode,
                   ta.phone        AS phone,
                   ta.email        AS email,
                   ta.note         AS note,
                   ta.status       AS status,
                   ta.created_by   AS createdBy,
                   ta.created_time AS createdTime
            FROM t_ticket_agent ta
            WHERE ta.id = :id
            AND ta.status = 'WORKING'
        """, nativeQuery = true)
    fun getDetailTicketById(
        @Param("id") id: String,
    ): TicketAgentListProjection?
}