package com.anaa.dynamicisland.accessibilty

import android.accessibilityservice.AccessibilityService
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaMetadata
import android.view.Gravity
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.anaa.dynamicisland.AccessbilityStaticClass
import com.anaa.dynamicisland.R
import com.anaa.dynamicisland.accessibilty.viewModel.ComposeViewModel
import com.anaa.dynamicisland.application.DynamicApplication
import com.anaa.dynamicisland.broadcast.bluetooth.BluetoothBroadcastReciever
import com.anaa.dynamicisland.broadcast.bluetooth.BluetoothListener
import com.anaa.dynamicisland.broadcast.charging.ChargeBroadcastReciever
import com.anaa.dynamicisland.broadcast.charging.ChargingListener
import com.anaa.dynamicisland.broadcast.ringer.RingerBroadcastReciever
import com.anaa.dynamicisland.broadcast.ringer.RingerMode
import com.anaa.dynamicisland.ui.compose.DynamicIslandComposibleView
import com.anaa.dynamicisland.ui.compose.IslandState
import com.anaa.dynamicisland.ui.compose.utils.NotchIslandStateSealedClass
import com.anaa.dynamicisland.ui.compose.utils.NotchViewHolder
import com.anaa.dynamicisland.ui.view.DynamicLayoutParams
import com.anaa.dynamicisland.utils.DynamixSharedPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

@OptIn(ExperimentalAnimationApi::class)
class ComposeAccessibiltyService : AccessibilityService() {
    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {

    }

    private var viewAdded = false

    lateinit var job: Job
    lateinit var scope : CoroutineScope

    lateinit var windowsViewInflater: WindowManager

    var state = mutableStateOf(IslandState.AIRPODS)

    @Inject
    lateinit var sharedPref: DynamixSharedPref

    override fun onInterrupt() {

    }

    override fun onCreate() {
        super.onCreate()
        (applicationContext as DynamicApplication).getMainAppComponent().inject(this)

    }

    private var defaultLayoutParams : WindowManager.LayoutParams? = null

    lateinit var notchViewHolder :NotchViewHolder

    lateinit var composeViewModel: ComposeViewModel

    override fun onServiceConnected() {
        AccessbilityStaticClass.service = this
        (application as DynamicApplication).setAccessibilityService(this)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }


    fun enableView() {
        if(viewAdded) {
            return
        }
        defaultLayoutParams = DynamicLayoutParams.getLayoutParams(sharedPref.getX(),sharedPref.getY(), WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,getGravity())
        job = SupervisorJob()
        scope = CoroutineScope(Dispatchers.Main + job)

        notchViewHolder = NotchViewHolder(applicationContext)
        composeViewModel = notchViewHolder.viewModel
        notchViewHolder.view.setContent {
            DynamicIslandComposibleView(composeViewModel.viewState.value,Size(sharedPref.getDimension().dp.value,sharedPref.getDimension().dp.value), notch = sharedPref.getNotch(),sharedPref.getRadius().toInt())
        }
        windowsViewInflater = getSystemService(WINDOW_SERVICE) as WindowManager
        windowsViewInflater.addView(notchViewHolder.view,defaultLayoutParams)

        initBroadCastRecievers()
        viewAdded = true

    }

    private fun initBroadCastRecievers() {
        registerReceiver(
            ChargeBroadcastReciever.broadcastReceiver,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        registerReceiver(
            RingerBroadcastReciever.receiver,
            IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION)
        )
        registerReceiver(
            BluetoothBroadcastReciever.broadcastReceiver,
            IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED)
        )
        setupBroadcastListner()
    }

    private fun setupBroadcastListner() {
        setupChargeListener()
        setupRingerListener()
        setupBluetoothListener()
    }

    private fun setupBluetoothListener() {
        BluetoothBroadcastReciever.setListener(object : BluetoothListener {
            override fun onDeviceConnected(device: BluetoothDevice?) {
                composeViewModel.changeView(NotchIslandStateSealedClass.BluetoothConnected(device))
            }
        })
    }

    private fun setupRingerListener() {
        RingerBroadcastReciever.setListener(object : RingerMode {
            override fun onVibrate() {

            }

            override fun onGeneral() {
                composeViewModel.changeView(NotchIslandStateSealedClass.RingerNotch("Ring",R.drawable.ic_leftnormal))
            }

            override fun onSilent() {
                composeViewModel.changeView(NotchIslandStateSealedClass.RingerNotch("Silent",R.drawable.ic_leftsilent_icon))
            }

        })
    }

    private fun setupChargeListener() {
        ChargeBroadcastReciever.setListener(object : ChargingListener {
            override fun onChargeConnected(level: Int?) {
                composeViewModel.changeView(NotchIslandStateSealedClass.ChargingNotch(level ?: 100,getBatteryIcon(level)))

            }
            override fun onChargeDisconnected() {

            }
        })
    }

    fun enableSetupView() {
        defaultLayoutParams = DynamicLayoutParams.getLayoutParams(0,0, WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,getGravity())
        job = SupervisorJob()
        scope = CoroutineScope(Dispatchers.Main + job)

        notchViewHolder = NotchViewHolder(applicationContext)

        composeViewModel = notchViewHolder.viewModel
        notchViewHolder.view.setContent {
            DynamicIslandComposibleView(
                composeViewModel.viewState.value,
                composeViewModel.size.value,
                notch = sharedPref.getNotch(),
                roundedCorner = composeViewModel.radius.value
            )
        }
        windowsViewInflater = getSystemService(WINDOW_SERVICE) as WindowManager
        windowsViewInflater.addView(notchViewHolder.view,defaultLayoutParams)
    }

    fun updateView(x:Int,y:Int) {
        defaultLayoutParams?.x  = x
        defaultLayoutParams?.y = y
        windowsViewInflater.updateViewLayout(notchViewHolder.view,defaultLayoutParams)
    }

    fun removeView() {
        windowsViewInflater.removeView(notchViewHolder.view)
        viewAdded = false
        ChargeBroadcastReciever.settingReceiver = true
        RingerBroadcastReciever.settingReceiver = true
    }

    private fun getGravity(): Int {
        when(sharedPref.getNotch()) {
            0 -> return Gravity.TOP or Gravity.LEFT
            1 -> return Gravity.TOP or Gravity.RIGHT
            2,3 -> return Gravity.TOP or Gravity.CENTER
        }
        return -1
    }

    fun updateRadius(radius:Int) {
        composeViewModel.changeRadius(radius)
    }

    fun changeSize(size:Int) {
        composeViewModel.changeSize(size)
    }

    fun updateState(islandState: NotchIslandStateSealedClass) {
        composeViewModel.changeView(islandState)
    }



    override fun onUnbind(intent: Intent?): Boolean {
        unregisterReceiver(ChargeBroadcastReciever.broadcastReceiver)
        unregisterReceiver(RingerBroadcastReciever.receiver)
        viewAdded = false
        AccessbilityStaticClass.service = null
        ChargeBroadcastReciever.settingReceiver = true
        RingerBroadcastReciever.settingReceiver = true
        return super.onUnbind(intent)
    }

    private fun getBatteryIcon( level: Int?): Int {
        return when(level) {
            in 90..100 -> R.drawable.ic_chargepercentage_100
            in 75..90 -> R.drawable.ic_chargepercentage_75
            in 50..75 -> R.drawable.ic_chargepercentage_50
            in 20..50 -> R.drawable.ic_chargepercentage_25
            else -> R.drawable.ic_chargepercentage_0
        }

    }

    fun onMusicStartPlaying(metadata: MediaMetadata) {
        val musicNotch = NotchIslandStateSealedClass.MusicSmallView(composeViewModel.getMediaBitmapData(metadata))
        composeViewModel.defaultNotch = musicNotch
        composeViewModel.changeView(musicNotch)
    }

    fun onMusicStopped() {
        composeViewModel.defaultNotch = NotchIslandStateSealedClass.DefaultNotch
        composeViewModel.changeView(composeViewModel.defaultNotch)
    }

}