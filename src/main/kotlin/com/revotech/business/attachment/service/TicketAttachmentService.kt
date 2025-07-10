package com.revotech.business.attachment.service

import com.revotech.business.attachment.entity.AttachmentType
import com.revotech.business.attachment.entity.TicketAttachment
import com.revotech.business.attachment.exeption.AttachmentException
import com.revotech.business.attachment.repository.TicketAttachmentRepository
import com.revotech.client.FileServiceClient
import com.revotech.util.WebUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

@Service
class TicketAttachmentService(
    private val ticketAttachmentRepository: TicketAttachmentRepository,
    private val fileServiceClient: FileServiceClient,
    private val webUtil: WebUtil
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    fun uploadFileToTicketAttachment(
        attachmentFiles: List<MultipartFile>?,
        attachmentId: String,
        attachmentType: AttachmentType
    ): Boolean {
        attachmentFiles?.map {
            if (it.isEmpty || it.size < 0) return false
            try {
                val filePath = fileServiceClient.upload(
                    webUtil.getHeaders(),
                    it,
                    it.originalFilename,
                    getPath(attachmentId)
                )

                ticketAttachmentRepository.save(
                    TicketAttachment(
                        name = it.resource.filename!!,
                        parentPath = filePath,
                        size = it.size,
                        objectId = attachmentId,
                        objectType = attachmentType,
                        isFolder = false,
                        parentId = null,
                        extension = StringUtils.getFilenameExtension(it.originalFilename),
                        downloadPath = filePath
                    )
                )
            } catch (e: Exception) {
                log.warn("Fail to upload file ${it.originalFilename}", e)
                return false
            }
        }
        return true
    }

    private fun getPath(objectId: String): String {
        return "ticket/$objectId"
    }

    fun findLogoAirlineFileById(id: String): TicketAttachment {
        return ticketAttachmentRepository.findLogoAirlineFileById(id)
            ?: throw AttachmentException("LogoAirlineNotFound", "Logo airline not found!")
    }
}