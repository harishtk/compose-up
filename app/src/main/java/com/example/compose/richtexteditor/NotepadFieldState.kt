package com.example.compose.richtexteditor

import com.example.compose.designcomponents.TextFieldState
import com.example.compose.designcomponents.textFieldStateSaver

class NotepadFieldState(
    initialText: String? = null,
) : TextFieldState() {
    init {
        this.text = initialText ?: ""
    }
}

val NotepadFieldStateSaver = textFieldStateSaver(NotepadFieldState())