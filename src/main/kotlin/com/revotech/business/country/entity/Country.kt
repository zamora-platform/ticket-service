package com.revotech.business.country.entity

import com.revotech.audit.JpaActivityInfo
import jakarta.persistence.*

@Entity
@Table(name = "t_country")
class Country (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    var code: String? = null,
    var name: String? = null,
    var isDefault: Boolean? = null,
    var sortOrder: Int? = null,
    @Enumerated(EnumType.STRING)
    var status: CountryStatus? = null
) : JpaActivityInfo()

enum class CountryStatus {
    ACTIVE,
    INACTIVE
}
