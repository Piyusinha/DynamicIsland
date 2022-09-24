package com.anaa.dynamicisland.accessibilty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ComposeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ComposeViewModel::class.java)) {
            ComposeViewModel() as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}