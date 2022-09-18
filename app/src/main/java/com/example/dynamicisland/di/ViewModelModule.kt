package com.example.dynamicisland.di

import androidx.lifecycle.ViewModel
import com.example.dynamicisland.MainActivityViewModel
import com.example.dynamicisland.di.dikey.ViewModelKey
import com.example.dynamicisland.fragments.FragmentsViewModel
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