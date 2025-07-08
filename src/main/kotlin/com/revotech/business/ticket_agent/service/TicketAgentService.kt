package com.revotech.business.ticket_agent.service

import com.revotech.business.ticket_agent.dto.*
import com.revotech.business.ticket_agent.entity.TicketAgent
import com.revotech.business.ticket_agent.entity.TicketAgentContact
import com.revotech.business.ticket_agent.entity.TicketAgentStatus
import com.revotech.business.ticket_agent.exception.TicketAgentException
import com.revotech.business.ticket_agent.exception.TicketAgentNotFoundException
import com.revotech.business.ticket_agent.repository.TicketAgentContactRepository
import com.revotech.business.ticket_agent.repository.TicketAgentRepository
import com.revotech.util.WebUtil
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TicketAgentService(
    private val ticketAgentRepository: TicketAgentRepository,
    private val ticketAgentContactRepository: TicketAgentContactRepository,
    private val webUtil: WebUtil
) {
    @Transactional
    fun saveTicketAgent(saveTicketAgentReq: SaveTicketAgentReq): Boolean {

        val userId = webUtil.getUserId()

        if (saveTicketAgentReq.id == null) {
            validate(
                saveTicketAgentReq = saveTicketAgentReq,
                isCreate = true,
                currentTicketAgentToUpdate = null
            )

            // CREATE
            val newTicketAgent = ticketAgentRepository.save(
                TicketAgent(
                    code = saveTicketAgentReq.code,
                    name = saveTicketAgentReq.name,
                    adddress = saveTicketAgentReq.address,
                    taxCode = saveTicketAgentReq.taxCode,
                    phone = saveTicketAgentReq.phone,
                    email = saveTicketAgentReq.email,
                    note = saveTicketAgentReq.note,
                    status = TicketAgentStatus.WORKING
                ).apply {
                    createdBy = userId
                }
            )

            if (saveTicketAgentReq.ticketAgentContact?.isEmpty() == false) {
                val ticketAgentContactList = saveTicketAgentReq.ticketAgentContact!!

                ticketAgentContactRepository.saveAll(
                    ticketAgentContactList.map {
                        TicketAgentContact(
                            name = it.ticketAgentContactName,
                            email = it.ticketAgentContactEmail,
                            phone = it.ticketAgentContactPhone,
                            ticketAgentId = newTicketAgent.id
                        )
                    }
                )
            }

        } else {
            val currentTicketAgentToUpdate = findTicketAgentById(saveTicketAgentReq.id!!)
            validate(
                saveTicketAgentReq = saveTicketAgentReq,
                isCreate = true,
                currentTicketAgentToUpdate = currentTicketAgentToUpdate
            )

            // UPDATE
            currentTicketAgentToUpdate.apply {
                code = saveTicketAgentReq.code
                name = saveTicketAgentReq.name
                adddress = saveTicketAgentReq.address
                taxCode = saveTicketAgentReq.taxCode
                phone = saveTicketAgentReq.phone
                email = saveTicketAgentReq.email
                note = saveTicketAgentReq.note
                lastModifiedBy = userId
            }

            ticketAgentContactRepository.deleteAllByTicketAgentId(currentTicketAgentToUpdate.id!!)

            if (saveTicketAgentReq.ticketAgentContact?.isEmpty() == false) {
                val ticketAgentContactList = saveTicketAgentReq.ticketAgentContact!!

                ticketAgentContactRepository.saveAll(
                    ticketAgentContactList.map {
                        TicketAgentContact(
                            name = it.ticketAgentContactName,
                            email = it.ticketAgentContactEmail,
                            phone = it.ticketAgentContactPhone,
                            ticketAgentId = currentTicketAgentToUpdate.id
                        )
                    }
                )
            }
        }

        return true
    }

    fun validate(
        saveTicketAgentReq: SaveTicketAgentReq,
        isCreate: Boolean,
        currentTicketAgentToUpdate: TicketAgent?,
    ) {
        if (saveTicketAgentReq.code.isNullOrBlank()) {
            throw TicketAgentException("TicketAgentCodeRequired", "Ticket agent code is required!")
        }

        if (saveTicketAgentReq.name.isNullOrBlank()) {
            throw TicketAgentException("TicketAgentNameRequired", "Ticket agent name is required!")
        }

        if (!isCreate) {
            if (saveTicketAgentReq.code != currentTicketAgentToUpdate?.code) {
                throw TicketAgentException("TicketAgentCodeExist", "Ticket agent code is existed!")
            }
            if (saveTicketAgentReq.name != currentTicketAgentToUpdate?.name) {
                throw TicketAgentException("TicketAgentNameExist", "Ticket agent name is existed!")
            }
        } else {
            isCodeTicketAgentExisted(saveTicketAgentReq.code!!)
            isNameTicketAgentExisted(saveTicketAgentReq.name!!)
        }
    }

    fun findTicketAgentById(id: String): TicketAgent {
        val ticketAgent = ticketAgentRepository.findTicketAgentById(id) ?: throw TicketAgentNotFoundException(
            "TicketAgentNotFound",
            "Ticket agent not found!"
        )
        return ticketAgent
    }

    fun isCodeTicketAgentExisted(code: String) {
        val isExisted = ticketAgentRepository.existsByCode(code)
        if (isExisted) {
            throw TicketAgentException("TicketAgentCodeExist", "Ticket agent code is existed!")
        }
    }

    fun isNameTicketAgentExisted(name: String) {
        val isExisted = ticketAgentRepository.existsByName(name)
        if (isExisted) {
            throw TicketAgentException("TicketAgentNameExist", "Ticket agent name is existed!")
        }
    }

    fun searchTicketAgent(searchInput: SearchInput, pageable: Pageable): SearchTicketAgentResult {
        val ticketAgentList = ticketAgentRepository.searchTicketAgent(searchInput.textSearch, pageable)

        val ticketAgentContactList = if (ticketAgentList.content.isNotEmpty()) {
            ticketAgentContactRepository.findContactsByAgentIds(ticketAgentList.content.map { it.getId() })
        } else {
            emptyList()
        }

        val ticketAgentContactMap = ticketAgentContactList.groupBy { it.getTicketAgentId() }

        val ticketAgentListMap = ticketAgentList.content.map { agentList ->
            TicketAgentList(
                id = agentList.getId(),
                code = agentList.getCode(),
                name = agentList.getName(),
                address = agentList.getAddress(),
                taxCode = agentList.getTaxCode(),
                phone = agentList.getPhone(),
                email = agentList.getEmail(),
                note = agentList.getNote(),
                status = agentList.getStatus(),
                createdBy = agentList.getCreatedBy(),
                createdTime = agentList.getCreatedTime(),
                ticketAgentContact = ticketAgentContactMap[agentList.getId()]?.map { agentContactList ->
                    TicketAgentContactList(
                        ticketAgentContactId = agentContactList.getTicketAgentId(),
                        ticketAgentContactName = agentContactList.getName(),
                        ticketAgentContactEmail = agentContactList.getEmail(),
                        ticketAgentContactPhone = agentContactList.getPhone()
                    )
                }
            )
        }

        return SearchTicketAgentResult(
            content = ticketAgentListMap,
            page = ticketAgentList.number + 1,
            pageSize = ticketAgentList.size,
            totalRecords = ticketAgentList.totalElements.toInt(),
            totalPages = ticketAgentList.totalPages
        )
    }
}