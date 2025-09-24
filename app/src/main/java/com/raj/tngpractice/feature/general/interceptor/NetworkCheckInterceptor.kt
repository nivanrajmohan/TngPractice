package com.raj.tngpractice.feature.general.interceptor

import android.content.Context
import com.raj.tngpractice.R
import com.raj.tngpractice.util.DeviceUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkCheckInterceptor(private val appContext : Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!DeviceUtil.isNetworkAvailable(appContext)) {
            throw IOException(appContext.getString(R.string.network_unavailable))
        }
        return chain.proceed(chain.request())
    }

}