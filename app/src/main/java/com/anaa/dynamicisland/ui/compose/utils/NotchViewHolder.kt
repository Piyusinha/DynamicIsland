package com.anaa.dynamicisland.ui.compose.utils

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.runtime.Recomposer
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.compositionContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.anaa.dynamicisland.MainActivityViewModel
import com.anaa.dynamicisland.accessibilty.viewModel.ComposeViewModel
import com.anaa.dynamicisland.accessibilty.viewModel.ComposeViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NotchViewHolder(context: Context) {
  val view = ComposeView(context)
  lateinit var viewModel :ComposeViewModel

  init {
    view.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    composeViewConfigure(view)
  }


fun composeViewConfigure(composeView: ComposeView) {
  val lifecycleOwner = MyLifecycleOwner()
  lifecycleOwner.performRestore(null)
  lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
  ViewTreeLifecycleOwner.set(composeView, lifecycleOwner)
  composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)

  val viewModelStore = ViewModelStore()
  ViewTreeViewModelStoreOwner.set(composeView) { viewModelStore }

  val coroutineContext = AndroidUiDispatcher.CurrentThread
  val runRecomposeScope = CoroutineScope(coroutineContext)
  val recomposer = Recomposer(coroutineContext)
  viewModel = ViewModelProvider(viewModelStore, ComposeViewModelFactory())[ComposeViewModel::class.java]
  composeView.compositionContext = recomposer

  runRecomposeScope.launch {
    recomposer.runRecomposeAndApplyChanges()
  }
}

// https://stackoverflow.com/questions/64585547/jetpack-compose-crash-when-adding-view-to-window-manager
internal class MyLifecycleOwner : SavedStateRegistryOwner {
  private var mLifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
  private var mSavedStateRegistryController: SavedStateRegistryController =
    SavedStateRegistryController.create(this)
  val isInitialized: Boolean
    get() = true

  override fun getLifecycle(): Lifecycle {
    return mLifecycleRegistry
  }

  override val savedStateRegistry: SavedStateRegistry
    get() = mSavedStateRegistryController.savedStateRegistry

  fun setCurrentState(state: Lifecycle.State) {
    mLifecycleRegistry.currentState = state
  }

  fun handleLifecycleEvent(event: Lifecycle.Event) {
    mLifecycleRegistry.handleLifecycleEvent(event)
  }

//  override fun getSavedStateRegistry(): SavedStateRegistry {
//    return mSavedStateRegistryController.savedStateRegistry
//  }

  fun performRestore(savedState: Bundle?) {
    mSavedStateRegistryController.performRestore(savedState)
  }

  fun performSave(outBundle: Bundle) {
    mSavedStateRegistryController.performSave(outBundle)
  }
}
}