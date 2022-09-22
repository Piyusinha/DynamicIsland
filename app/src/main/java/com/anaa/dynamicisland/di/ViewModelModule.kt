package com.anaa.dynamicisland.di

import androidx.lifecycle.ViewModel
import com.anaa.dynamicisland.MainActivityViewModel
import com.anaa.dynamicisland.di.dikey.ViewModelKey
import com.anaa.dynamicisland.fragments.FragmentsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FragmentsViewModel::class)
    internal abstract fun bindMyViewModel(viewModel: FragmentsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    internal abstract fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel
}