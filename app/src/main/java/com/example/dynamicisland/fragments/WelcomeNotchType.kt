package com.example.dynamicisland.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dynamicisland.R
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class WelcomeNotchType : DaggerFragment() {

    companion object {
        fun newInstance() = WelcomeNotchType()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewmodel by lazy{
        ViewModelProvider(this,  viewModelFactory)[FragmentsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome_notch_type, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}