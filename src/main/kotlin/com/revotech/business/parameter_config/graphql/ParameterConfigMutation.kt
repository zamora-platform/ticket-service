package com.revotech.business.parameter_config.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.revotech.business.parameter_config.dto.ParameterBookingConfigInput
import com.revotech.business.parameter_config.dto.ParameterBookingConfigResultType
import com.revotech.business.parameter_config.entity.ParameterConfig
import com.revotech.business.parameter_config.service.ParameterConfigService

@DgsComponent
class ParameterConfigMutation(private val parameterConfigService: ParameterConfigService) {
    @DgsMutation
    fun saveParameterBookingConfig(parameterBookingConfigInput: ParameterBookingConfigInput): ParameterBookingConfigResultType {
        return parameterConfigService.saveParameterBookingConfig(parameterBookingConfigInput)
    }

    @DgsMutation
    fun increaseBookingRequestCodeStartValue(): Boolean {
        return parameterConfigService.increaseBookingRequestCodeStartValue()
    }
}