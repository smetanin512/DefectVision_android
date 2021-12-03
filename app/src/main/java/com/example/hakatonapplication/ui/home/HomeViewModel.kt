package com.alexvas.rtsp.demo.ui.live

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val RTSP_REQUEST_KEY = "rtsp_request"
private const val RTSP_USERNAME_KEY = "rtsp_username"
private const val RTSP_PASSWORD_KEY = "rtsp_password"

private const val LIVE_PARAMS_FILENAME1 = "live_params1"
private const val LIVE_PARAMS_FILENAME2 = "live_params2"
private const val LIVE_PARAMS_FILENAME3 = "live_params3"
private const val LIVE_PARAMS_FILENAME4 = "live_params4"

@SuppressLint("LogNotTimber")
class HomeViewModel : ViewModel() {

    companion object {
        private val TAG: String = HomeViewModel::class.java.simpleName
        private const val DEBUG = true
    }

    val rtspRequest1 = MutableLiveData<String>().apply {
        value = "tcp://94.26.229.85:5001"
    }
    val rtspUsername1 = MutableLiveData<String>().apply {
        value = ""
    }
    val rtspPassword1 = MutableLiveData<String>().apply {
        value = ""
    }

    val rtspRequest2 = MutableLiveData<String>().apply {
        value = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"
    }
    val rtspUsername2 = MutableLiveData<String>().apply {
        value = ""
    }
    val rtspPassword2 = MutableLiveData<String>().apply {
        value = ""
    }

    val rtspRequest3 = MutableLiveData<String>().apply {
        value = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"
    }
    val rtspUsername3 = MutableLiveData<String>().apply {
        value = ""
    }
    val rtspPassword3 = MutableLiveData<String>().apply {
        value = ""
    }

    val rtspRequest4 = MutableLiveData<String>().apply {
        value = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"
    }
    val rtspUsername4 = MutableLiveData<String>().apply {
        value = ""
    }
    val rtspPassword4 = MutableLiveData<String>().apply {
        value = ""
    }

    init {
        rtspRequest1.value = "tcp://94.26.229.85:5001"
        rtspRequest2.value = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"
        rtspRequest3.value = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"
        rtspRequest4.value = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"
    }


    fun loadParams1(context: Context?) {
        val pref = context?.getSharedPreferences(LIVE_PARAMS_FILENAME1, Context.MODE_PRIVATE)
        try {
            rtspRequest1.setValue(pref?.getString(RTSP_REQUEST_KEY, "tcp://94.26.229.85:5001"))
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
        try {
            rtspUsername1.setValue(pref?.getString(RTSP_USERNAME_KEY, ""))
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
        try {
            rtspPassword1.setValue(pref?.getString(RTSP_PASSWORD_KEY, ""))
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    fun loadParams2(context: Context?) {
        val pref = context?.getSharedPreferences(LIVE_PARAMS_FILENAME2, Context.MODE_PRIVATE)
        try {
            rtspRequest2.setValue(pref?.getString(RTSP_REQUEST_KEY, "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"))
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
        try {
            rtspUsername2.setValue(pref?.getString(RTSP_USERNAME_KEY, ""))
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
        try {
            rtspPassword2.setValue(pref?.getString(RTSP_PASSWORD_KEY, ""))
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    fun loadParams3(context: Context?) {
        val pref = context?.getSharedPreferences(LIVE_PARAMS_FILENAME3, Context.MODE_PRIVATE)
        try {
            rtspRequest3.setValue(pref?.getString(RTSP_REQUEST_KEY, "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"))
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
        try {
            rtspUsername3.setValue(pref?.getString(RTSP_USERNAME_KEY, ""))
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
        try {
            rtspPassword3.setValue(pref?.getString(RTSP_PASSWORD_KEY, ""))
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    fun loadParams4(context: Context?) {
        val pref = context?.getSharedPreferences(LIVE_PARAMS_FILENAME4, Context.MODE_PRIVATE)
        try {
            rtspRequest4.setValue(pref?.getString(RTSP_REQUEST_KEY, "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"))
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
        try {
            rtspUsername4.setValue(pref?.getString(RTSP_USERNAME_KEY, ""))
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
        try {
            rtspPassword4.setValue(pref?.getString(RTSP_PASSWORD_KEY, ""))
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    fun saveParams1(context: Context?) {
        if (DEBUG) Log.v(TAG, "saveParams()")
        val editor = context?.getSharedPreferences(LIVE_PARAMS_FILENAME1, Context.MODE_PRIVATE)?.edit()
        editor?.putString(RTSP_REQUEST_KEY, rtspRequest1.value)
        editor?.putString(RTSP_USERNAME_KEY, rtspUsername1.value)
        editor?.putString(RTSP_PASSWORD_KEY, rtspPassword1.value)
        editor?.apply()
    }

    fun saveParams2(context: Context?) {
        if (DEBUG) Log.v(TAG, "saveParams()")
        val editor = context?.getSharedPreferences(LIVE_PARAMS_FILENAME2, Context.MODE_PRIVATE)?.edit()
        editor?.putString(RTSP_REQUEST_KEY, rtspRequest2.value)
        editor?.putString(RTSP_USERNAME_KEY, rtspUsername2.value)
        editor?.putString(RTSP_PASSWORD_KEY, rtspPassword2.value)
        editor?.apply()
    }

    fun saveParams3(context: Context?) {
        if (DEBUG) Log.v(TAG, "saveParams()")
        val editor = context?.getSharedPreferences(LIVE_PARAMS_FILENAME3, Context.MODE_PRIVATE)?.edit()
        editor?.putString(RTSP_REQUEST_KEY, rtspRequest3.value)
        editor?.putString(RTSP_USERNAME_KEY, rtspUsername3.value)
        editor?.putString(RTSP_PASSWORD_KEY, rtspPassword3.value)
        editor?.apply()
    }

    fun saveParams4(context: Context?) {
        if (DEBUG) Log.v(TAG, "saveParams()")
        val editor = context?.getSharedPreferences(LIVE_PARAMS_FILENAME4, Context.MODE_PRIVATE)?.edit()
        editor?.putString(RTSP_REQUEST_KEY, rtspRequest4.value)
        editor?.putString(RTSP_USERNAME_KEY, rtspUsername4.value)
        editor?.putString(RTSP_PASSWORD_KEY, rtspPassword4.value)
        editor?.apply()
    }

}