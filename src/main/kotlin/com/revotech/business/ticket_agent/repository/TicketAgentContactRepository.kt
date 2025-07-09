package com.revotech.business.ticket_agent.repository

import com.revotech.business.ticket_agent.dto.TicketAgentContactProjection
import com.revotech.business.ticket_agent.entity.TicketAgentContact
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TicketAgentContactRepository : JpaRepository<TicketAgentContact, String> {
    fun deleteAllByTicketAgentId(ticketAgentId: String)

    @Query(
        """
            SELECT tac.ticket_agent_id AS ticketAgentId,
                   tac.name            AS name,
                   tac.email           AS email,
                   tac.phone           AS phone
            FROM t_ticket_agent_contact tac
            WHERE tac.ticket_agent_id IN :ids
            AND tac.status = 'WORKING'
        """, nativeQuery = true)
    fun findContactsByAgentIds(
        @Param("ids") ids: List<String>
    ): List<TicketAgentContactProjection>

    @Modifying
    @Query(
        """
            UPDATE t_ticket_agent_contact tac
            SET status = 'NOT_WORKING'
            WHERE tac.ticket_agent_id IN :agentIds
        """, nativeQuery = true
    )
    fun softDeleteTicketAgentContactByAgentIds(
        @Param("agentIds") agentIds: List<String>,
    ): Int

    @Query("SELECT tac " +
            "FROM TicketAgentContact tac " +
            "WHERE tac.id = :ticketAgentContactId " +
            "AND tac.status = 'WORKING' ")
    fun findTicketAgentContactById(ticketAgentContactId: String): TicketAgentContact?

    @Modifying
    @Query(
        """
            UPDATE TicketAgentContact tac
            SET tac.status = 'NOT_WORKING'
            WHERE tac.id IN :ticketAgentContactId
        """, nativeQuery = true
    )
    fun softDeleteTicketAgentContactById(
        @Param("ticketAgentContactId") ticketAgentContactId: List<String>,
    ): Boolean
}