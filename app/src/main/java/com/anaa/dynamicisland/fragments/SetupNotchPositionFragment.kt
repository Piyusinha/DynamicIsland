package com.anaa.dynamicisland.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CompoundButton
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.anaa.dynamicisland.AccessbilityStaticClass
import com.anaa.dynamicisland.MainActivityViewModel
import com.anaa.dynamicisland.R
import com.anaa.dynamicisland.databinding.DefaultNotchViewBinding
import com.anaa.dynamicisland.databinding.FragmentSetupNotchPositionBinding
import com.anaa.dynamicisland.ui.activity.IslandActivity
import com.anaa.dynamicisland.ui.view.DynamicLayoutParams
import com.anaa.dynamicisland.utils.dpToPx
import com.anaa.dynamicisland.utils.pxTodp
import com.anaa.dynamicisland.view.WindowsViewInflater
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
    private var size = 0f
    private var radius = 0f
    private var defaultLayoutParams : WindowManager.LayoutParams? = null

    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity(),viewModelFactory)[MainActivityViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetupNotchPositionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defaultLayoutParams = DynamicLayoutParams.getActivityLayoutParams(x,y, WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,viewModel.getGravity())
        initViewListener()
        initArrowClickListener()
        initClickListener()
    }

    private fun initClickListener() {
        binding.confirmPosition.setOnClickListener {
            viewModel.setXandY(x,y)
            viewModel.setDimension(size)
            viewModel.setRadius(radius)
            binding.switchView.isChecked = false
            viewModel.setupDone(true)
            AccessbilityStaticClass.service?.removeView()
            startActivity(Intent(activity,IslandActivity::class.java))
        }
    }

    private fun initArrowClickListener() {
        binding.down.setOnClickListener {
            y++
            AccessbilityStaticClass.service?.updateView(x,y)
        }
        binding.up.setOnClickListener {
            y--
            AccessbilityStaticClass.service?.updateView(x,y)
        }
        binding.left.setOnClickListener {
            if(viewModel.getSavedNotch() == 1) {
                x++
            }else {
                x--
            }
            AccessbilityStaticClass.service?.updateView(x,y)
        }
        binding.right.setOnClickListener {
            if(viewModel.getSavedNotch() == 1) {
                x--
            }else {
                x++
            }
            AccessbilityStaticClass.service?.updateView(x,y)
        }
    }

    private fun initViewListener() {
        binding.switchView.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(p1) {
                    AccessbilityStaticClass.service?.enableSetupView()
                    binding.arrowView.isVisible = true
                    initViewValues()
                }else {
                    windowsView?.removeView()
                    binding.arrowView.isVisible = false
                }
            }

        })
    }


    lateinit var notchViewBinding : DefaultNotchViewBinding

    private fun setupNotchView() {
        val li = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        notchViewBinding = DataBindingUtil.inflate<DefaultNotchViewBinding>(li,R.layout.default_notch_view,null,false)
        windowsView =  WindowsViewInflater(activity,notchViewBinding.root)
        windowsView?.addView(defaultLayoutParams)
    }

    companion object {

        @JvmStatic
        fun newInstance() = SetupNotchPositionFragment()
    }

    fun onBackPressed() {
        AccessbilityStaticClass.service?.removeView()
        binding.arrowView.isVisible = false
    }

    private fun initViewValues() {
        size = 20f
        binding.sliderSeekbar.position = size /100
        radius = 8f
        binding.sliderSeekbarRadius.position = radius/100

        initPositionListner()
    }

    private fun initPositionListner() {
        binding.sliderSeekbar.positionListener = { p ->
            size = p*100
            AccessbilityStaticClass.service?.changeSize(size.toInt())
        }
        binding.sliderSeekbarRadius.positionListener = { p ->
            radius = p*100
            AccessbilityStaticClass.service?.updateRadius(radius.toInt())
        }
    }

}