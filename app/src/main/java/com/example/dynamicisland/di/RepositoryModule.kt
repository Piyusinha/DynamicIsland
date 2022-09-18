package com.example.dynamicisland.di

import com.example.dynamicisland.FragmentRepository
import com.example.dynamicisland.utils.DynamixSharedPref
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun provideFragmentRepository(
        sharedPref: DynamixSharedPref
    ): FragmentRepository {
        return FragmentRepository(sharedPref)
    }
}
