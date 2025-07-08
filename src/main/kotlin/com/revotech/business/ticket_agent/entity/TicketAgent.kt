package com.revotech.business.ticket_agent.entity

import com.revotech.audit.JpaActivityInfo
import jakarta.persistence.*

@Entity
@Table(name = "t_ticket_agent")
class TicketAgent (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    var code: String? = null,
    var name: String? = null,
    var adddress: String? = null,
    var taxCode: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var note: String? = null,
    @Enumerated(EnumType.STRING)
    var status: TicketAgentStatus? = null
) : JpaActivityInfo()

enum class TicketAgentStatus {
    WORKING, NOT_WORKING
}