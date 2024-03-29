package com.anaa.dynamicisland.di

import com.anaa.dynamicisland.MainActivity
import com.anaa.dynamicisland.ui.activity.IslandActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
        modules = [ViewModelModule::class,RepositoryModule::class,FragmentBuildersModule::class]
    )

    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(
        modules = [ViewModelModule::class,RepositoryModule::class]
    )

    abstract fun contributeIslandActivity(): IslandActivity


}