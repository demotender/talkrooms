package com.vangood.chatfrag0315

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TalkRoomViewModel: ViewModel() {
    val talkRooms = MutableLiveData<String>()
    fun setMsgData(message:String){

            talkRooms.postValue(message)
        }

}