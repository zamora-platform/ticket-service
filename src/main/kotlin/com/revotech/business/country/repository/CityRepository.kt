package com.revotech.business.country.repository

import com.revotech.business.country.dto.CityListProjection
import com.revotech.business.country.entity.City
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CityRepository : JpaRepository<City, String> {
    fun deleteAllByCountryId(countryId: String)

    @Query(
        """
            SELECT c.id         AS cityId,
                   c.name       AS cityName,
                   c.country_id AS countryId
            FROM t_city c
            WHERE c.country_id IN :ids
            AND c.status = 'ACTIVE'
        """, nativeQuery = true
    )
    fun findCitiesByCountryIds(
        @Param("ids") ids: List<String>
    ): List<CityListProjection>

    @Modifying
    @Query(
        """
            UPDATE t_city c
            SET status = 'INACTIVE'
            WHERE c.country_id IN :countryId
        """, nativeQuery = true
    )
    fun softDeleteCityByCountryId(
        @Param("countryId") countryId: List<String>,
    ): Int

    fun findCityById(id: String): City?
}