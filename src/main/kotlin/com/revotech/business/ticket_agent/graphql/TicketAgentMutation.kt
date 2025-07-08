package com.revotech.business.ticket_agent.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.revotech.business.ticket_agent.dto.SaveTicketAgentReq
import com.revotech.business.ticket_agent.service.TicketAgentService

@DgsComponent
class TicketAgentMutation(private val ticketAgentService: TicketAgentService) {
    @DgsMutation
    fun saveTicketAgent(saveTicketAgentReq: SaveTicketAgentReq): Boolean {
        return ticketAgentService.saveTicketAgent(saveTicketAgentReq)
    }
}