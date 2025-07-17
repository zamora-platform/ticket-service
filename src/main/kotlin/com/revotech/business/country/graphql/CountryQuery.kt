package com.revotech.business.country.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.revotech.business.country.dto.*
import com.revotech.business.country.service.CountryService
import com.revotech.graphql.GraphqlUtil
import com.revotech.graphql.type.CustomPageable

@DgsComponent
class CountryQuery(private val countryService: CountryService) {
    @DgsQuery
    fun searchCountry(searchInput: SearchInput, pageable: CustomPageable): SearchCountryResult {
        val adjustedPageable = pageable.copy(page = (pageable.page ?: 1).coerceAtLeast(1) - 1)
        val page = GraphqlUtil.toPageable(adjustedPageable)
        return countryService.searchCountry(searchInput, page)
    }

    @DgsQuery
    fun getNextCountrySortOrder(): Int {
        return countryService.getNextCountrySortOrder()
    }

    @DgsQuery
    fun getDetailCountryById(id: String): CountryDetail {
        return countryService.getDetailCountryById(id)
    }

    @DgsQuery
    fun getAllCountryAndItCity(): List<CountryList> {
        return countryService.getAllCountryAndItCity()
    }

    @DgsQuery
    fun getAllCity(): List<ListCity> {
        return countryService.getAllCity()
    }
}