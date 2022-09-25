package com.anaa.dynamicisland.application

import com.anaa.dynamicisland.di.AppComponent
import com.anaa.dynamicisland.di.DaggerAppComponent
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.core.FlipperClient
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
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
        SoLoader.init(this, false)
        if ( FlipperUtils.shouldEnableFlipper(this)) {
            val client: FlipperClient = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.addPlugin(SharedPreferencesFlipperPlugin(this,"DynamicSharedPref"))
            client.start()
        }
    }

    private fun initDagger() {
        applicationComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()
        applicationComponent.inject(this)
    }
    fun getMainAppComponent(): AppComponent {
        return applicationComponent
    }

}