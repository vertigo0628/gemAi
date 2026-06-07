package com.vertigo.gemai

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BakingScreen(
    bakingViewModel: BakingViewModel = viewModel()
) {
    var customBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var prompt by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("") }
    val uiState by bakingViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val takePhotoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            customBitmap = bitmap
        }
    }

    val pickPhotoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                customBitmap = BitmapFactory.decodeStream(inputStream)
            }
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.baking_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (customBitmap != null) {
                    Image(
                        bitmap = customBitmap!!.asImageBitmap(),
                        contentDescription = "Selected photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Text(
                        text = "Take or select a photo to begin",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { takePhotoLauncher.launch() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.take_photo))
                }
                Button(
                    onClick = { pickPhotoLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.select_photo))
                }
            }

            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                TextField(
                    value = prompt,
                    label = { Text(stringResource(R.string.label_prompt)) },
                    placeholder = { Text(stringResource(R.string.prompt_placeholder)) },
                    onValueChange = { prompt = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                        .align(Alignment.CenterVertically)
                )

                Button(
                    onClick = {
                        customBitmap?.let {
                            bakingViewModel.sendPrompt(it, prompt)
                        }
                    },
                    enabled = prompt.isNotEmpty() && customBitmap != null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    Text(text = stringResource(R.string.action_go))
                }
            }

            if (uiState is UiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                var textColor = MaterialTheme.colorScheme.onSurface
                if (uiState is UiState.Error) {
                    textColor = MaterialTheme.colorScheme.error
                    result = (uiState as UiState.Error).errorMessage
                } else if (uiState is UiState.Success) {
                    textColor = MaterialTheme.colorScheme.onSurface
                    result = (uiState as UiState.Success).outputText
                }
                val scrollState = rememberScrollState()
                Text(
                    text = result,
                    textAlign = TextAlign.Start,
                    color = textColor,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BakingScreenPreview() {
    BakingScreen()
}
