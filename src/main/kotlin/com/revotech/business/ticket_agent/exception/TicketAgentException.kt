package com.revotech.business.ticket_agent.exception

import com.revotech.exception.AppException
import com.revotech.exception.NotFoundException

open class TicketAgentException(code: String, message: String) : AppException(code, message)

class TicketAgentNotFoundException(code: String, message: String) : TicketAgentException(code, message), NotFoundException