package com.revotech.business.country.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.revotech.business.country.dto.SearchCountryResult
import com.revotech.business.country.service.CountryService
import com.revotech.business.ticket_agent.dto.SearchInput
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
}