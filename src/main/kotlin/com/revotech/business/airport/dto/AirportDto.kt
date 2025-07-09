package com.revotech.business.airport.dto

import java.time.LocalDateTime

data class SaveAirportReq(
    var id: String? = null,
    var code: String? = null,
    var name: String? = null,
    var countryId: String? = null,
    var cityId: String? = null,
    var sortOrder: Int? = null,
)

data class Airport(
    var id: String? = null,
    var code: String? = null,
    var name: String? = null,
    var countryName: String? = null,
    var cityName: String? = null,
    var status: String? = null,
    var sortOrder: Int? = null,
    var isDefault: Boolean? = null,
    var createdBy: String? = null,
    var createdTime: LocalDateTime? = null,
)

interface AirportProjection {
    fun getId(): String
    fun getCode(): String
    fun getName(): String
    fun getCountryName(): String
    fun getCityName(): String
    fun getStatus(): String
    fun getSortOrder(): Int
    fun getIsDefault(): Boolean
    fun getCreatedBy(): String
    fun getCreatedTime(): LocalDateTime
}

data class SearchInput(
    var textSearch: String? = null
)

data class SearchAirportResult(
    val content: List<Airport>,
    val page: Int,
    val pageSize: Int,
    val totalRecords: Int,
    val totalPages: Int
)