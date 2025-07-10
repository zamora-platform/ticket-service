package com.revotech.business.airline.dto

import com.revotech.business.airline.entity.AirlineType

data class SaveAirlineReq(
    var id: String? = null,
    var code: String? = null,
    var name: String? = null,
    var type: AirlineType? = null,
    var sortOrder: Int? = null,
)