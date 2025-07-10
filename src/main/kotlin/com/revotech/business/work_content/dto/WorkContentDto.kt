package com.revotech.business.work_content.dto

data class SaveWorkContentReq(
    var id: String? = null,
    var code: String? = null,
    var content: String? = null,
    var timeFrom: String? = null,
    var timeTo: String? = null,
    var openTicketRegistration: Boolean? = null
)