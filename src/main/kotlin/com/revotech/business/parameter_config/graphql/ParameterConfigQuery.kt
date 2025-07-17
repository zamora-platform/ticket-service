package com.revotech.business.parameter_config.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.revotech.business.parameter_config.dto.ParameterBookingConfigResultType
import com.revotech.business.parameter_config.service.ParameterConfigService

@DgsComponent
class ParameterConfigQuery(private val parameterConfigService: ParameterConfigService) {
    @DgsQuery
    fun getParameterBookingConfig(): ParameterBookingConfigResultType {
        return parameterConfigService.getParameterBookingConfig()
    }

    @DgsQuery
    fun getBookingRequestCode(): String? {
        return parameterConfigService.getBookingRequestCode()
    }
}