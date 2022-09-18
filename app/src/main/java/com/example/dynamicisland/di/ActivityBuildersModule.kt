package com.example.dynamicisland.di

import com.example.dynamicisland.MainActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
        modules = [ViewModelModule::class,RepositoryModule::class,FragmentBuildersModule::class]
    )

    abstract fun contributeMainActivity(): MainActivity


}