package com.revotech.business.work_content.entity

import com.revotech.audit.JpaActivityInfo
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "t_work_content")
class WorkContent(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    var code: String? = null,
    @Column(columnDefinition = "TEXT")
    var content: String? = null,
    var timeFrom: LocalDate? = null,
    var timeTo: LocalDate? = null,
    var openTicketRegistration: Boolean? = null,
    var isDeleted: Boolean? = null
) : JpaActivityInfo()