package com.revotech.business.work_content.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.revotech.business.work_content.service.WorkContentService

@DgsComponent
class WorkContentQuery(private val workContentService: WorkContentService) {
}