package com.example.compose.viewflipper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * A [FlippableController] which lets you control flipping programmatically.
 *
 * @author Wajahat Karim (https://github.com/wajahatkarim3/Flippable)
 * Modified by: Hariskumar
 */
class FlippableController {

    var currentState: FlippableState by mutableStateOf(FlippableState.FRONT)
        private set
    var flipEnabled: Boolean = true
        private set

    fun flipToFront() {
        flip(FlippableState.FRONT)
    }

    fun flipToBack() {
        flip(FlippableState.BACK)
    }

    fun flip(targetFlippableState: FlippableState) {
        if (!flipEnabled) {
            return
        }

        currentState = targetFlippableState
    }


    fun flip() {
        if (currentState == FlippableState.FRONT)
            flipToBack()
        else
            flipToFront()
    }

    internal fun setConfig(
        flipEnabled: Boolean = true,
    ) {
        this.flipEnabled = true
    }
}

enum class FlippableState {
    FRONT, BACK
}

@Composable
fun rememberFlippableController(): FlippableController {
    return remember { FlippableController() }
}