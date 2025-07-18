package com.revotech.business.airline.dto

import com.revotech.business.airline.entity.AirlineType
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class SaveAirlineReq(
    var id: String? = null,
    var code: String? = null,
    var name: String? = null,
    var logoFile: AirlineLogoAttachment? = null,
    var type: AirlineType? = null,
    var sortOrder: Int? = null,
)

data class AirlineLogoAttachment(
    var id: String? = null,
    var file: MultipartFile? = null
)

data class AirlineList(
    var id: String? = null,
    var code: String? = null,
    var name: String? = null,
    var logoFile: AirlineLogoDetailAttachment? = null,
    var type: String? = null,
    var sortOrder: Int? = null,
    var status: String? = null,
    var createdBy: String? = null,
    var createdTime: LocalDateTime? = null
)

data class AirlineDetail(
    var id: String? = null,
    var code: String? = null,
    var name: String? = null,
    var logoFile: AirlineLogoDetailAttachment? = null,
    var type: String? = null,
    var sortOrder: Int? = null,
    var status: String? = null,
    var createdBy: String? = null,
    var createdTime: LocalDateTime? = null
)

data class AirlineLogoDetailAttachment(
    var id: String? = null,
    var downloadPath: String? = null
)

data class SearchInput(
    var textSearch: String? = null
)

data class SearchAirlineResult(
    val content: List<AirlineList>,
    val page: Int,
    val pageSize: Int,
    val totalRecords: Int,
    val totalPages: Int
)

interface AirlineProjection {
    fun getId(): String
    fun getCode(): String
    fun getName(): String
    fun getLogoFileId(): String
    fun getLogoFileDownloadPath(): String
    fun getType(): String
    fun getSortOrder(): Int
    fun getStatus(): String
    fun getCreatedBy(): String
    fun getCreatedTime(): LocalDateTime
}