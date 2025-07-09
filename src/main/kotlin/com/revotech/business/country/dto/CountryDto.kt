package com.revotech.business.country.dto

import com.revotech.business.ticket_agent.dto.TicketAgentList
import java.time.LocalDateTime

data class SaveCountryReq(
    var id: String? = null,
    var code: String? = null,
    var name: String? = null,
    var sortOrder: Int? = null,
    var listCity: List<ListCityReq>? = emptyList()
)

data class ListCityReq(
    var cityName: String? = null
)

data class CountryList(
    var id: String? = null,
    var code: String? = null,
    var name: String? = null,
    var status: String? = null,
    var sortOrder: Int? = null,
    var createdBy: String? = null,
    var createdTime: LocalDateTime? = null,
    var listCity: List<ListCity>? = null
)

data class ListCity(
    var cityId: String? = null,
    var cityName: String? = null
)

data class SearchCountryResult(
    val content: List<CountryList>,
    val page: Int,
    val pageSize: Int,
    val totalRecords: Int,
    val totalPages: Int
)

interface CountryListProjection {
    fun getId(): String
    fun getCode(): String
    fun getName(): String
    fun getStatus(): String
    fun getSortOrder(): Int
    fun getCreatedBy(): String
    fun getCreatedTime(): LocalDateTime
}

interface CityListProjection {
    fun getCityId(): String?
    fun getCityName(): String?
    fun getCountryId(): String?
}