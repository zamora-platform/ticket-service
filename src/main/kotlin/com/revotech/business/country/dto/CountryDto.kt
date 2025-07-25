package com.revotech.business.country.dto

import java.time.LocalDateTime

data class SaveCountryReq(
    var id: String? = null,
    var code: String? = null,
    var name: String? = null,
    var sortOrder: Int? = null,
    var listCity: List<ListCityReq>? = emptyList(),
    var isDefault: Boolean? = null,
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
    var isDefault: Boolean? = null,
    var createdBy: String? = null,
    var createdTime: LocalDateTime? = null,
    var listCity: List<ListCity>? = null
)

data class CountryDetail(
    var id: String? = null,
    var code: String? = null,
    var name: String? = null,
    var status: String? = null,
    var sortOrder: Int? = null,
    var isDefault: Boolean? = null,
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

data class SearchInput(
    var textSearch: String? = null
)

interface CountryProjection {
    fun getId(): String
    fun getCode(): String
    fun getName(): String
    fun getStatus(): String
    fun getSortOrder(): Int
    fun getIsDefault(): Boolean
    fun getCreatedBy(): String
    fun getCreatedTime(): LocalDateTime
}

interface CityListProjection {
    fun getCityId(): String?
    fun getCityName(): String?
    fun getCountryId(): String?
}