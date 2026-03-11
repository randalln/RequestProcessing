package org.noblecow.requestprocessing.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.revenuecat.purchases.slidetounlock.DefaultSlideToUnlockColors
import com.revenuecat.purchases.slidetounlock.HintTexts
import com.revenuecat.purchases.slidetounlock.SlideToUnlock
import org.noblecow.requestprocessing.R
import org.noblecow.requestprocessing.ui.theme.BackgroundBlue
import org.noblecow.requestprocessing.ui.theme.BrandBlue
import org.noblecow.requestprocessing.ui.theme.DetailBackground
import org.noblecow.requestprocessing.ui.theme.DetailCardBackground
import org.noblecow.requestprocessing.ui.theme.SlideCompletedColor
import org.noblecow.requestprocessing.ui.theme.ThumbChevronColor

private val verticalSpacing = 60.dp
private val horizontalSpacing = 12.dp
private val cardSpacing = 20.dp

@Composable
@Suppress("LongMethod")
fun RequestDetailScreen(
    onNavigateToHome: (Int?, Int?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RequestDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnNavigateToHome by rememberUpdatedState(onNavigateToHome)
    var isSlided by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.successStringRes, uiState.errorStringRes) {
        if (uiState.successStringRes != null || uiState.errorStringRes != null) {
            currentOnNavigateToHome(uiState.successStringRes, uiState.errorStringRes)
        }
    }

    uiState.request?.let { request ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DetailBackground)
                .then(modifier)
                .padding(horizontal = horizontalSpacing)
        ) {
            Spacer(Modifier.height(verticalSpacing))
            Text(
                text = stringResource(R.string.new_request),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 32.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(verticalSpacing))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(DetailCardBackground, RoundedCornerShape(8.dp))
                    .padding(cardSpacing)
            ) {
                Text(
                    text = request.heading,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = request.body,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.isLoading) {
                    Spacer(Modifier.weight(1f))
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier
                            .width(32.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(vertical = verticalSpacing)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    enabled = !uiState.isLoading,
                    onClick = { viewModel.reject() },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White,
                        disabledContentColor = Color.White.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = if (!uiState.isLoading) 1f else 0.5f)
                    )
                ) {
                    Text(stringResource(R.string.reject))
                }
                SlideToUnlock(
                    isSlided = isSlided,
                    modifier = Modifier.weight(1f),
                    colors = DefaultSlideToUnlockColors(
                        startTrackColor = BrandBlue,
                        endTrackColor = SlideCompletedColor,
                        thumbColor = BackgroundBlue,
                        startHintColor = Color.White,
                        endHintColor = BrandBlue
                    ),
                    hintTexts = HintTexts(
                        defaultText = stringResource(R.string.slide_to_approve),
                        slidedText = stringResource(R.string.approved)
                    ),
                    onSlideCompleted = {
                        isSlided = true
                        viewModel.approve()
                    },
                    fractionalThreshold = 0.7f, // Make the slider a little more sensitive
                    thumb = { slided, fraction, colors, size, orientation ->
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                // .offset { IntOffset(currentOffsetPx.roundToInt(), 0) }
                                .size(size)
                                .clip(RoundedCornerShape(8.dp))
                                .background(BackgroundBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            if (slided) {
                                Box(
                                    modifier = Modifier
                                        .size(size * 0.6f)
                                        .clip(CircleShape)
                                        .background(ThumbChevronColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            } else {
                                Text(
                                    text = "»",
                                    color = ThumbChevronColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 32.sp
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}
