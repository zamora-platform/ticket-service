package com.revotech.business.book_flight.service

import com.revotech.business.airline.service.AirlineService
import com.revotech.business.airport.service.AirportService
import com.revotech.business.attachment.entity.AttachmentType
import com.revotech.business.attachment.repository.TicketAttachmentRepository
import com.revotech.business.attachment.service.TicketAttachmentService
import com.revotech.business.book_flight.dto.BookingFlightAttachmentsDetail
import com.revotech.business.book_flight.dto.BookingFlightDetail
import com.revotech.business.book_flight.dto.SaveBookingFlightReq
import com.revotech.business.book_flight.entity.BookingFlight
import com.revotech.business.book_flight.entity.BookingFlightAttachment
import com.revotech.business.book_flight.exception.BookingFlightException
import com.revotech.business.book_flight.exception.BookingFlightNotFoundException
import com.revotech.business.book_flight.repository.BookingFlightAttachmentRepository
import com.revotech.business.book_flight.repository.BookingFlightRepository
import com.revotech.business.country.service.CountryService
import com.revotech.business.work_content.service.WorkContentService
import com.revotech.client.AdminServiceClient
import com.revotech.util.WebUtil
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Service
class BookingFlightService(
    private val bookingFlightRepository: BookingFlightRepository,
    private val bookingFlightAttachmentRepository: BookingFlightAttachmentRepository,
    private val ticketAttachmentRepository: TicketAttachmentRepository,
    private val ticketAttachmentService: TicketAttachmentService,
    private val workContentService: WorkContentService,
    private val countryService: CountryService,
    private val airportService: AirportService,
    private val airlineService: AirlineService,
    private val adminServiceClient: AdminServiceClient,
    private val webUtil: WebUtil
) {

    @Transactional
    fun saveBookingFlight(saveBookingFlightReq: SaveBookingFlightReq): Boolean {

        val userId = webUtil.getUserId()
        var newBookingFlight: BookingFlight? = null
        var currentBookingFlightToUpdate: BookingFlight? = null

        if (saveBookingFlightReq.id.isNullOrEmpty()) {
            // CREATE
            validate(
                req = saveBookingFlightReq,
                isCreate = true,
                currentBookingFlightToUpdate = null
            )
            newBookingFlight = bookingFlightRepository.save(
                BookingFlight(
                    requestNumber = saveBookingFlightReq.requestNumber,
                    officerId = saveBookingFlightReq.officerId,
                    goldenLotusCode = saveBookingFlightReq.goldenLotusCode,
                    workContentId = saveBookingFlightReq.workContentId,
                    flightType = saveBookingFlightReq.flightType,
                    cityId = saveBookingFlightReq.cityId,
                    flightDate = LocalDate.parse(saveBookingFlightReq.flightDate),
                    departureAirportId = saveBookingFlightReq.departureAirportId,
                    airportToDepartureId = saveBookingFlightReq.airportToDepartureId,
                    returnFlightDate = saveBookingFlightReq.returnFlightDate
                        ?.takeIf { it.isNotBlank() }
                        ?.let { LocalDate.parse(it) },
                    airportDepartureReturnId = saveBookingFlightReq.airportDepartureReturnId,
                    airportToReturnId = saveBookingFlightReq.airportToReturnId,
                    requestType = saveBookingFlightReq.requestType,
                    departureTime = saveBookingFlightReq.departureTime
                        ?.takeIf { it.isNotBlank() }
                        ?.let { LocalTime.parse(it) },
                    outboundFlightNumber = saveBookingFlightReq.outboundFlightNumber,
                    airlineDepartureId = saveBookingFlightReq.airlineDepartureId,
                    returnFlightTime = saveBookingFlightReq.returnFlightTime
                        ?.takeIf { it.isNotBlank() }
                        ?.let { LocalTime.parse(it) },
                    returnFlightNumber = saveBookingFlightReq.returnFlightNumber,
                    airlineReturnId = saveBookingFlightReq.airlineReturnId,
                    flightScheduleDescription = saveBookingFlightReq.flightScheduleDescription,
                    isDeleted = false
                ).apply {
                    createdBy = userId
                    createdTime = parseLocalDateTimeWithDefaultTime(saveBookingFlightReq.createdDate!!)
                }
            )
        } else {
            // UPDATE
            currentBookingFlightToUpdate = findBookingFlightById(saveBookingFlightReq.id!!)
            validate(
                req = saveBookingFlightReq,
                isCreate = false,
                currentBookingFlightToUpdate = currentBookingFlightToUpdate
            )
            bookingFlightRepository.save(
                currentBookingFlightToUpdate.apply {
                    requestNumber = saveBookingFlightReq.requestNumber
                    officerId = saveBookingFlightReq.officerId
                    goldenLotusCode = saveBookingFlightReq.goldenLotusCode
                    workContentId = saveBookingFlightReq.workContentId
                    flightType = saveBookingFlightReq.flightType
                    cityId = saveBookingFlightReq.cityId
                    flightDate = LocalDate.parse(saveBookingFlightReq.flightDate)
                    departureAirportId = saveBookingFlightReq.departureAirportId
                    airportToDepartureId = saveBookingFlightReq.airportToDepartureId
                    returnFlightDate = saveBookingFlightReq.returnFlightDate
                        ?.takeIf { it.isNotBlank() }
                        ?.let { LocalDate.parse(it) }
                    airportDepartureReturnId = saveBookingFlightReq.airportDepartureReturnId
                    airportToReturnId = saveBookingFlightReq.airportToReturnId
                    requestType = saveBookingFlightReq.requestType
                    departureTime = saveBookingFlightReq.departureTime
                        ?.takeIf { it.isNotBlank() }
                        ?.let { LocalTime.parse(it) }
                    outboundFlightNumber = saveBookingFlightReq.outboundFlightNumber
                    airlineDepartureId = saveBookingFlightReq.airlineDepartureId
                    returnFlightTime = saveBookingFlightReq.returnFlightTime
                        ?.takeIf { it.isNotBlank() }
                        ?.let { LocalTime.parse(it) }
                    airlineReturnId = saveBookingFlightReq.airlineReturnId
                    flightScheduleDescription = saveBookingFlightReq.flightScheduleDescription
                }
            )
        }

        val bookingFlightId = newBookingFlight?.id ?: currentBookingFlightToUpdate?.id
        ?: throw IllegalStateException("ID Booking undefined!")

        // UPLOAD FILES
        saveBookingFlightReq.files?.forEach { file ->
            if (!file.id.isNullOrBlank()) {
                val existingAttachment = bookingFlightAttachmentRepository.findById(file.id!!)
                    ?: throw BookingFlightNotFoundException("BookingFlightAttachmentNotFound", "Attachment not found")
                bookingFlightAttachmentRepository.delete(existingAttachment.get())
                val newBookingFlightAttachmentUpdateCase = bookingFlightAttachmentRepository.save(
                    BookingFlightAttachment(
                        bookingFlightId = bookingFlightId,
                        quote = file.quote,
                        isDeleted = false
                    )
                )
                if (file.attachment != null) {
                    ticketAttachmentRepository.deleteByObjectId(existingAttachment.get().id!!)
                    ticketAttachmentService.uploadFileToTicketAttachment(
                        listOf(file.attachment!!), newBookingFlightAttachmentUpdateCase.id!!, AttachmentType.BOOKING
                    )
                }
            } else {
                val newBookingFlightAttachmentCreateCase = bookingFlightAttachmentRepository.save(
                    BookingFlightAttachment(
                        bookingFlightId = bookingFlightId,
                        quote = file.quote,
                        isDeleted = false
                    )
                )
                ticketAttachmentService.uploadFileToTicketAttachment(
                    listOf(file.attachment!!), newBookingFlightAttachmentCreateCase.id!!, AttachmentType.BOOKING
                )
            }
        }

        return true
    }

    fun findBookingFlightById(id: String): BookingFlight {
        return bookingFlightRepository.findBookingFlightsByIdAndIsDeletedFalse(id)
            ?: throw BookingFlightNotFoundException("BookingFlightNotFound", "Booking flight not found!")
    }

    fun validate(
        req: SaveBookingFlightReq,
        isCreate: Boolean,
        currentBookingFlightToUpdate: BookingFlight?
    ) {
        val formatterLocalDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // Số phiếu
        if (req.requestNumber.isNullOrBlank()) {
            throw BookingFlightException("RequestNumberRequired", "Request number is required!")
        }

        if (!isCreate) {
            if (req.requestNumber != currentBookingFlightToUpdate?.requestNumber) {
                existByRequestNumber(req.requestNumber!!)
            }
        } else {
            existByRequestNumber(req.requestNumber!!)
        }

        // Ngày lập
        if (req.createdDate.isNullOrBlank()) {
            throw BookingFlightException("CreatedDateRequired", "Created date is required!")
        } else {
            validateDateFormat(req.createdDate!!)
            val createdDate = LocalDate.parse(req.createdDate, formatterLocalDate)
            if (createdDate.isBefore(LocalDate.now())) {
                throw BookingFlightException("CreatedDateInPast", "Created date must not be in the past")
            }
        }

        // Tên cán bộ (ID)
        if (req.officerId.isNullOrBlank()) {
            throw BookingFlightException("OfficerRequired", "Officer is required!")
        } else {
            val officer = adminServiceClient.getUsersCache(webUtil.getHeaders(), listOf(req.officerId!!))
            if (officer.isEmpty()) {
                throw BookingFlightException("OfficerNotFound", "Officer not found!")
            }
        }

        // Nội dung làm việc
        if (!req.workContentId.isNullOrBlank()) {
            workContentService.findWorkContentById(req.workContentId!!)
        }

        // Thành phố
        if (!req.cityId.isNullOrBlank()) {
            countryService.findCityById(req.cityId!!)
        }

        // Ngày bay đi
        if (req.flightDate.isNullOrBlank()) {
            throw BookingFlightException("FlightDateRequired", "Flight date is required!")
        } else {
            if (LocalDate.parse(req.flightDate, formatterLocalDate).isBefore(LocalDate.now())) {
                throw BookingFlightException("FlightDateInPast", "Flight date must not be in the past")
            }
            validateDateFormat(req.flightDate!!)
        }

        // Sân bay khởi hành chiều đi
        if (req.departureAirportId.isNullOrBlank()) {
            throw BookingFlightException("DepartureAirportRequired", "Departure airport is required!")
        } else {
            airportService.findAirportById(req.departureAirportId!!)
        }

        // Sân bay đến chiều đi
        if (req.airportToDepartureId.isNullOrBlank()) {
            throw BookingFlightException("AirportToDepartureRequired", "Airport to departure is required!")
        } else {
            airportService.findAirportById(req.airportToDepartureId!!)
        }

        // Ngày bay về
        val returnFlightDate: LocalDate? = if (!req.returnFlightDate.isNullOrBlank()) {
            validateDateFormat(req.returnFlightDate!!)
            val parsed = LocalDate.parse(req.returnFlightDate, formatterLocalDate)
            if (parsed.isBefore(LocalDate.now())) {
                throw BookingFlightException("ReturnFlightDateInPast", "Return flight date must not be in the past")
            }
            parsed
        } else {
            null
        }

        // Ngày bay đi + Ngày bay về
        if (!req.flightDate.isNullOrBlank()) {
            val flightDate = LocalDate.parse(req.flightDate, formatterLocalDate)
            if (flightDate.isBefore(LocalDate.now())) {
                throw BookingFlightException("FlightDateInPast", "Flight date must not be in the past")
            }
            if (returnFlightDate != null && flightDate.isAfter(returnFlightDate)) {
                throw BookingFlightException("FlightDateAfterReturnDate", "Flight date must be before or equal to return flight date")
            }
        }

        // Sân bay khởi hành chiều về
        if (!req.airportDepartureReturnId.isNullOrBlank()) {
            airportService.findAirportById(req.airportDepartureReturnId!!)
        }

        // Sân bay đến chiều về
        if (!req.airportToReturnId.isNullOrBlank()) {
            airportService.findAirportById(req.airportToReturnId!!)
        }

        // Giờ bay chiều đi
        if (!req.departureTime.isNullOrBlank()) {
            validateTimeFormat(req.departureTime!!)
        }

        // Hãng hàng không chiều đi
        if (!req.airlineDepartureId.isNullOrBlank()) {
            airlineService.findAirlineById(req.airlineDepartureId!!)
        }

        // Giờ bay chiều về
        if (!req.returnFlightTime.isNullOrBlank()) {
            validateTimeFormat(req.returnFlightTime!!)
        }

        // Hãng hàng không chiều về
        if (!req.airlineReturnId.isNullOrBlank()) {
            airlineService.findAirlineById(req.airlineReturnId!!)
        }
    }

    fun validateDateFormat(dateStr: String, pattern: String = "yyyy-MM-dd") {
        try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            LocalDate.parse(dateStr, formatter)
        } catch (e: DateTimeParseException) {
            throw BookingFlightException("InvalidDateFormat", "Date must match format $pattern!")
        }
    }

    fun validateTimeFormat(timeStr: String, pattern: String = "HH:mm") {
        try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            LocalTime.parse(timeStr, formatter)
        } catch (e: DateTimeParseException) {
            throw BookingFlightException("InvalidTimeFormat", "Time must match format $pattern!")
        }
    }

    fun parseLocalDateTimeWithDefaultTime(input: String): LocalDateTime {
        val paddedInput = if (input.length == 10) "$input 00:00:00" else input
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.parse(paddedInput, formatter)
    }

    fun existByRequestNumber(requestNumber: String) {
        val existed = bookingFlightRepository.existsByRequestNumberAndIsDeletedFalse(requestNumber)
        if (existed) {
            throw BookingFlightException("RequestNumberExisted", "Request number is already in use!")
        }
    }

    fun existByWorkContentId(workContentId: String): Boolean {
        return bookingFlightRepository.existsByWorkContentIdAndIsDeletedFalse(workContentId)
    }

    fun existByCityId(cityId: String): Boolean {
        return bookingFlightRepository.existsByCityIdAndIsDeletedFalse(cityId)
    }

    fun existByAirportId(airportId: String): Boolean {
        return bookingFlightRepository.existsByDepartureAirportIdAndIsDeletedFalse(airportId) &&
                bookingFlightRepository.existsByAirportToDepartureIdAndIsDeletedFalse(airportId) &&
                bookingFlightRepository.existsByAirportDepartureReturnIdAndIsDeletedFalse(airportId) &&
                bookingFlightRepository.existsByAirportToReturnIdAndIsDeletedFalse(airportId)
    }

    fun existByAirlineId(airlineId: String): Boolean {
        return bookingFlightRepository.existsByAirlineDepartureIdAndIsDeletedFalse(airlineId) &&
                bookingFlightRepository.existsByAirlineReturnId(airlineId)
    }

    fun getDetailBookingFlightById(id: String): BookingFlightDetail {

        val bookingFlightDetail = bookingFlightRepository.getDetailBookingFlightById(id)
            ?: throw BookingFlightNotFoundException("BookingFlightNotFound", "Booking flight not found!")

        val bookingFlightAttachments = bookingFlightAttachmentRepository.findAllByBookingFlightIdAndIsDeletedFalse(
            bookingFlightDetail.getId()!!
        )

        val attachmentFilesOfBookingFlight = bookingFlightAttachments.flatMap { bookingAttachment ->
            ticketAttachmentRepository.findByObjectIdAndObjectTypeAndIsDeletedFalseAndParentIdIsNull(
                bookingAttachment.id!!, AttachmentType.BOOKING
            ).map { file ->
                BookingFlightAttachmentsDetail(
                    id = bookingAttachment.id,
                    quote = bookingAttachment.quote,
                    attachment = file.name,
                    size = file.size.toString(),
                    downloadPath = file.downloadPath
                )
            }
        }

        return BookingFlightDetail(
            id = bookingFlightDetail.getId(),
            requestNumber = bookingFlightDetail.getRequestNumber(),
            createdDate = bookingFlightDetail.getCreatedDate()?.toLocalDate(),
            officerId = bookingFlightDetail.getOfficerId(),
            goldenLotusCode = bookingFlightDetail.getGoldenLotusCode(),
            workContentId = bookingFlightDetail.getWorkContentId(),
            workContentCode = bookingFlightDetail.getWorkContentCode(),
            workContentContent = bookingFlightDetail.getWorkContentContent(),
            flightType = bookingFlightDetail.getFlightType(),
            cityId = bookingFlightDetail.getCityId(),
            cityName = bookingFlightDetail.getCityName(),
            flightDate = bookingFlightDetail.getFlightDate(),
            departureAirportId = bookingFlightDetail.getDepartureAirportId(),
            departureAirportName = bookingFlightDetail.getDepartureAirportName(),
            airportToDepartureId = bookingFlightDetail.getAirportToDepartureId(),
            airportToDepartureName = bookingFlightDetail.getAirportToDepartureName(),
            returnFlightDate = bookingFlightDetail.getReturnFlightDate(),
            airportDepartureReturnId = bookingFlightDetail.getAirportDepartureReturnId(),
            airportDepartureReturnName = bookingFlightDetail.getAirportDepartureReturnName(),
            airportToReturnId = bookingFlightDetail.getAirportToReturnId(),
            airportToReturnName = bookingFlightDetail.getAirportToReturnName(),
            requestType = bookingFlightDetail.getRequestType(),
            departureTime = bookingFlightDetail.getDepartureTime(),
            outboundFlightNumber = bookingFlightDetail.getOutboundFlightNumber(),
            airlineDepartureId = bookingFlightDetail.getAirlineDepartureId(),
            airlineDepartureName = airlineService.findAirlineById(bookingFlightDetail.getAirlineDepartureId()!!).name,
            returnFlightTime = bookingFlightDetail.getReturnFlightTime(),
            returnFlightNumber = bookingFlightDetail.getReturnFlightNumber(),
            airlineReturnId = bookingFlightDetail.getAirlineReturnId(),
            airlineReturnName = airlineService.findAirlineById(bookingFlightDetail.getAirlineReturnId()!!).name,
            flightScheduleDescription = bookingFlightDetail.getFlightScheduleDescription(),
            attachments = attachmentFilesOfBookingFlight
        )
    }
}