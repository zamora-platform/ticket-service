package com.revotech.business.parameter_config.repository

import com.revotech.business.parameter_config.entity.ParameterConfig
import org.springframework.data.jpa.repository.JpaRepository

interface ParameterConfigRepository : JpaRepository<ParameterConfig, String> {
}