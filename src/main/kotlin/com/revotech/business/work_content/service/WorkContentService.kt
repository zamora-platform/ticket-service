package com.revotech.business.work_content.service

import com.revotech.business.book_flight.service.BookingFlightService
import com.revotech.business.work_content.dto.*
import com.revotech.business.work_content.entity.WorkContent
import com.revotech.business.work_content.exception.WorkContentException
import com.revotech.business.work_content.repository.WorkContentRepository
import com.revotech.util.WebUtil
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Service
class WorkContentService(
    private val workContentRepository: WorkContentRepository,
    private val bookingFlightService: BookingFlightService,
    private val webUtil: WebUtil
) {
    fun saveWorkContent(saveWorkContentReq: SaveWorkContentReq): Boolean {

        val userId = webUtil.getUserId()

        if (saveWorkContentReq.id == null) {
            validate(
                saveWorkContentReq = saveWorkContentReq,
                isCreate = true,
                currentWorkContentToUpdate = null
            )
            workContentRepository.save(
                WorkContent(
                    code = saveWorkContentReq.code,
                    content = saveWorkContentReq.content,
                    timeFrom = LocalDate.parse(saveWorkContentReq.timeFrom),
                    timeTo = LocalDate.parse(saveWorkContentReq.timeTo),
                    openTicketRegistration = saveWorkContentReq.openTicketRegistration ?: false,
                    isDeleted = false
                ).apply {
                    createdBy = userId
                }
            )
        } else {
            val currentWorkContentToUpdate = findWorkContentById(saveWorkContentReq.id!!)
            validate(
                saveWorkContentReq = saveWorkContentReq,
                isCreate = false,
                currentWorkContentToUpdate = currentWorkContentToUpdate
            )
            workContentRepository.save(
                currentWorkContentToUpdate.apply {
                    code = saveWorkContentReq.code
                    content = saveWorkContentReq.content
                    timeFrom = LocalDate.parse(saveWorkContentReq.timeFrom)
                    timeTo = LocalDate.parse(saveWorkContentReq.timeTo)
                    openTicketRegistration = saveWorkContentReq.openTicketRegistration ?: false
                    lastModifiedBy = userId
                }
            )
        }

        return true
    }

    fun validate(
        saveWorkContentReq: SaveWorkContentReq,
        isCreate: Boolean,
        currentWorkContentToUpdate: WorkContent?
    ) {
        if (saveWorkContentReq.code.isNullOrBlank()) {
            throw WorkContentException("WorkContentCodeRequired", "Work content code is required")
        }

        if (saveWorkContentReq.content.isNullOrBlank()) {
            throw WorkContentException("WorkContentContentRequired", "Work content content is required")
        }

        if (!isCreate) {
            if (saveWorkContentReq.code != currentWorkContentToUpdate?.code) {
                isCodeWorkContentExisted(saveWorkContentReq.code!!)
            }
        } else {
            isCodeWorkContentExisted(saveWorkContentReq.code!!)
        }

        if (saveWorkContentReq.timeFrom != null && saveWorkContentReq.timeTo != null) {
            if (!isValidDateFormat(saveWorkContentReq.timeFrom)) {
                throw WorkContentException("InvalidDateFormat", "Start date is not in yyyy-MM-dd format!")
            }

            if (!isValidDateFormat(saveWorkContentReq.timeTo)) {
                throw WorkContentException("InvalidDateFormat", "End date is not in yyyy-MM-dd format!")
            }

            val fromDate = LocalDate.parse(saveWorkContentReq.timeFrom)
            val toDate = LocalDate.parse(saveWorkContentReq.timeTo)

            if (fromDate.isAfter(toDate)) {
                throw WorkContentException("InvalidDateRange", "Start date must be before or equal to end date")
            }
        }
    }

    fun isValidDateFormat(value: String?): Boolean {
        return try {
            value?.let {
                LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
                true
            } ?: false
        } catch (ex: DateTimeParseException) {
            false
        }
    }

    fun isCodeWorkContentExisted(code: String) {
        val isExisted = workContentRepository.existsByCodeAndIsDeletedFalse(code)
        if (isExisted) {
            throw WorkContentException("WorkContentCodeExisted", "Work content code is existed!")
        }
    }

    fun findWorkContentById(id: String): WorkContent {
        return workContentRepository.findByIdAndIsDeletedFalse(id)
            ?: throw WorkContentException("WorkContentNotFound", "Work content not found!")
    }

    fun searchWorkContent(searchInput: SearchInput, pageable: Pageable): SearchWorkContentResult {
        val listWorkContent = workContentRepository.searchWorkContent(
            searchInput.textSearch, pageable
        )

        val mappedListWorkContent = listWorkContent.content.map { item ->
            WorkContentList(
                id = item.getId(),
                code = item.getCode(),
                content = item.getContent(),
                timeFrom = item.getTimeFrom().toString(),
                timeTo = item.getTimeTo().toString(),
                openTicketRegistration = item.getOpenTicketRegistration()
            )
        }

        return SearchWorkContentResult(
            content = mappedListWorkContent,
            page = listWorkContent.number + 1,
            pageSize = listWorkContent.size,
            totalRecords = listWorkContent.totalElements.toInt(),
            totalPages = listWorkContent.totalPages
        )
    }

    fun getDetailWorkContentById(id: String): WorkContentDetail {

        val workContentDetail = workContentRepository.getDetailWorkContentById(id)
            ?: throw WorkContentException("WorkContentNotFound", "Work content not found!")

        return WorkContentDetail(
            id = workContentDetail.getId(),
            code = workContentDetail.getCode(),
            content = workContentDetail.getContent(),
            timeFrom = workContentDetail.getTimeFrom().toString(),
            timeTo = workContentDetail.getTimeTo().toString(),
            openTicketRegistration = workContentDetail.getOpenTicketRegistration()
        )
    }

    fun deleteWorkContent(id: String): Boolean {

        val currentWorkContent = findWorkContentById(id)

        val isUsingInSomeBookingFlightTicket = bookingFlightService.existByWorkContentId(currentWorkContent.id!!)

        if (isUsingInSomeBookingFlightTicket) {
            throw WorkContentException("WorkContentCannotDelete", "Work content can't delete becase it being use in a booking flight ticket")
        }

        currentWorkContent.isDeleted = true

        workContentRepository.save(currentWorkContent)

        return true
    }

    fun getAllWorkContent(): List<WorkContentList> {
        val allWorkContent = workContentRepository.getAllWorkContent()
        return allWorkContent.map { item ->
            WorkContentList(
                id = item.getId(),
                code = item.getCode(),
                content = item.getContent(),
                timeFrom = item.getTimeFrom().toString(),
                timeTo = item.getTimeTo().toString(),
                openTicketRegistration = item.getOpenTicketRegistration()
            )
        }
    }
}