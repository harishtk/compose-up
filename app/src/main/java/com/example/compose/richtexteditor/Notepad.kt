package com.example.compose.richtexteditor

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.MaterialColor

@Composable
fun NotepadRoute() {
    NotepadScreen()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotepadScreen() {
    val notepadFieldFocusRequester = remember { FocusRequester() }
    val notepadFieldState = rememberTextFieldState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Vertical,
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = notepadFieldState.text.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            NotepadEditor(
                Modifier.padding(8.dp),
                textFieldState = notepadFieldState,
                focusRequesterProvider = { notepadFieldFocusRequester }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NotepadEditor(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    focusRequesterProvider: () -> FocusRequester =  { FocusRequester() },
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    BasicTextField2(
        state = textFieldState,
        modifier = modifier
            .fillMaxWidth()
            .sizeIn(minWidth = 320.dp, minHeight = 150.dp)
            .background(
                color = MaterialColor.BlueGrey50,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(10.dp)
            .focusRequester(focusRequesterProvider())
            .clickable {
                focusRequesterProvider().requestFocus()
                softwareKeyboardController?.show()
            }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun NotepadScreenPreview() {
    Box(
        Modifier.fillMaxSize(),
    ) {
        NotepadScreen()
    }
}