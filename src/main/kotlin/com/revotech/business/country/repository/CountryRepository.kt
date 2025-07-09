package com.revotech.business.country.repository

import com.revotech.business.country.dto.CountryListProjection
import com.revotech.business.country.entity.Country
import com.revotech.business.ticket_agent.dto.TicketAgentListProjection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CountryRepository : JpaRepository<Country, String> {
    @Query("SELECT c " +
            "FROM Country c " +
            "WHERE c.id = :id " +
            "AND c.status = 'ACTIVE' ")
    fun findCountryById(
        @Param("id") id: String
    ): Country?

    fun existsByCode(code: String): Boolean
    fun existsByName(code: String): Boolean

    @Query(
        """
            SELECT c.id           AS id,
                   c.code         AS code,
                   c.name         AS name,
                   c.status       AS status,
                   c.sort_order   AS sortOrder,
                   c.created_by   AS createdBy,
                   c.created_time AS createdTime
            FROM t_country c
            WHERE (:textSearch IS NULL OR
                   LOWER(c.code) LIKE LOWER(CONCAT('%', :textSearch, '%')) OR
                   LOWER(c.name) LIKE LOWER(CONCAT('%', :textSearch, '%'))
            AND c.status = 'ACTIVE')
            ORDER BY c.sort_order ASC, c.created_time DESC
        """, nativeQuery = true)
    fun searchCountry(
        @Param("textSearch") textSearch: String?,
        pageable: Pageable
    ): Page<CountryListProjection>

    @Query(
        """
            SELECT COUNT(id) + 1 
            FROM t_country 
            WHERE status = 'ACTIVE'
        """, nativeQuery = true)
    fun getNextCountrySortOrder(): Int
}