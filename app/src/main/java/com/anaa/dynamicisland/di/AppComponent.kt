package com.anaa.dynamicisland.di

import android.app.Application
import com.anaa.dynamicisland.application.DynamicApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,ApplicationModule::class,ActivityBuildersModule::class,ViewModelModuleFactoryModule::class]
)
interface AppComponent : AndroidInjector<DynamicApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}