package com.bbc.polly.wordle

import android.app.Application

class WordleApp : Application() {

    lateinit var serviceContainer: ServiceContainer

    override fun onCreate() {
        super.onCreate()
        serviceContainer = ServiceContainer()
    }
}