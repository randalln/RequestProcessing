package org.noblecow.requestprocessing.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import org.noblecow.requestprocessing.ui.theme.RequestProcessingTheme
import org.noblecow.requestprocessing.ui.theme.SnackbarErrorBackground
import org.noblecow.requestprocessing.ui.theme.SnackbarSuccessBackground

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppScreen()
        }
    }
}

@Serializable
object RequestList

@Serializable
class RequestDetail(@Suppress("unused") val newRequest: Boolean = true)

@Composable
@Suppress("LongMethod")
fun AppScreen(modifier: Modifier = Modifier) {
    RequestProcessingTheme {
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }
        var snackbarContainerColor by remember { mutableStateOf(Color.Unspecified) }

        Scaffold(
            modifier = modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = snackbarContainerColor
                    )
                }
            }
        ) { innerPadding ->
            NavHost(navController = navController, startDestination = RequestList) {
                composable<RequestList> { backStackEntry ->
                    val view = LocalView.current
                    if (!view.isInEditMode) {
                        SideEffect {
                            WindowCompat.getInsetsController(
                                (view.context as Activity).window,
                                view
                            ).isAppearanceLightStatusBars = true
                        }
                    }
                    val savedStateHandle = backStackEntry.savedStateHandle
                    val successStringRes by savedStateHandle
                        .getStateFlow<Int?>("successStringRes", null)
                        .collectAsStateWithLifecycle()
                    val errorStringRes by savedStateHandle
                        .getStateFlow<Int?>("errorStringRes", null)
                        .collectAsStateWithLifecycle()

                    var color = SnackbarErrorBackground
                    val snackbarStringRes = if (successStringRes != null) {
                        color = SnackbarSuccessBackground
                        successStringRes
                    } else {
                        errorStringRes
                    }

                    snackbarStringRes?.let { stringRes ->
                        val snackbarMessage = stringResource(stringRes)
                        LaunchedEffect(snackbarMessage) {
                            snackbarContainerColor = color
                            savedStateHandle.remove<Int?>("successStringRes")
                            savedStateHandle.remove<Int?>("errorStringRes")
                            snackbarHostState.showSnackbar(snackbarMessage, withDismissAction = true)
                        }
                    }

                    RequestListScreen(
                        onNavigateToNewRequest = { navController.navigate(route = RequestDetail()) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                composable<RequestDetail> {
                    val view = LocalView.current
                    if (!view.isInEditMode) {
                        SideEffect {
                            WindowCompat.getInsetsController(
                                (view.context as Activity).window,
                                view
                            ).isAppearanceLightStatusBars = false
                        }
                    }
                    RequestDetailScreen(
                        onNavigateToHome = { successStringRes, errorStringRes ->
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                "successStringRes",
                                successStringRes
                            )
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                "errorStringRes",
                                errorStringRes
                            )
                            navController.popBackStack()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppScreenPreview() {
    AppScreen()
}
