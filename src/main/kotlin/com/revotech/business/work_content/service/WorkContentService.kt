package com.revotech.business.work_content.service

import com.revotech.business.work_content.dto.SaveWorkContentReq
import com.revotech.business.work_content.entity.WorkContent
import com.revotech.business.work_content.exception.WorkContentException
import com.revotech.business.work_content.repository.WorkContentRepository
import com.revotech.util.WebUtil
import org.springframework.cglib.core.Local
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Service
class WorkContentService(
    private val workContentRepository: WorkContentRepository,
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
}