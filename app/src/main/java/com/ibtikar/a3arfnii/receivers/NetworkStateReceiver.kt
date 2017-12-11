package com.ibtikar.a3arfnii.receivers

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.ibtikar.a3arfnii.events.NetworkStateChange
import com.ibtikar.a3arfnii.model.utils.Constants
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


class NetworkStateReceiver : BroadcastReceiver(){


    @Inject
    lateinit  var context:Context

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(ContentValues.TAG, "Network connectivity change")
        val manager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = manager.activeNetworkInfo
        if (ni == null || ni.state != NetworkInfo.State.CONNECTED) {
            Log.d(ContentValues.TAG, "There's no network connectivity")

        } else {
            Log.d(ContentValues.TAG, "Network " + ni.typeName + " connected")
//            val intent = Intent()
//            intent.action = Constants.RECEIVE_NETWORK_STATE_ACTION
//            context.sendBroadcast(intent)

            EventBus.getDefault().post(NetworkStateChange(true))

        }
    }
    }
