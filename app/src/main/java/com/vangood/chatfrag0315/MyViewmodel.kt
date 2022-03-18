package com.vangood.chatfrag0315

import android.os.Bundle
import android.os.PersistableBundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewmodel:ViewModel() {
    val roomvalue = MutableLiveData<Lightyear>()
    fun setroom(ly:Lightyear){
        roomvalue.value = ly
    }
    fun getroom(): MutableLiveData<Lightyear> = roomvalue

}