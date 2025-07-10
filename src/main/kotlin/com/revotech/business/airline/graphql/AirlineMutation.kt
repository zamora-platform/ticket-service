package com.revotech.business.airline.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.revotech.business.airline.dto.SaveAirlineReq
import com.revotech.business.airline.service.AirlineService

@DgsComponent
class AirlineMutation(private val airlineService: AirlineService) {
    @DgsMutation
    fun saveAirline(saveAirlineReq: SaveAirlineReq): Boolean {
        return airlineService.saveAirline(saveAirlineReq)
    }
}