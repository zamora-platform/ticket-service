package com.revotech.business.work_content.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.revotech.business.work_content.dto.SaveWorkContentReq
import com.revotech.business.work_content.service.WorkContentService

@DgsComponent
class WorkContentMutation(private val workContentService: WorkContentService) {
    @DgsMutation
    fun saveWorkContent(saveWorkContentReq: SaveWorkContentReq): Boolean {
        return workContentService.saveWorkContent(saveWorkContentReq)
    }
}