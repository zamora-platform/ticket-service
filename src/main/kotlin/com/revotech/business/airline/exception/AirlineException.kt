package com.revotech.business.airline.exception

import com.revotech.exception.AppException
import com.revotech.exception.NotFoundException

open class AirlineException(code: String, message: String) : AppException(code, message)

class AirlineNotFoundException(code: String, message: String) : AirlineException(code, message), NotFoundException