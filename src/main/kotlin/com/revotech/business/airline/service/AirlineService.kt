package com.revotech.business.airline.service

import com.revotech.business.airline.dto.*
import com.revotech.business.airline.entity.Airline
import com.revotech.business.airline.entity.AirlineStatus
import com.revotech.business.airline.exception.AirlineException
import com.revotech.business.airline.repository.AirlineRepository
import com.revotech.business.airport.dto.SearchInput
import com.revotech.business.airport.exception.AirportException
import com.revotech.business.attachment.entity.AttachmentType
import com.revotech.business.attachment.repository.TicketAttachmentRepository
import com.revotech.business.attachment.service.TicketAttachmentService
import com.revotech.business.book_flight.service.BookingFlightService
import com.revotech.util.WebUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AirlineService(
    private val airlineRepository: AirlineRepository,
    private val ticketAttachmentRepository: TicketAttachmentRepository,
    private val webUtil: WebUtil,
    private val ticketAttachmentService: TicketAttachmentService
) {
    private lateinit var bookingFlightService: BookingFlightService

    @Autowired
    fun setBookingFlightService(@Lazy bookingFlightService: BookingFlightService) {
        this.bookingFlightService = bookingFlightService
    }

    fun saveAirline(saveAirlineReq: SaveAirlineReq): Boolean {

        val userId = webUtil.getUserId()
        var newAirline: Airline? = null
        var currentAirlineToUpdate: Airline? = null

        if (saveAirlineReq.id == null) {
            // CREATE
            validate(
                saveAirlineReq = saveAirlineReq,
                isCreate = true,
                currentAirlineToUpdate = null
            )
            newAirline = airlineRepository.save(
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
            currentAirlineToUpdate = findAirlineById(saveAirlineReq.id!!)
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

        // UPLOAD LOGO FILE
        saveAirlineReq.logoFile?.let { file ->

            val airlineId = newAirline?.id ?: currentAirlineToUpdate?.id
            ?: throw IllegalStateException("Airline ID không xác định")

            // CASE HAS ID
            if (!file.id.isNullOrBlank()) {
                val oldAirlineLogoFile = ticketAttachmentService.findLogoAirlineFileById(file.id!!)
                oldAirlineLogoFile.lastModifiedBy = userId
                ticketAttachmentRepository.save(oldAirlineLogoFile).also {
                    if (file.file != null) {
                        ticketAttachmentRepository.delete(oldAirlineLogoFile)
                        ticketAttachmentService.uploadFileToTicketAttachment(
                            listOf(file.file!!), airlineId, AttachmentType.AIRLINE_LOGO
                        )
                    }
                }
            } else {
                // CASE NO ID
                ticketAttachmentService.uploadFileToTicketAttachment(
                    listOf(file.file!!), airlineId, AttachmentType.AIRLINE_LOGO
                )
            }
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

    fun searchAirline(searchInput: SearchInput, pageable: Pageable): SearchAirlineResult {

        val airlineListSearch = airlineRepository.searchAirline(searchInput.textSearch, pageable)

        val mappedAirlineList = airlineListSearch.content.map { item ->
            AirlineList(
                id = item.getId(),
                code = item.getCode(),
                name = item.getName(),
                type = item.getType(),
                sortOrder = item.getSortOrder(),
                status = item.getStatus(),
                createdBy = item.getCreatedBy(),
                createdTime = item.getCreatedTime()
            )
        }

        return SearchAirlineResult(
            content = mappedAirlineList,
            page = airlineListSearch.number + 1,
            pageSize = airlineListSearch.size,
            totalRecords = airlineListSearch.totalElements.toInt(),
            totalPages = airlineListSearch.totalPages
        )
    }

    fun getDetailAirlineById(id: String): AirlineDetail {
        val airlineDetail = airlineRepository.getDetailAirlineById(id)
            ?: throw AirlineException("AirlineNotFound", "Airline not found!")
        return AirlineDetail(
            id = airlineDetail.getId(),
            code = airlineDetail.getCode(),
            name = airlineDetail.getName(),
            logoFile = AirlineLogoDetailAttachment(
                id = airlineDetail.getLogoFileId(),
                downloadPath = airlineDetail.getLogoFileDownloadPath()
            ),
            type = airlineDetail.getType(),
            sortOrder = airlineDetail.getSortOrder(),
            status = airlineDetail.getStatus(),
            createdBy = airlineDetail.getCreatedBy(),
            createdTime = airlineDetail.getCreatedTime()
        )
    }

    fun deleteAirline(id: String): Boolean {

        val currentAirline = findAirlineById(id)

        val isUsingInSomeBookingFlightTicket = bookingFlightService.existByAirlineId(currentAirline.id!!)

        if (isUsingInSomeBookingFlightTicket) {
            throw AirlineException(
                "AirlineCannotDelete",
                "Airline can't delete becase it being use in a booking flight ticket"
            )
        }

        currentAirline.apply {
            status = AirlineStatus.NOT_WORKING
        }

        airlineRepository.save(currentAirline)

        return true
    }

    fun getAllAirline(): List<AirlineList> {
        val allAirline = airlineRepository.getAllAirline()
        return allAirline.map { item ->
            AirlineList(
                id = item.getId(),
                code = item.getCode(),
                name = item.getName(),
                type = item.getType(),
                sortOrder = item.getSortOrder(),
                status = item.getStatus(),
                createdBy = item.getCreatedBy(),
                createdTime = item.getCreatedTime()
            )
        }
    }
}