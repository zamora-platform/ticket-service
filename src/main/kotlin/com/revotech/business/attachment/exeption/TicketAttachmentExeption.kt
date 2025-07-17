package com.revotech.business.attachment.exeption

import com.revotech.exception.AppException

open class AttachmentException(code: String, message: String) : AppException(code, message)
class AttachmentDuplicateNameException(code: String, message: String) : AttachmentException(code, message)
class NotAllowedException(code: String, message: String) : AttachmentException(code, message)