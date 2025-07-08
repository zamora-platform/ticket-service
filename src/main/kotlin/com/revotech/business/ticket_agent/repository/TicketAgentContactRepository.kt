package com.revotech.business.ticket_agent.repository

import com.revotech.business.ticket_agent.dto.TicketAgentContactProjection
import com.revotech.business.ticket_agent.entity.TicketAgentContact
import org.springframework.data.jpa.repository.JpaRepository
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
        """, nativeQuery = true)
    fun findContactsByAgentIds(
        @Param("ids") ids: List<String>
    ): List<TicketAgentContactProjection>
}