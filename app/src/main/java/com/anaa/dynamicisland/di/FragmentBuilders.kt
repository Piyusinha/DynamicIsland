package com.anaa.dynamicisland.di

import com.anaa.dynamicisland.fragments.SetupNotchPositionFragment
import com.anaa.dynamicisland.fragments.WelcomeNotchType
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMyFragment(): WelcomeNotchType

    @ContributesAndroidInjector
    abstract fun contributeNotchPositionFragment(): SetupNotchPositionFragment
}