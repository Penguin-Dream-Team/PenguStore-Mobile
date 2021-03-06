package store.pengu.mobile.views.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import store.pengu.mobile.R

@Composable
fun LoginScreenLogo() {
    Column(
        modifier = Modifier
            .padding(top = 75.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .height(128.dp)
                .width(128.dp)
                .clip(CircleShape)
        ) {
            Image(
                painterResource(R.drawable.pengulogo),
                contentDescription = "PenguStore",
                contentScale = ContentScale.Crop,
            )
        }

        val logoFont = MaterialTheme.typography.h6
        Text(
            text = stringResource(R.string.app_name), fontSize = logoFont.fontSize,
            fontWeight = logoFont.fontWeight,
            modifier = Modifier
                .padding(top = 10.dp),
            textAlign = TextAlign.Center
        )
    }
}
