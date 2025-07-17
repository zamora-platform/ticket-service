package com.revotech.business.country.exception

import com.revotech.exception.AppException
import com.revotech.exception.NotFoundException

open class CountryException(code: String, message: String) : AppException(code, message)

class CountryNotFoundException(code: String, message: String) : CountryException(code, message), NotFoundException