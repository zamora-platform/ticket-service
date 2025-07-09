package com.revotech.business.airport.exception

import com.revotech.exception.AppException
import com.revotech.exception.NotFoundException

open class AirportException(code: String, message: String) : AppException(code, message)

class AirportNotFoundException(code: String, message: String) : AirportException(code, message), NotFoundException