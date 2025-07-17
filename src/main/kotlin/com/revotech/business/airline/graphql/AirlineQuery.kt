package com.revotech.business.airline.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.revotech.business.airline.dto.AirlineDetail
import com.revotech.business.airline.dto.AirlineList
import com.revotech.business.airline.dto.SearchAirlineResult
import com.revotech.business.airline.service.AirlineService
import com.revotech.business.airport.dto.SearchAirportResult
import com.revotech.business.airport.dto.SearchInput
import com.revotech.graphql.GraphqlUtil
import com.revotech.graphql.type.CustomPageable

@DgsComponent
class AirlineQuery(private val airlineService: AirlineService) {
    @DgsQuery
    fun getNextAirlineSortOrder(): Int {
        return airlineService.getNextAirlineSortOrder()
    }

    @DgsQuery
    fun searchAirline(searchInput: SearchInput, pageable: CustomPageable): SearchAirlineResult {
        val adjustedPageable = pageable.copy(page = (pageable.page ?: 1).coerceAtLeast(1) - 1)
        val page = GraphqlUtil.toPageable(adjustedPageable)
        return airlineService.searchAirline(searchInput, page)
    }

    @DgsQuery
    fun getDetailAirlineById(id: String): AirlineDetail {
        return airlineService.getDetailAirlineById(id)
    }

    @DgsQuery
    fun getAllAirline(): List<AirlineList> {
        return airlineService.getAllAirline()
    }
}