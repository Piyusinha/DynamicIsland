package com.example.dynamicisland.di

import androidx.lifecycle.ViewModelProvider
import com.example.dynamicisland.viewmodelFactory.ViewModelFactory
import dagger.Binds
import dagger.Module


@Module
abstract class ViewModelModuleFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}