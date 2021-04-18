package store.pengu.mobile.views.partials

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast
import pt.inesc.termite.wifidirect.SimWifiP2pInfo
import store.pengu.mobile.views.MainActivity

class WifiP2pBroadcastReceiver(activity: MainActivity) : BroadcastReceiver() {
    private val mActivity: MainActivity = activity

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION == action) {

            // This action is triggered when the Termite service changes state:
            // - creating the service generates the WIFI_P2P_STATE_ENABLED event
            // - destroying the service generates the WIFI_P2P_STATE_DISABLED event
            val state = intent.getIntExtra(SimWifiP2pBroadcast.EXTRA_WIFI_STATE, -1)
            if (state == SimWifiP2pBroadcast.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(
                    mActivity, "WiFi Direct enabled",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    mActivity, "WiFi Direct disabled",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } else if (SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION == action) {

            // Request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            Toast.makeText(
                mActivity, "Peer list changed",
                Toast.LENGTH_SHORT
            ).show()

        } else if (SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION == action) {
            val info = intent.getSerializableExtra(SimWifiP2pBroadcast.EXTRA_GROUP_INFO) as SimWifiP2pInfo?
            info!!.print()
            Toast.makeText(
                mActivity, "Network membership changed",
                Toast.LENGTH_SHORT
            ).show()

        } else if (SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION == action) {
            val info = intent.getSerializableExtra(SimWifiP2pBroadcast.EXTRA_GROUP_INFO) as SimWifiP2pInfo?
            info!!.print()
            Toast.makeText(
                mActivity, "Group ownership changed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}