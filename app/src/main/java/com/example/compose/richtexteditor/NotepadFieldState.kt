package com.example.compose.richtexteditor

import com.example.compose.designsystem.TextFieldState
import com.example.compose.designsystem.textFieldStateSaver

class NotepadFieldState(
    initialText: String? = null,
) : TextFieldState() {
    init {
        this.text = initialText ?: ""
    }
}

val NotepadFieldStateSaver = textFieldStateSaver(NotepadFieldState())