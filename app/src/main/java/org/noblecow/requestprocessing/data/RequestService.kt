package org.noblecow.requestprocessing.data

import org.noblecow.requestprocessing.domain.Request

interface RequestService {
    suspend fun getNewRequest(): Request
    suspend fun approveRequest(request: Request): Boolean
    suspend fun rejectRequest(request: Request): Boolean // Can a rejection fail?
}
