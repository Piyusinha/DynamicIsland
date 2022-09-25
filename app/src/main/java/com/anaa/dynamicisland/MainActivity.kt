package com.anaa.dynamicisland

import MyLifecycleOwner
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.compositionContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.anaa.dynamicisland.databinding.ActivityMain2Binding
import com.anaa.dynamicisland.databinding.DefaultNotchViewBinding
import com.anaa.dynamicisland.fragments.PermissionFragment
import com.anaa.dynamicisland.fragments.SetupNotchPositionFragment
import com.anaa.dynamicisland.ui.compose.DynamicIslandComposibleView
import com.anaa.dynamicisland.ui.compose.IslandState
import com.anaa.dynamicisland.ui.view.DynamicLayoutParams
import com.anaa.dynamicisland.utils.DynamixSharedPref
import com.anaa.dynamicisland.utils.ScreenUtils
import com.anaa.dynamicisland.utils.liveData.toFreshLiveData
import com.anaa.dynamicisland.view.WindowsViewInflater
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
                beginTransaction().add(R.id.container, PermissionFragment.newInstance()).commit()
            }
        }
        updateProgress()
        initObserver()
        biding.progress.max = 2
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