package com.revotech.business.country.repository

import com.revotech.business.country.dto.CountryProjection
import com.revotech.business.country.entity.Country
import com.revotech.business.country.entity.CountryStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CountryRepository : JpaRepository<Country, String> {
    @Query(
        "SELECT c " +
                "FROM Country c " +
                "WHERE c.id = :id " +
                "AND c.status = 'ACTIVE' "
    )
    fun findCountryById(
        @Param("id") id: String
    ): Country?

    fun existsByCodeAndStatus(code: String, status: CountryStatus): Boolean
    fun existsByNameAndStatus(code: String, status: CountryStatus): Boolean

    @Query(
        """
            SELECT c.id           AS id,
                   c.code         AS code,
                   c.name         AS name,
                   c.status       AS status,
                   c.sort_order   AS sortOrder,
                   c.is_default   AS isDefault,
                   c.created_by   AS createdBy,
                   c.created_time AS createdTime
            FROM t_country c
            WHERE (:textSearch IS NULL OR
                   LOWER(c.code) LIKE LOWER(CONCAT('%', :textSearch, '%')) OR
                   LOWER(c.name) LIKE LOWER(CONCAT('%', :textSearch, '%')))
            AND c.status = 'ACTIVE'
            ORDER BY c.is_default DESC, c.sort_order ASC, c.created_time DESC
        """, nativeQuery = true
    )
    fun searchCountry(
        @Param("textSearch") textSearch: String?,
        pageable: Pageable
    ): Page<CountryProjection>

    @Query(
        """
            SELECT COUNT(id) + 1 
            FROM t_country 
            WHERE status = 'ACTIVE'
        """, nativeQuery = true
    )
    fun getNextCountrySortOrder(): Int

    @Query(
        """
            SELECT c.id           AS id,
                   c.code         AS code,
                   c.name         AS name,
                   c.status       AS status,
                   c.sort_order   AS sortOrder,
                   c.is_default   AS isDefault,
                   c.created_by   AS createdBy,
                   c.created_time AS createdTime
            FROM t_country c
            WHERE c.id = :id
            AND c.status = 'ACTIVE'
        """, nativeQuery = true
    )
    fun getDetailCountryById(
        @Param("id") id: String,
    ): CountryProjection?

    @Modifying
    @Query("UPDATE Country c SET c.isDefault = false WHERE c.isDefault = true")
    fun unsetCountryIsDefaultTrueToFalse(): Int

    @Query(
        """
            SELECT c.id           AS id,
                   c.code         AS code,
                   c.name         AS name,
                   c.status       AS status,
                   c.sort_order   AS sortOrder,
                   c.is_default   AS isDefault,
                   c.created_by   AS createdBy,
                   c.created_time AS createdTime
            FROM t_country c
            WHERE c.status = 'ACTIVE'
            ORDER BY c.is_default DESC, c.sort_order ASC, c.created_time DESC
        """, nativeQuery = true
    )
    fun getAllCountryAndItCity(): List<CountryProjection>
}