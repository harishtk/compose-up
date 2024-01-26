package com.example.compose

import android.app.Application
import timber.log.Timber

class ApplicationContext : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}