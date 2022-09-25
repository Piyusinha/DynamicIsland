package com.anaa.dynamicisland.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.anaa.dynamicisland.databinding.DialogFragmentBinding

class SimpleDialog : DialogFragment() {

    var buttonInterface: onButtonClick? = null
    companion object {

        const val TAG = "SimpleDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(title: String, subTitle: String): SimpleDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            val fragment = SimpleDialog()
            fragment.arguments = args
            return fragment
        }

    }

    lateinit var binding: DialogFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupClickListeners(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupView(view: View) {
      binding.header.text = arguments?.getString(KEY_TITLE)
      binding.description.text = arguments?.getString(KEY_SUBTITLE)
    }

    private fun setupClickListeners(view: View) {
        binding.close.setOnClickListener {
            buttonInterface?.cancelClicked()
            dismiss()
        }
        binding.ok.setOnClickListener {
            buttonInterface?.okClicked()
            dismiss()
        }
    }
    interface onButtonClick {
        fun okClicked()
        fun cancelClicked()
    }
}