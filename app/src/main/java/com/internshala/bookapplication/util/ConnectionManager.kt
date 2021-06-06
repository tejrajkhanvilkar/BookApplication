package com.internshala.bookapplication.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo

class ConnectionManager {
    fun checkConnectivity(context: Context):Boolean{

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager//this will givw the info about currently active netowrk

        val activenetwork:NetworkInfo?=connectivityManager.activeNetworkInfo

        if(activenetwork?.isConnected!=null){
            return activenetwork.isConnected
        }else{
            return false
        }

    }
}