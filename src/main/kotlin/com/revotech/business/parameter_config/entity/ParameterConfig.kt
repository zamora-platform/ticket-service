package com.revotech.business.parameter_config.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.revotech.audit.JpaActivityInfo
import com.revotech.business.parameter_config.dto.BookingCodeSettingsInput
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType
import jakarta.persistence.*
import org.hibernate.annotations.Type

@Entity
@Table(name = "t_parameter_config")
@JsonInclude(JsonInclude.Include.NON_NULL)
class ParameterConfig(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var value: ParameterValue? = null
) : JpaActivityInfo() {
    constructor() : this(null, null)
}

data class ParameterValue(
    var code: BookingCodeSettingsInput? = null,
)