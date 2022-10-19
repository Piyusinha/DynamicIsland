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

    private lateinit var viewModel: FragmentsViewModel

    @Before
    fun setup(){
        viewModel = FragmentsViewModel(fragmentRepository)
    }

    @Test
    fun `Setup Notch`() {
        val notch = NOTCH.PUNCH_CENTER
        viewModel.setupNotch(notch)
        whenever(fragmentRepository.getNotch()).thenReturn(notch.ordinal)
        Assert.assertEquals(notch.ordinal, viewModel.getSavedNotch())

    }

}