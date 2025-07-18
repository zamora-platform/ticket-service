package com.revotech.business.work_content.dto

import org.springframework.data.annotation.CreatedBy
import java.time.LocalDate
import java.time.LocalDateTime

data class SaveWorkContentReq(
    var id: String? = null,
    var code: String? = null,
    var content: String? = null,
    var timeFrom: String? = null,
    var timeTo: String? = null,
    var openTicketRegistration: Boolean? = null
)

data class SearchInput(
    var textSearch: String? = null
)

data class WorkContentList(
    var id: String? = null,
    var code: String? = null,
    var content: String? = null,
    var timeFrom: String? = null,
    var timeTo: String? = null,
    var openTicketRegistration: Boolean? = null,
    var createdBy: String? = null,
    var createdTime: LocalDateTime? = null
)

data class SearchWorkContentResult(
    var content: List<WorkContentList>,
    var page: Int,
    var pageSize: Int,
    var totalRecords: Int,
    var totalPages: Int,
)

interface WorkContentProjection {
    fun getId(): String
    fun getCode(): String
    fun getContent(): String
    fun getTimeFrom(): LocalDate
    fun getTimeTo(): LocalDate
    fun getOpenTicketRegistration(): Boolean
}

data class WorkContentDetail(
    var id: String? = null,
    var code: String? = null,
    var content: String? = null,
    var timeFrom: String? = null,
    var timeTo: String? = null,
    var openTicketRegistration: Boolean? = null
)