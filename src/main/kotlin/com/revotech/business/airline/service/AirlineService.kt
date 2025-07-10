package com.revotech.business.airline.service

import com.revotech.business.airline.dto.SaveAirlineReq
import com.revotech.business.airline.entity.Airline
import com.revotech.business.airline.entity.AirlineStatus
import com.revotech.business.airline.exception.AirlineException
import com.revotech.business.airline.repository.AirlineRepository
import com.revotech.business.airport.exception.AirportException
import com.revotech.util.WebUtil
import org.springframework.stereotype.Service

@Service
class AirlineService(
    private val airlineRepository: AirlineRepository,
    private val webUtil: WebUtil
) {
    fun saveAirline(saveAirlineReq: SaveAirlineReq): Boolean {

        val userId = webUtil.getUserId()

        if (saveAirlineReq.id == null) {
            // CREATE
            validate(
                saveAirlineReq = saveAirlineReq,
                isCreate = true,
                currentAirlineToUpdate = null
            )
            airlineRepository.save(
                Airline(
                    code = saveAirlineReq.code,
                    name = saveAirlineReq.name,
                    type = saveAirlineReq.type,
                    sortOrder = saveAirlineReq.sortOrder ?: getNextAirlineSortOrder(),
                    status = AirlineStatus.WORKING
                ).apply {
                    createdBy = userId
                }
            )
        } else {
            // UPDATE
            val currentAirlineToUpdate = findAirlineById(saveAirlineReq.id!!)
            validate(
                saveAirlineReq = saveAirlineReq,
                isCreate = false,
                currentAirlineToUpdate = currentAirlineToUpdate
            )
            airlineRepository.save(
                currentAirlineToUpdate.apply {
                    code = saveAirlineReq.code
                    name = saveAirlineReq.name
                    type = saveAirlineReq.type
                    sortOrder = saveAirlineReq.sortOrder ?: getNextAirlineSortOrder()
                    lastModifiedBy = userId
                }
            )
        }

        return true
    }

    fun validate(
        saveAirlineReq: SaveAirlineReq,
        isCreate: Boolean,
        currentAirlineToUpdate: Airline?
    ) {
        if (saveAirlineReq.code.isNullOrBlank()) {
            throw AirlineException("AirlineCodeRequired", "Airline code is required!")
        }
        if (saveAirlineReq.name.isNullOrBlank()) {
            throw AirlineException("AirlineNameRequired", "Airline name is required!")
        }

        if (!isCreate) {
            if (saveAirlineReq.code != currentAirlineToUpdate?.code) {
                isCodeAirlineExisted(saveAirlineReq.code!!)
            }
            if (saveAirlineReq.name != currentAirlineToUpdate?.name) {
                isNameAirlineExisted(saveAirlineReq.name!!)
            }
        } else {
            isCodeAirlineExisted(saveAirlineReq.code!!)
            isNameAirlineExisted(saveAirlineReq.name!!)
        }
    }

    fun isCodeAirlineExisted(code: String) {
        val isExisted = airlineRepository.existsByCodeAndStatus(code, AirlineStatus.WORKING)
        if (isExisted) {
            throw AirportException("AirportCodeExisted", "Airport code is existed!")
        }
    }

    fun isNameAirlineExisted(name: String) {
        val isExisted = airlineRepository.existsByNameAndStatus(name, AirlineStatus.WORKING)
        if (isExisted) {
            throw AirportException("AirportNameExisted", "Airport name is existed!")
        }
    }

    fun findAirlineById(id: String): Airline {
        return airlineRepository.findAirlineById(id)
            ?: throw AirlineException("AirlineNotFound", "Airline not found!")
    }

    fun getNextAirlineSortOrder(): Int {
        return airlineRepository.getNextAirlineSortOrder()
    }
}