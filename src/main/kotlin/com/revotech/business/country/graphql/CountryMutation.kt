package com.revotech.business.country.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.revotech.business.country.dto.SaveCountryReq
import com.revotech.business.country.service.CountryService

@DgsComponent
class CountryMutation(private val countryService: CountryService) {
    @DgsMutation
    fun saveCountry(saveCountryReq: SaveCountryReq): Boolean {
        return countryService.saveCountry(saveCountryReq)
    }
}