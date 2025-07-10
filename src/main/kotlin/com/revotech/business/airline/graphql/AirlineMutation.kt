package com.revotech.business.airline.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.revotech.business.airline.dto.SaveAirlineReq
import com.revotech.business.airline.service.AirlineService

@DgsComponent
class AirlineMutation(private val airlineService: AirlineService) {
    @DgsMutation
    fun deleteAirline(id: String): Boolean {
        return airlineService.deleteAirline(id)
    }
}