package com.revotech.business.ticket_agent.dto

import com.revotech.business.ticket_agent.entity.TicketAgentStatus
import java.time.LocalDateTime
import java.util.*

data class SaveTicketAgentReq(
    var id: String? = null,
    var code: String? = null,
    var name: String? = null,
    var address: String? = null,
    var taxCode: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var note: String? = null,
    var ticketAgentContact: List<TicketAgentContact>? = null
)

data class TicketAgentContact(
    var ticketAgentContactName: String? = null,
    var ticketAgentContactEmail: String? = null,
    var ticketAgentContactPhone: String? = null
)

data class SearchInput(
    var textSearch: String? = null
)

data class TicketAgentContactList(
    val ticketAgentContactId: String? = null,
    val ticketAgentContactName: String? = null,
    val ticketAgentContactEmail: String? = null,
    val ticketAgentContactPhone: String? = null
)

data class TicketAgentList(
    val id: String? = null,
    val code: String? = null,
    val name: String? = null,
    val address: String? = null,
    val taxCode: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val note: String? = null,
    val status: String? = null,
    val createdBy: String? = null,
    val createdTime: LocalDateTime? = null,
    val ticketAgentContact: List<TicketAgentContactList>? = null
)

data class TicketAgentDetail(
    val id: String? = null,
    val code: String? = null,
    val name: String? = null,
    val address: String? = null,
    val taxCode: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val note: String? = null,
    val status: String? = null,
    val createdBy: String? = null,
    val createdTime: LocalDateTime? = null,
    val ticketAgentContact: List<TicketAgentContactList>? = null
)

interface TicketAgentListProjection {
    fun getId(): String
    fun getCode(): String
    fun getName(): String
    fun getAddress(): String
    fun getTaxCode(): String
    fun getPhone(): String
    fun getEmail(): String
    fun getNote(): String
    fun getStatus(): String
    fun getCreatedBy(): String
    fun getCreatedTime(): LocalDateTime
}

interface TicketAgentContactProjection {
    fun getTicketAgentId(): String?
    fun getName(): String?
    fun getEmail(): String?
    fun getPhone(): String?
}

data class SearchTicketAgentResult(
    val content: List<TicketAgentList>,
    val page: Int,
    val pageSize: Int,
    val totalRecords: Int,
    val totalPages: Int
)