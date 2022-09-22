package com.anaa.dynamicisland.fragments

import android.content.Context
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
import com.anaa.dynamicisland.MainActivityViewModel
import com.anaa.dynamicisland.R
import com.anaa.dynamicisland.databinding.DefaultNotchViewBinding
import com.anaa.dynamicisland.databinding.FragmentSetupNotchPositionBinding
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
            viewModel.setDimension(binding.sliderSeekbar.position * 100)
            viewModel.setRadius(binding.sliderSeekbarRadius.position * 100)
            binding.switchView.isChecked = false
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
            if(viewModel.getSavedNotch() == 1) {
                x++
            }else {
                x--
            }
            defaultLayoutParams?.x = x
            windowsView?.updateView(defaultLayoutParams)
        }
        binding.right.setOnClickListener {
            if(viewModel.getSavedNotch() == 1) {
                x--
            }else {
                x++
            }
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
        windowsView?.removeView()
        binding.arrowView.isVisible = false
    }

    private fun initViewValues() {
        size = (context?.pxTodp(notchViewBinding.root.minimumWidth.toFloat())?.toFloat() ?: 0f)
        binding.sliderSeekbar.position = size /100
        radius = (context?.pxTodp((notchViewBinding.root as? CardView)?.radius ?: 0f )?.toFloat() ?: 0f)
        binding.sliderSeekbarRadius.position = radius/100

        iniPositionListner()
    }

    private fun iniPositionListner() {
        binding.sliderSeekbar.positionListener = { p ->
            size = requireContext().dpToPx(p*100).toFloat()
            notchViewBinding.root.minimumWidth = size.toInt()
            notchViewBinding.root.minimumHeight = size.toInt()
        }
        binding.sliderSeekbarRadius.positionListener = { p ->
            radius = requireContext().dpToPx(p*100).toFloat()
            (notchViewBinding.root as? CardView)?.radius = radius
        }
    }

}