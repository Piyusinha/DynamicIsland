package com.anaa.dynamicisland.application

import com.anaa.dynamicisland.di.AppComponent
import com.anaa.dynamicisland.di.DaggerAppComponent
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