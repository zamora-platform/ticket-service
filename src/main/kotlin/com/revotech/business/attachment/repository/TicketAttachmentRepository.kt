package com.revotech.business.attachment.repository

import com.revotech.business.attachment.entity.TicketAttachment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TicketAttachmentRepository : JpaRepository<TicketAttachment, String> {
    @Query("SELECT ta " +
            "FROM TicketAttachment ta " +
            "WHERE ta.id = :id " +
            "AND ta.objectType = 'AIRLINE_LOGO' " +
            "AND ta.isDeleted = false ")
    fun findLogoAirlineFileById(id: String): TicketAttachment?
}