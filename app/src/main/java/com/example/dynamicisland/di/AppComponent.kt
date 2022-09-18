package com.example.dynamicisland.di

import android.app.Application
import com.example.dynamicisland.application.DynamicApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [AndroidSupportInjectionModule::class,ActivityBuildersModule::class,ViewModelModuleFactoryModule::class]
)
interface AppComponent : AndroidInjector<DynamicApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}