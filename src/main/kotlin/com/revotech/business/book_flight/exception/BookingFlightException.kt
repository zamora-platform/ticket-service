package com.revotech.business.book_flight.exception

import com.revotech.exception.AppException
import com.revotech.exception.NotFoundException

open class BookingFlightException(code: String, message: String) : AppException(code, message)

class BookingFlightNotFoundException(code: String, message: String) : BookingFlightException(code, message),
    NotFoundException