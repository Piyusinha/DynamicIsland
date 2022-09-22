package com.anaa.dynamicisland.di

import android.content.Context
import com.anaa.dynamicisland.FragmentRepository
import com.anaa.dynamicisland.di.custom_annotation.ApplicationContext
import com.anaa.dynamicisland.utils.DynamixSharedPref
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
