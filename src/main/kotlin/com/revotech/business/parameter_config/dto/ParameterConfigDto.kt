package com.revotech.business.parameter_config.dto

/* --- INPUTS --- */
data class ParameterBookingConfigInput(
    val id: String? = null,
    val value: ParameterBookingValueInput? = null
)

data class ParameterBookingValueInput(
    val code: BookingCodeSettingsInput? = null
)

data class BookingCodeSettingsInput(
    val enabledGenerateCode: Boolean? = null,
    val codeStructure: String? = null,
    var startValue: String? = null,
    val allowEdit: Boolean? = null
)

/* --- OUTPUTS --- */
data class ParameterBookingConfigResultType(
    val id: String? = null,
    val value: ParameterBookingValueResultType? = null
)

data class ParameterBookingValueResultType(
    val code: BookingCodeSettingsResultType? = null
)

data class BookingCodeSettingsResultType(
    val enabledGenerateCode: Boolean? = null,
    val codeStructure: String? = null,
    val startValue: String? = null,
    val allowEdit: Boolean? = null,
)
