package com.example.compose

import timber.log.Timber

object CLogger {
    private const val TAG = "ComposePerformance"

    fun d(message: String, vararg args: Any, filter: LogFilter? = null) {
        Timber.tag("$TAG-${filter ?: ""}").d(message, args)
    }
}

enum class LogFilter {
    Reallocation,
    Recomposition,
}