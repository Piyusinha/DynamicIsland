package com.anaa.dynamicisland

import com.anaa.dynamicisland.fragments.FragmentsViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class FragmentViewModelTest {

    @Mock
    lateinit var fragmentRepository: FragmentRepository

    private lateinit var viewModel2: FragmentsViewModel

    @Before
    fun setup(){
        viewModel2 = FragmentsViewModel(fragmentRepository)
    }

    @Test
    fun `Setup Notch`() {
        val notch = NOTCH.PUNCH_CENTER
        viewModel2.setupNotch(notch)
        whenever(fragmentRepository.getNotch()).thenReturn(notch.ordinal)
        Assert.assertEquals(notch.ordinal, viewModel2.getSavedNotch())

    }

}