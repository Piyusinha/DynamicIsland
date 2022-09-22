package com.anaa.dynamicisland.di

import android.app.Application
import android.content.Context
import com.anaa.dynamicisland.di.custom_annotation.ApplicationContext
import com.anaa.dynamicisland.utils.DynamixSharedPref
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @ApplicationContext
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): DynamixSharedPref {
        return DynamixSharedPref(context)
    }
}