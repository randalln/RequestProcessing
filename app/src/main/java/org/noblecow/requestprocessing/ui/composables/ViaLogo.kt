package org.noblecow.requestprocessing.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.noblecow.requestprocessing.R

@Composable
fun ViaLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(238.dp)
            .clip(CircleShape)
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(R.drawable.via_logo),
            contentDescription = "Via logo",
            modifier = Modifier
                .absoluteOffset(x = 48.dp, y = 97.dp)
                .size(width = 130.dp, height = 43.dp)
        )
    }
}

@Preview
@Composable
private fun ViaLogoPreview() {
    ViaLogo()
}
