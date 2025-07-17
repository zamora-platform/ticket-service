package com.revotech.business.parameter_config.service

import com.revotech.business.parameter_config.dto.*
import com.revotech.business.parameter_config.entity.ParameterConfig
import com.revotech.business.parameter_config.entity.ParameterValue
import com.revotech.business.parameter_config.exception.ParameterConfigException
import com.revotech.business.parameter_config.repository.ParameterConfigRepository
import com.revotech.client.AdminServiceClient
import com.revotech.util.TemplateProcessor
import com.revotech.util.WebUtil
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ParameterConfigService(
    private val parameterConfigRepository: ParameterConfigRepository,
    private val webUtil: WebUtil,
    private val adminServiceClient: AdminServiceClient
) {
    @Transactional
    fun saveParameterBookingConfig(parameterBookingConfigInput: ParameterBookingConfigInput): ParameterBookingConfigResultType {

        val existingConfig = findParameterConfig()

        if (parameterBookingConfigInput.value?.code != null) {
            parameterBookingConfigInput.value.code.codeStructure?.let {
                if (!isCorrectTemplateCode(it)) {
                    throw ParameterConfigException("CodeStructureInvalid", "Invalid code structure! Check again!")
                }
            }

            existingConfig.apply {
                value?.code = BookingCodeSettingsInput(
                    enabledGenerateCode = parameterBookingConfigInput.value.code.enabledGenerateCode,
                    codeStructure = parameterBookingConfigInput.value.code.codeStructure,
                    startValue = parameterBookingConfigInput.value.code.startValue,
                    allowEdit = parameterBookingConfigInput.value.code.allowEdit
                )
            }
        }

        val savedConfig = parameterConfigRepository.save(existingConfig)

        return ParameterBookingConfigResultType(
            id = savedConfig.id,
            value = ParameterBookingValueResultType(
                code = BookingCodeSettingsResultType(
                    enabledGenerateCode = savedConfig.value?.code?.enabledGenerateCode,
                    codeStructure = savedConfig.value?.code?.codeStructure,
                    startValue = savedConfig.value?.code?.startValue,
                    allowEdit = savedConfig.value?.code?.allowEdit
                )
            )
        )
    }

    fun getParameterBookingConfig() : ParameterBookingConfigResultType {
        val parameterConfig = findParameterConfig()
        return ParameterBookingConfigResultType(
            id = parameterConfig.id,
            value = ParameterBookingValueResultType(
                code = BookingCodeSettingsResultType(
                    enabledGenerateCode = parameterConfig.value?.code?.enabledGenerateCode,
                    codeStructure = parameterConfig.value?.code?.codeStructure,
                    startValue = parameterConfig.value?.code?.startValue,
                    allowEdit = parameterConfig.value?.code?.allowEdit
                )
            )
        )
    }

    fun getBookingRequestCode(): String? {
        val codeSettings = parameterConfigRepository.findAll().firstOrNull()?.value?.code
        return processCode(codeSettings)
    }

    private fun processCode(codeSettings: BookingCodeSettingsInput?): String? {
        if (codeSettings?.codeStructure != null) {
            val user = adminServiceClient.getUserCache(webUtil.getHeaders(), webUtil.getUserId())
            val data = mapOf<String, Any?>(
                "date" to LocalDate.now(),
                "count" to codeSettings.startValue,
                "user_id" to user.username,
                "dept_id" to (user.departments?.firstOrNull()?.code ?: "")
            )
            return TemplateProcessor(data).process(codeSettings.codeStructure)
        }
        return null
    }

    fun findParameterConfig(): ParameterConfig {
        return parameterConfigRepository.findAll().firstOrNull() ?: saveDefaultParameters()
    }

    private fun isCorrectTemplateCode(templateCode: String): Boolean {
        val data = mapOf(
            "date" to LocalDate.of(2000, 1, 1), "count" to 1, "user_id" to "userId", "dept_id" to "deptId"
        )
        TemplateProcessor(data).process(templateCode)
        return true
    }

    private fun saveDefaultParameters(): ParameterConfig {
        val defaultParameterConfig = ParameterConfig(
            value = ParameterValue(
                code = BookingCodeSettingsInput(
                    enabledGenerateCode = true,
                    codeStructure = null,
                    startValue = null,
                    allowEdit = false
                )
            )
        ).apply {
            createdBy = webUtil.getUserId()
        }
        return parameterConfigRepository.save(defaultParameterConfig)
    }

    fun increaseBookingRequestCodeStartValue(): Boolean {
        val config = parameterConfigRepository.findAll().firstOrNull() ?: return false
        val codeSettings = config.value?.code
        return processStartValue(config, codeSettings)
    }

    private fun processStartValue(config: ParameterConfig, codeSettings: BookingCodeSettingsInput?): Boolean {
        return if (codeSettings?.startValue == null) {
            false
        } else {
            codeSettings.startValue = codeSettings.startValue!! + 1
            parameterConfigRepository.save(config)
            true
        }
    }
}
