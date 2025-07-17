package com.revotech.business.airport.entity

import com.revotech.audit.JpaActivityInfo
import jakarta.persistence.*

@Entity
@Table(name = "t_airport")
class Airport(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    var code: String? = null,
    var name: String? = null,
    var countryId: String? = null,
    var cityId: String? = null,
    var sortOrder: Int? = null,
    var isDefault: Boolean? = null,
    @Enumerated(EnumType.STRING)
    var status: AirportStatus? = null
) : JpaActivityInfo()

enum class AirportStatus {
    WORKING,
    NOT_WORKING
}