package com.revotech.business.work_content.repository

import com.revotech.business.airline.entity.AirlineStatus
import com.revotech.business.work_content.entity.WorkContent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WorkContentRepository : JpaRepository<WorkContent, String> {
    fun existsByCodeAndIsDeletedFalse(code: String): Boolean
    fun findByIdAndIsDeletedFalse(id: String): WorkContent?
}