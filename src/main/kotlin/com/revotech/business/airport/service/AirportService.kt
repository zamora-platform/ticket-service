package com.revotech.business.airport.service

import com.revotech.business.airline.exception.AirlineException
import com.revotech.business.airport.dto.AirportDetail
import com.revotech.business.airport.dto.SaveAirportReq
import com.revotech.business.airport.dto.SearchAirportResult
import com.revotech.business.airport.dto.SearchInput
import com.revotech.business.airport.entity.Airport
import com.revotech.business.airport.entity.AirportStatus
import com.revotech.business.airport.exception.AirportException
import com.revotech.business.airport.repository.AirportRepository
import com.revotech.business.book_flight.service.BookingFlightService
import com.revotech.business.country.entity.City
import com.revotech.business.country.entity.Country
import com.revotech.business.country.exception.CountryException
import com.revotech.business.country.service.CountryService
import com.revotech.util.WebUtil
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AirportService(
    private val airportRepository: AirportRepository,
    private val countryService: CountryService,
    private val bookingFlightService: BookingFlightService,
    private val webUtil: WebUtil
) {
    fun saveAirport(saveAirportReq: SaveAirportReq): Boolean {

        val userId = webUtil.getUserId()

        if (saveAirportReq.id == null) {
            // CREATE
            validate(
                saveAirportReq = saveAirportReq,
                isCreate = true,
                currentAirportToUpdate = null
            )
            airportRepository.save(
                Airport(
                    code = saveAirportReq.code,
                    name = saveAirportReq.name,
                    countryId = saveAirportReq.countryId,
                    cityId = saveAirportReq.cityId,
                    sortOrder = saveAirportReq.sortOrder ?: getNextAirportSortOrder(),
                    isDefault = false,
                    status = AirportStatus.WORKING
                ).apply {
                    createdBy = userId
                }
            )
        } else {
            // UPDATE
            val currentAirportToUpdate = findAirportById(saveAirportReq.id!!)
            validate(
                saveAirportReq = saveAirportReq,
                isCreate = false,
                currentAirportToUpdate = currentAirportToUpdate
            )
            airportRepository.save(
                currentAirportToUpdate.apply {
                    code = saveAirportReq.code
                    name = saveAirportReq.name
                    countryId = saveAirportReq.countryId
                    cityId = saveAirportReq.cityId
                    sortOrder = saveAirportReq.sortOrder ?: getNextAirportSortOrder()
                    lastModifiedBy = userId
                }
            )
        }

        return true
    }

    fun isCodeAirportExisted(code: String) {
        val isExisted = airportRepository.existsByCodeAndStatus(code, AirportStatus.WORKING)
        if (isExisted) {
            throw AirportException("AirportCodeExisted", "Airport code is existed!")
        }
    }

    fun isNameAirportExisted(name: String) {
        val isExisted = airportRepository.existsByNameAndStatus(name, AirportStatus.WORKING)
        if (isExisted) {
            throw AirportException("AirportNameExisted", "Airport name is existed!")
        }
    }

    fun validate(
        saveAirportReq: SaveAirportReq,
        isCreate: Boolean,
        currentAirportToUpdate: Airport?,
    ) {
        if (saveAirportReq.code.isNullOrBlank()) {
            throw AirportException("AirportCodeRequired", "Airport code is required!")
        }
        if (saveAirportReq.name.isNullOrBlank()) {
            throw AirportException("AirportNameRequired", "Airport name is required!")
        }

        if (!isCreate) {
            if (saveAirportReq.code != currentAirportToUpdate?.code) {
                isCodeAirportExisted(saveAirportReq.code!!)
            }
            if (saveAirportReq.name != currentAirportToUpdate?.name) {
                isNameAirportExisted(saveAirportReq.name!!)
            }
        } else {
            isCodeAirportExisted(saveAirportReq.code!!)
            isNameAirportExisted(saveAirportReq.name!!)
        }

        var selectedCountry: Country? = null
        var selectedCity: City? = null

        if (saveAirportReq.countryId != null) {
            selectedCountry = countryService.findCountryById(saveAirportReq.countryId!!)
        }

        if (saveAirportReq.cityId != null) {
            selectedCity = countryService.findCityById(saveAirportReq.cityId!!)
        }

        if (selectedCity != null && selectedCountry != null) {
            if (selectedCity.countryId != selectedCountry.id) {
                throw AirportException(
                    "CityNotBelongToCountry",
                    "City '${selectedCity.name}' not belong to country '${selectedCountry.name}'!"
                )
            }
        }
    }

    fun findAirportById(id: String): Airport {
        return airportRepository.findAirportById(id)
            ?: throw AirportException("AirportNotFound", "Airport not found!")
    }

    fun getNextAirportSortOrder(): Int {
        return airportRepository.getNextAirportSortOrder()
    }

    fun searchAirport(searchInput: SearchInput, pageable: Pageable): SearchAirportResult {
        val listSearchedAirport = airportRepository.searchAirport(searchInput.textSearch, pageable)

        val listAirportMapped = listSearchedAirport.content.map { item ->
            com.revotech.business.airport.dto.Airport(
                id = item.getId(),
                code = item.getCode(),
                name = item.getName(),
                countryName = item.getCountryName(),
                cityName = item.getCityName(),
                status = item.getStatus(),
                sortOrder = item.getSortOrder(),
                isDefault = item.getIsDefault(),
                createdBy = item.getCreatedBy(),
                createdTime = item.getCreatedTime()
            )
        }

        return SearchAirportResult(
            content = listAirportMapped,
            page = listSearchedAirport.number + 1,
            pageSize = listSearchedAirport.size,
            totalRecords = listSearchedAirport.totalElements.toInt(),
            totalPages = listSearchedAirport.totalPages
        )
    }

    fun getDetailAirportById(id: String): AirportDetail {
        val airportDetail = airportRepository.getDetailAirportById(id)
            ?: throw AirportException("AirportNotFound", "Airport not found!")

        return AirportDetail(
            id = airportDetail.getId(),
            code = airportDetail.getCode(),
            name = airportDetail.getName(),
            countryId = airportDetail.getCountryId(),
            countryName = airportDetail.getCountryName(),
            cityId = airportDetail.getCityId(),
            cityName = airportDetail.getCityName(),
            status = airportDetail.getStatus(),
            sortOrder = airportDetail.getSortOrder(),
            isDefault = airportDetail.getIsDefault(),
            createdBy = airportDetail.getCreatedBy(),
            createdTime = airportDetail.getCreatedTime()
        )
    }

    fun deleteAirport(id: String): Boolean {

        val currentAirport = findAirportById(id)

        val isUsingInSomeBookingFlightTicket = bookingFlightService.existByAirportId(currentAirport.id!!)

        if (isUsingInSomeBookingFlightTicket) {
            throw AirlineException("AirportCannotDelete", "Airport can't delete becase it being use in a booking flight ticket")
        }

        currentAirport.apply {
            status = AirportStatus.NOT_WORKING
        }
        airportRepository.save(currentAirport)

        return true
    }

    @Transactional
    fun setAirportDefault(id: String): Boolean {

        val currentAirport = findAirportById(id)

        if (currentAirport.isDefault == true) {
            throw CountryException("CountryAlreadyIsDefault", "This country already is default")
        } else {
            airportRepository.unsetAirportIsDefaultTrueToFalse()
            currentAirport.isDefault = true
            airportRepository.save(currentAirport)
        }

        return true
    }

    fun getAllAirport(): List<com.revotech.business.airport.dto.Airport> {
        val allAirport = airportRepository.getAllAirport()
        return allAirport.map { item ->
            com.revotech.business.airport.dto.Airport(
                id = item.getId(),
                code = item.getCode(),
                name = item.getName(),
                countryName = item.getCountryName(),
                cityName = item.getCityName(),
                status = item.getStatus(),
                sortOrder = item.getSortOrder(),
                isDefault = item.getIsDefault(),
                createdBy = item.getCreatedBy(),
                createdTime = item.getCreatedTime()
            )
        }
    }
}