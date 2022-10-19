package com.anaa.dynamicisland.application


import com.anaa.dynamicisland.BuildConfig
import com.anaa.dynamicisland.accessibilty.ComposeAccessibiltyService
import com.anaa.dynamicisland.di.AppComponent
import com.anaa.dynamicisland.di.DaggerAppComponent
import com.anaa.dynamicisland.service.DynamicNotificationListenerService
//import com.facebook.flipper.android.AndroidFlipperClient
//import com.facebook.flipper.android.utils.FlipperUtils
//import com.facebook.flipper.core.FlipperClient
//import com.facebook.flipper.plugins.inspector.DescriptorMapping
//import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
//import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
//import com.facebook.soloader.SoLoader
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class DynamicApplication: DaggerApplication() {

    lateinit var applicationComponent: AppComponent

    var composeAccessibiltyService: ComposeAccessibiltyService ?= null

    var notificationService: DynamicNotificationListenerService ?= null

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
      return applicationComponent
    }

    override fun onCreate() {
        initDagger()
        super.onCreate()
//        SoLoader.init(this, false)
//        if ( BuildConfig.DEBUGABLE && FlipperUtils.shouldEnableFlipper(this)) {
//            val client: FlipperClient = AndroidFlipperClient.getInstance(this)
//            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
//            client.addPlugin(SharedPreferencesFlipperPlugin(this,"DynamicSharedPref"))
//            client.start()
//        }
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

    fun setAccessibilityService(composeAccessibiltyService: ComposeAccessibiltyService) {
        this.composeAccessibiltyService = composeAccessibiltyService
    }

    fun setNotificationServiceValue(notificationService: DynamicNotificationListenerService) {
        this.notificationService = notificationService
    }

}