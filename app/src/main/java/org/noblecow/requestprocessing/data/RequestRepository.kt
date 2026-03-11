package org.noblecow.requestprocessing.data

import javax.inject.Inject
import org.noblecow.requestprocessing.domain.Request

class ApprovalException : Exception()

class RequestRepository @Inject constructor(val requestService: RequestService) {
    suspend fun getRequest(isNew: Boolean): Request = if (isNew) {
        requestService.getNewRequest()
    } else {
        // This would actually fetch from a local/remote data source (given an ID)
        requestService.getNewRequest()
    }
    suspend fun approveRequest(request: Request) = requestService.approveRequest(request)
    suspend fun rejectRequest(request: Request) = requestService.rejectRequest(request)
}
