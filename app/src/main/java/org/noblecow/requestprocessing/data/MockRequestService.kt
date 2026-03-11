package org.noblecow.requestprocessing.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.delay
import org.noblecow.requestprocessing.R
import org.noblecow.requestprocessing.domain.Request

private const val DELAY_MS = 2000L

class MockRequestService @Inject constructor(@ApplicationContext private val context: Context) : RequestService {
    override suspend fun getNewRequest(): Request = Request(
        isNew = true,
        context.getString(R.string.heading),
        body = context.getString(R.string.body)
    )

    override suspend fun approveRequest(request: Request): Boolean {
        delay(DELAY_MS)
        if ((0..1).random() == 0) {
            return true
        } else {
            throw ApprovalException()
        }
    }

    override suspend fun rejectRequest(request: Request): Boolean = true
}
