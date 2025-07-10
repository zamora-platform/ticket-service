package com.revotech.business.airline.controller

import com.revotech.business.airline.dto.SaveAirlineReq
import com.revotech.business.airline.service.AirlineService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/airline")
class AirlineController(
    private val airlineService: AirlineService
) {
    @PostMapping
    fun createJob(@ModelAttribute @Valid saveAirlineReq: SaveAirlineReq): Boolean? {
        return airlineService.saveAirline(saveAirlineReq)
    }
}