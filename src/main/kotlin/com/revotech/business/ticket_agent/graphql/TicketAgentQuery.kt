package com.revotech.business.ticket_agent.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.revotech.business.ticket_agent.dto.SearchInput
import com.revotech.business.ticket_agent.dto.SearchTicketAgentResult
import com.revotech.business.ticket_agent.service.TicketAgentService
import com.revotech.graphql.GraphqlUtil
import com.revotech.graphql.type.CustomPageable

@DgsComponent
class TicketAgentQuery(private val ticketAgentService: TicketAgentService) {
    @DgsQuery
    fun searchTicketAgent(searchInput: SearchInput, pageable: CustomPageable): SearchTicketAgentResult {
        val adjustedPageable = pageable.copy(page = (pageable.page ?: 1).coerceAtLeast(1) - 1)
        val page = GraphqlUtil.toPageable(adjustedPageable)
        return ticketAgentService.searchTicketAgent(searchInput, page)
    }
}