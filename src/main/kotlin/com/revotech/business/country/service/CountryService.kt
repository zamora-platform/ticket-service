package com.revotech.business.country.service

import com.revotech.business.country.dto.*
import com.revotech.business.country.entity.City
import com.revotech.business.country.entity.CityStatus
import com.revotech.business.country.entity.Country
import com.revotech.business.country.entity.CountryStatus
import com.revotech.business.country.exception.CountryException
import com.revotech.business.country.repository.CityRepository
import com.revotech.business.country.repository.CountryRepository
import com.revotech.business.ticket_agent.dto.SearchInput
import com.revotech.util.WebUtil
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CountryService(
    private val countryRepository: CountryRepository,
    private val cityRepository: CityRepository,
    private val webUtil: WebUtil
) {
    @Transactional
    fun saveCountry(saveCountryReq: SaveCountryReq): Boolean {

        val userId = webUtil.getUserId()

        if (saveCountryReq.id == null) {
            validate(
                saveCountryReq = saveCountryReq,
                isCreate = true,
                currentCountryToUpdate = null
            )

            val newCountry = countryRepository.save(
                Country(
                    code = saveCountryReq.code,
                    name = saveCountryReq.name,
                    isDefault = false,
                    sortOrder = saveCountryReq.sortOrder,
                    status = CountryStatus.ACTIVE
                ).apply {
                    createdBy = userId
                }
            )

            if (saveCountryReq.listCity?.isEmpty() == false) {
                cityRepository.saveAll(
                    saveCountryReq.listCity!!.map {
                        City(
                            name = it.cityName,
                            countryId = newCountry.id,
                            status = CityStatus.ACTIVE
                        )
                    }
                )
            }
        } else {
            // UPDATE
            val currentCountryToUpdate = findCountryById(saveCountryReq.id!!)

            validate(
                saveCountryReq = saveCountryReq,
                isCreate = false,
                currentCountryToUpdate = currentCountryToUpdate
            )

            currentCountryToUpdate?.apply {
                code = saveCountryReq.code
                name = saveCountryReq.name
                sortOrder = saveCountryReq.sortOrder
                lastModifiedBy = userId
            }

            cityRepository.deleteAllByCountryId(currentCountryToUpdate?.id!!)

            if (saveCountryReq.listCity?.isEmpty() == false) {
                cityRepository.saveAll(
                    saveCountryReq.listCity!!.map {
                        City(
                            name = it.cityName,
                            countryId = currentCountryToUpdate.id,
                            status = CityStatus.ACTIVE
                        )
                    }
                )
            }
        }

        return true
    }

    fun isCodeExisted(code: String) {
        val isExisted = countryRepository.existsByCode(code)
        if (isExisted) {
            throw CountryException("CountryCodeExist", "Country code is existed!")
        }
    }

    fun isNameExisted(code: String) {
        val isExisted = countryRepository.existsByName(code)
        if (isExisted) {
            throw CountryException("CountryNameExist", "Country name is existed!")
        }
    }

    fun validate(
        saveCountryReq: SaveCountryReq,
        isCreate: Boolean,
        currentCountryToUpdate: Country?,
    ) {
        if (saveCountryReq.code.isNullOrBlank()) {
            throw CountryException("CountryCodeRequired", "Country code is required!")
        }

        if (saveCountryReq.name.isNullOrBlank()) {
            throw CountryException("CountryNameRequired", "Country name is required!")
        }

        if (!isCreate) {
            if (saveCountryReq.code != currentCountryToUpdate?.code) {
                throw CountryException("CountryCodeExist", "Country code is existed!")
            }
            if (saveCountryReq.name != currentCountryToUpdate?.name) {
                throw CountryException("CountryNameRequired", "Country name is required!")
            }
        } else {
            isCodeExisted(saveCountryReq.code!!)
            isNameExisted(saveCountryReq.name!!)
        }
    }

    fun findCountryById(id: String): Country? {
        return countryRepository.findCountryById(id) ?: throw CountryException(
            "CountryNotFound", "Country not found"
        )
    }

    fun searchCountry(searchInput: SearchInput, pageable: Pageable): SearchCountryResult {
        val countryList = countryRepository.searchCountry(searchInput.textSearch, pageable)

        val cityList = if (countryList.content.isNotEmpty()) {
            cityRepository.findCitiesByCountryIds(countryList.content.map { it.getId() })
        } else {
            emptyList()
        }

        val cityListMap = cityList.groupBy { it.getCountryId() }

        val countryListMap = countryList.content.map { item ->
            CountryList(
                id = item.getId(),
                code = item.getCode(),
                name = item.getName(),
                status = item.getStatus(),
                sortOrder = item.getSortOrder(),
                isDefault = item.getIsDefault(),
                createdBy = item.getCreatedBy(),
                createdTime = item.getCreatedTime(),
                listCity = cityListMap[item.getId()]?.map { cityList ->
                    ListCity(
                        cityId = cityList.getCityId(),
                        cityName = cityList.getCityName()
                    )
                }
            )
        }

        return SearchCountryResult(
            content = countryListMap,
            page = countryList.number + 1,
            pageSize = countryList.size,
            totalRecords = countryList.totalElements.toInt(),
            totalPages = countryList.totalPages
        )
    }

    fun getNextCountrySortOrder(): Int {
        return countryRepository.getNextCountrySortOrder()
    }

    fun getDetailCountryById(id: String): CountryDetail {
        val detailCountry = countryRepository.getDetailCountryById(id) ?: throw CountryException(
            "CountryNotFound", "Country not found"
        )

        val listCity = cityRepository.findCitiesByCountryIds(
            listOf(detailCountry.getId())
        )

        return CountryDetail(
            id = detailCountry.getId(),
            code = detailCountry.getCode(),
            name = detailCountry.getName(),
            status = detailCountry.getStatus(),
            sortOrder = detailCountry.getSortOrder(),
            isDefault = detailCountry.getIsDefault(),
            createdBy = detailCountry.getCreatedBy(),
            createdTime = detailCountry.getCreatedTime(),
            listCity = listCity.map { item ->
                ListCity(
                    cityId = item.getCityId(),
                    cityName = item.getCityName()
                )
            }
        )
    }

    @Transactional
    fun deleteCountry(id: String): Boolean {

        val currentCountry = findCountryById(id)

        cityRepository.softDeleteCityByCountryId(
            listOf(currentCountry?.id!!)
        )

        countryRepository.save(
            currentCountry.apply {
                status = CountryStatus.INACTIVE
            }
        )

        return true
    }

    @Transactional
    fun setCountryDefault(id: String): Boolean {

        val currentCountry = findCountryById(id)

        if (currentCountry?.isDefault == true) {
            throw CountryException("CountryAlreadyIsDefault", "This country already is default")
        } else {
            countryRepository.unsetCountryIsDefaultTrueToFalse()
            currentCountry!!.isDefault = true
            countryRepository.save(currentCountry)
        }

        return true
    }
}