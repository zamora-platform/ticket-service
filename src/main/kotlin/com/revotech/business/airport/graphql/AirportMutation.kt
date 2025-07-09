package com.revotech.business.airport.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.revotech.business.airport.dto.SaveAirportReq
import com.revotech.business.airport.service.AirportService
import com.revotech.business.country.dto.SaveCountryReq
import com.revotech.business.country.service.CountryService

@DgsComponent
class AirportMutation(private val airportService: AirportService) {
    @DgsMutation
    fun saveAirport(saveAirportReq: SaveAirportReq): Boolean {
        return airportService.saveAirport(saveAirportReq)
    }

    @DgsMutation
    fun deleteAirport(id: String): Boolean {
        return airportService.deleteAirport(id)
    }
}