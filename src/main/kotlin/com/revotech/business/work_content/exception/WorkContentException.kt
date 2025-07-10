package com.revotech.business.work_content.exception

import com.revotech.exception.AppException
import com.revotech.exception.NotFoundException

open class WorkContentException(code: String, message: String) : AppException(code, message)

class WorkContentNotFoundException(code: String, message: String) : WorkContentException(code, message), NotFoundException