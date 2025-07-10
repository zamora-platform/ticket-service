package com.revotech.business.airline.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.revotech.business.airline.service.AirlineService

@DgsComponent
class AirlineQuery(private val airlineService: AirlineService) {
    @DgsQuery
    fun getNextAirlineSortOrder(): Int {
        return airlineService.getNextAirlineSortOrder()
    }
}