package com.vangood.chatfrag0315

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val TAG = LoginViewModel::class.java.simpleName
    //val remember_me = MutableLiveData<Boolean>()

    fun loginJudge (Data_account: String?, Data_password: String?, username: String?, pass: String?) :Boolean {
        return if (Data_account == username && Data_password == pass) {
            Log.d(TAG, "Login success")
            true
        } else false
    }
}