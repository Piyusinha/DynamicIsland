package com.anaa.dynamicisland

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.anaa.dynamicisland.databinding.ActivityMain2Binding
import com.anaa.dynamicisland.fragments.SetupNotchPositionFragment
import com.anaa.dynamicisland.fragments.WelcomeNotchType
import com.anaa.dynamicisland.utils.DynamixSharedPref
import com.anaa.dynamicisland.utils.ScreenUtils
import com.anaa.dynamicisland.utils.liveData.toFreshLiveData
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

    private var activityOverlayResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if(Settings.canDrawOverlays(this)) {

        }
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
            supportFragmentManager.apply {
                beginTransaction().add(R.id.container, WelcomeNotchType.newInstance()).commit()
            }
        }
        updateProgress()
        initObserver()
        biding.progress.max = 2
    }

    private fun initObserver() {
        viewModel.changeFragment.toFreshLiveData().observe(this) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                activityOverlayResult.launch(intent)
            } else{
                changeFragment(it)
            }

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
        resetView()
        super.onBackPressed()
        viewModel.count--
        updateProgress()
    }

    private fun resetView() {
        val currentFrag = supportFragmentManager.findFragmentById(R.id.container)
        if(currentFrag is SetupNotchPositionFragment) {
            currentFrag.onBackPressed()
        }
    }
}