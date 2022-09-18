package com.example.dynamicisland.fragments

import android.accessibilityservice.AccessibilityService
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CompoundButton
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.dynamicisland.R
import com.example.dynamicisland.application.DynamicApplication
import com.example.dynamicisland.databinding.DefaultNotchViewBinding
import com.example.dynamicisland.databinding.FragmentSetupNotchPositionBinding
import com.example.dynamicisland.ui.view.DynamicLayoutParams
import com.example.dynamicisland.utils.DEFAULT_X
import com.example.dynamicisland.view.WindowsViewInflater
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class SetupNotchPositionFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: FragmentSetupNotchPositionBinding

    private var windowsView : WindowsViewInflater? = null

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[FragmentsViewModel::class.java]
    }

    private var x = 0
    private var y = 0
    private var defaultLayoutParams : WindowManager.LayoutParams? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetupNotchPositionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defaultLayoutParams = DynamicLayoutParams.getActivityLayoutParams(x,y, WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,getGravity())
        initViewListener()
        initArrowClickListener()
        initClickListener()
    }

    private fun initClickListener() {
        binding.confirmPosition.setOnClickListener {
            viewModel.setXandY(x,y)
        }
    }

    private fun initArrowClickListener() {
        binding.down.setOnClickListener {
            y++
            defaultLayoutParams?.y = y
            windowsView?.updateView(defaultLayoutParams)
        }
        binding.up.setOnClickListener {
            y--
            defaultLayoutParams?.y = y
            windowsView?.updateView(defaultLayoutParams)
        }
        binding.left.setOnClickListener {
            x--
            defaultLayoutParams?.x = x
            windowsView?.updateView(defaultLayoutParams)
        }
        binding.right.setOnClickListener {
            x++
            defaultLayoutParams?.x = x
            windowsView?.updateView(defaultLayoutParams)
        }
    }

    private fun initViewListener() {
        binding.switchView.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(p1) {
                    setupNotchView()
                    binding.arrowView.isVisible = true
                }else {
                    windowsView?.removeView()
                    binding.arrowView.isVisible = false
                }
            }

        })
    }

    private fun setupNotchView() {
        val li = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootBinding = DataBindingUtil.inflate<DefaultNotchViewBinding>(li,R.layout.default_notch_view,null,false)
        windowsView =  WindowsViewInflater(activity,rootBinding.root)
        windowsView?.addView(defaultLayoutParams)
    }

    private fun getGravity(): Int {
        when(viewModel.getSavedNotch()) {
            0 -> return Gravity.TOP or Gravity.LEFT
            1 -> return Gravity.TOP or Gravity.RIGHT
            2,3 -> return Gravity.TOP or Gravity.CENTER
        }
        return -1
    }

    companion object {

        @JvmStatic
        fun newInstance() = SetupNotchPositionFragment()
    }

    fun onBackPressed() {
        windowsView?.removeView()
        binding.arrowView.isVisible = true
    }
}