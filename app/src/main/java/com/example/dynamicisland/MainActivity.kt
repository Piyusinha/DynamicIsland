package com.example.dynamicisland

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dynamicisland.databinding.ActivityMain2Binding
import com.example.dynamicisland.fragments.WelcomeNotchType
import com.example.dynamicisland.utils.DynamixSharedPref
import com.example.dynamicisland.utils.ScreenUtils
import com.example.dynamicisland.utils.liveData.toFreshLiveData
import com.example.dynamicisland.viewmodelFactory.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {

    lateinit var biding : ActivityMain2Binding

    @Inject
    lateinit var viewModelFactory:ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[MainActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding =  ActivityMain2Binding.inflate(layoutInflater)
        setContentView(biding.root)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
        DynamixSharedPref(this).setWidth(ScreenUtils().getScreenWidth(this))
        if (savedInstanceState == null) {
            changeFragment(WelcomeNotchType.newInstance())
        }
        initObserver()
        biding.progress.max = 3
    }

    private fun initObserver() {
        viewModel.changeFragment.toFreshLiveData().observe(this) {
            changeFragment(it)
        }
    }

    private fun updateProgress() {
        biding.progress.setProgress(viewModel.count,true)
    }

    private fun changeFragment(fragment: DaggerFragment) {
        supportFragmentManager.apply {
            beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit()
        }
        updateProgress()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.count--
        updateProgress()
    }
}