package com.revotech.business.work_content.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.revotech.business.work_content.dto.SearchInput
import com.revotech.business.work_content.dto.SearchWorkContentResult
import com.revotech.business.work_content.service.WorkContentService
import com.revotech.graphql.GraphqlUtil
import com.revotech.graphql.type.CustomPageable

@DgsComponent
class WorkContentQuery(private val workContentService: WorkContentService) {
    @DgsQuery
    fun searchWorkContent(searchInput: SearchInput, pageable: CustomPageable): SearchWorkContentResult {
        val adjustedPageable = pageable.copy(page = (pageable.page ?: 1).coerceAtLeast(1) - 1)
        val page = GraphqlUtil.toPageable(adjustedPageable)
        return workContentService.searchWorkContent(searchInput, page)
    }
}