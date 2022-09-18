package com.example.dynamicisland.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dynamicisland.MainActivityViewModel
import com.example.dynamicisland.R
import com.example.dynamicisland.databinding.FragmentWelcomeNotchTypeBinding
import com.example.dynamicisland.fragments.interfaces.NotchSelectedInterface
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class WelcomeNotchType : DaggerFragment(),NotchSelectedInterface {


    lateinit var binding: FragmentWelcomeNotchTypeBinding

    companion object {
        fun newInstance() = WelcomeNotchType()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewmodel by lazy{
        ViewModelProvider(this,  viewModelFactory)[FragmentsViewModel::class.java]
    }

    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity(),viewModelFactory)[MainActivityViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_welcome_notch_type,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObserver()
        viewmodel.getNotchList()
        initClickListener()
    }

    private fun initClickListener() {
        binding.nextBtn.setOnClickListener {
            viewmodel.getSelectedNotch()?.let {
                viewmodel.setupNotch(it)
                sharedViewModel.changeFragment(SetupNotchPositionFragment.newInstance())
            } ?: Toast.makeText(context,"Please Select Notch type to proceed",Toast.LENGTH_SHORT).show()
        }
    }

    private fun initObserver() {
        viewmodel.listMLD.observe(viewLifecycleOwner) {
            it?.let {
                rvAdapter.submitList(it)
//                binding.notchType.post(Runnable { rvAdapter.updateList(it)})
            }
        }
    }

    private var rvAdapter = RecyclerViewAdapter(this)

    private fun initAdapter() {
        binding.notchType.layoutManager  = LinearLayoutManager(context)
        binding.notchType.adapter = rvAdapter
    }

    override fun onClicked(isSelected: Boolean, adapterPosition: Int) {
        viewmodel.onNotchClicked(isSelected,adapterPosition)
    }

}