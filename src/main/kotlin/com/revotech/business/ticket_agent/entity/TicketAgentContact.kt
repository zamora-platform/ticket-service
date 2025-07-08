package com.revotech.business.ticket_agent.entity

import com.revotech.audit.JpaActivityInfo
import jakarta.persistence.*

@Entity
@Table(name = "t_ticket_agent_contact")
class TicketAgentContact(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var ticketAgentId: String? = null
) : JpaActivityInfo()