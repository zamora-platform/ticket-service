package com.revotech.business.airport.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.revotech.business.airport.dto.AirportDetail
import com.revotech.business.airport.dto.SearchAirportResult
import com.revotech.business.airport.dto.SearchInput
import com.revotech.business.airport.service.AirportService
import com.revotech.graphql.GraphqlUtil
import com.revotech.graphql.type.CustomPageable

@DgsComponent
class AirportQuery(private val airportService: AirportService) {
    @DgsQuery
    fun getNextAirportSortOrder(): Int {
        return airportService.getNextAirportSortOrder()
    }

    @DgsQuery
    fun searchAirport(searchInput: SearchInput, pageable: CustomPageable): SearchAirportResult {
        val adjustedPageable = pageable.copy(page = (pageable.page ?: 1).coerceAtLeast(1) - 1)
        val page = GraphqlUtil.toPageable(adjustedPageable)
        return airportService.searchAirport(searchInput, page)
    }

    @DgsQuery
    fun getDetailAirportById(id: String): AirportDetail {
        return airportService.getDetailAirportById(id)
    }
}