package com.revotech.business.attachment.entity

import com.revotech.audit.JpaActivityInfo
import jakarta.persistence.*

@Entity
@Table(
    name = "t_attachment",
    indexes = [
        Index(columnList = "objectType, objectId"),
    ]
)
class TicketAttachment(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    var name: String? = null,
    var extension: String? = null,
    @Column(length = 2000)
    var parentPath: String? = null,
    @Column(length = 2000)
    var downloadPath: String? = null,
    var size: Long? = null,
    @Enumerated(EnumType.STRING)
    var objectType: AttachmentType? = null,
    var objectId: String? = null,
    var isFolder: Boolean? = null,
    var parentId: String? = null,
    var isDeleted: Boolean? = false,
) : JpaActivityInfo()

enum class AttachmentType {
    AIRPORT, AIRLINE, AIRLINE_LOGO
}