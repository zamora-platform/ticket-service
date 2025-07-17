package com.revotech.business.airline.entity

import com.revotech.audit.JpaActivityInfo
import jakarta.persistence.*

@Entity
@Table(name = "t_airline")
class Airline(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    var code: String? = null,
    var name: String? = null,
    @Enumerated(EnumType.STRING)
    var type: AirlineType? = null,
    var sortOrder: Int? = null,
    @Enumerated(EnumType.STRING)
    var status: AirlineStatus? = null,
) : JpaActivityInfo()

enum class AirlineType {
    INTERNATIONAL,
    DOMESTIC,
    OTHER
}

enum class AirlineStatus {
    WORKING,
    NOT_WORKING
}