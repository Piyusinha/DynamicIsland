package com.example.dynamicisland.di

import com.example.dynamicisland.fragments.SetupNotchPositionFragment
import com.example.dynamicisland.fragments.WelcomeNotchType
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMyFragment(): WelcomeNotchType

    @ContributesAndroidInjector
    abstract fun contributeNotchPositionFragment(): SetupNotchPositionFragment
}