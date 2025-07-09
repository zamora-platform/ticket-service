package com.revotech.business.country.entity

import com.revotech.audit.JpaActivityInfo
import jakarta.persistence.*

@Entity
@Table(name = "t_city")
class City(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    var name: String? = null,
    var countryId: String? = null,
    @Enumerated(EnumType.STRING)
    var status: CityStatus? = null
) : JpaActivityInfo()

enum class CityStatus {
    ACTIVE,
    INACTIVE
}