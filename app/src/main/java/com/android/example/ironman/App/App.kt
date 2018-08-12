package com.android.example.ironman.App

import android.app.Application
import com.android.example.ironman.db.MyObjectBox
import io.objectbox.BoxStore


class App : Application() {
    var boxStore: BoxStore? = null


    override fun onCreate() {
        super.onCreate()
        app = this
        boxStore = MyObjectBox.builder().androidContext(this@App).build()
    }

    companion object {

        var app: App? = null

    }
}
