package org.noblecow.requestprocessing

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.noblecow.requestprocessing.data.ApprovalException
import org.noblecow.requestprocessing.data.RequestRepository
import org.noblecow.requestprocessing.domain.Request
import org.noblecow.requestprocessing.ui.RequestDetailUiState
import org.noblecow.requestprocessing.ui.RequestDetailViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class RequestDetailViewModelTest {
    private lateinit var viewModel: RequestDetailViewModel
    private val request = Request(isNew = true, heading = "Heading", body = "Body")
    private val repository: RequestRepository = mockk()
    private val savedStateHandle = SavedStateHandle(mapOf("newRequest" to true))
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { repository.getRequest(any()) } returns request
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `approval happy path`() = runTest {
        coEvery { repository.approveRequest(any()) } coAnswers {
            delay(1L)
            true
        }
        viewModel = RequestDetailViewModel(repository, savedStateHandle)

        viewModel.uiState.test {
            assertEquals(RequestDetailUiState(request = request), awaitItem())
            viewModel.approve()
            assertEquals(RequestDetailUiState(isLoading = true, request = request), awaitItem())
            assertEquals(
                RequestDetailUiState(
                    isLoading = false,
                    request = request,
                    successStringRes = R.string.request_approved
                ),
                awaitItem()
            )
        }
    }

    @Test
    fun `approval failure`() = runTest {
        coEvery { repository.approveRequest(any()) } coAnswers {
            delay(1L)
            throw ApprovalException()
        }
        viewModel = RequestDetailViewModel(repository, savedStateHandle)

        viewModel.uiState.test {
            assertEquals(RequestDetailUiState(request = request), awaitItem())
            viewModel.approve()
            assertEquals(RequestDetailUiState(isLoading = true, request = request), awaitItem())
            assertEquals(
                RequestDetailUiState(isLoading = false, request = request, errorStringRes = R.string.approval_error),
                awaitItem()
            )
        }
    }

    @Test
    fun rejection() = runTest {
        coEvery { repository.rejectRequest(any()) } coAnswers {
            delay(1L)
            true
        }
        viewModel = RequestDetailViewModel(repository, savedStateHandle)

        viewModel.uiState.test {
            assertEquals(RequestDetailUiState(request = request), awaitItem())
            viewModel.reject()
            assertEquals(RequestDetailUiState(isLoading = true, request = request), awaitItem())
            assertEquals(
                RequestDetailUiState(isLoading = false, request = request, errorStringRes = R.string.request_rejected),
                awaitItem()
            )
        }
    }
}
