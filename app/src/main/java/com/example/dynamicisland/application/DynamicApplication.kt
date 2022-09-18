package com.example.dynamicisland.application

import com.example.dynamicisland.di.AppComponent
import com.example.dynamicisland.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class DynamicApplication: DaggerApplication() {

    lateinit var applicationComponent: AppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
      return applicationComponent
    }

    override fun onCreate() {
        initDagger()
        super.onCreate()

    }

    private fun initDagger() {
        applicationComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()
        applicationComponent.inject(this)
    }
}