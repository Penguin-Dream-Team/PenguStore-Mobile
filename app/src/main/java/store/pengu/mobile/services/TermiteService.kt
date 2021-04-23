package store.pengu.mobile.services

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Messenger
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.inesc.termite.wifidirect.SimWifiP2pManager
import pt.inesc.termite.wifidirect.service.SimWifiP2pService
import store.pengu.mobile.views.MainActivity

class TermiteService(private val mainActivity: MainActivity) {
    private var mManager: SimWifiP2pManager? = null
    private var mChannel: SimWifiP2pManager.Channel? = null
    private var mBound = false

    /*
     * Termite Actions
     */
    fun wifiDirectON() = run {
        val intent = Intent(mainActivity.applicationContext, SimWifiP2pService::class.java)
        mainActivity.bindService(intent, mConnection, AppCompatActivity.BIND_AUTO_CREATE)
        mBound = true
    }

    fun wifiDirectOFF() = run {
        if (mBound) {
            mainActivity.unbindService(mConnection)
            mBound = false
        }
    }

    fun wifiDirectPeersAvailable() = run {
        if (mBound) {
            mManager!!.requestPeers(mChannel, mainActivity)
        } else {
            Toast.makeText(
                mainActivity.applicationContext, "Service not bound",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val mConnection: ServiceConnection = object : ServiceConnection {
        // callbacks for service binding, passed to bindService()
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mManager = SimWifiP2pManager(Messenger(service))
            mChannel = mManager!!.initialize(mainActivity.application, mainActivity.mainLooper, null)
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mManager = null
            mChannel = null
            mBound = false
        }
    }
}