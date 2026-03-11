package org.noblecow.requestprocessing.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.noblecow.requestprocessing.R
import org.noblecow.requestprocessing.ui.composables.ViaLogo
import org.noblecow.requestprocessing.ui.theme.BackgroundBlue
import org.noblecow.requestprocessing.ui.theme.BrandBlue

private val horizontalSpacing = 30.dp
private val verticalSpacing = 60.dp

@Composable
fun RequestListScreen(
    onNavigateToNewRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlue)
            .then(modifier)
            .padding(horizontal = horizontalSpacing)
    ) {
        Spacer(Modifier.height(verticalSpacing))
        Text(
            text = stringResource(R.string.home),
            color = BrandBlue,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            fontSize = 32.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.size(verticalSpacing))
        ViaLogo()
        Spacer(Modifier.size(verticalSpacing))
        Button(
            onClick = {
                onNavigateToNewRequest()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.create_new_request))
        }
    }
}

@Preview
@Composable
private fun RequestListScreenPreview() {
    RequestListScreen(onNavigateToNewRequest = {})
}
