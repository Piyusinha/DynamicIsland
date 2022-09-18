package com.example.dynamicisland.di

import android.content.Context
import com.example.dynamicisland.FragmentRepository
import com.example.dynamicisland.di.custom_annotation.ApplicationContext
import com.example.dynamicisland.utils.DynamixSharedPref
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun provideFragmentRepository(
        sharedPref: DynamixSharedPref,
        @ApplicationContext context: Context
    ): FragmentRepository {
        return FragmentRepository(sharedPref,context)
    }
}
