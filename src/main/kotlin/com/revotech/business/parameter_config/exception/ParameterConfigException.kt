package com.revotech.business.parameter_config.exception

import com.revotech.exception.AppException

open class ParameterConfigException(code: String, message: String) : AppException(code, message)