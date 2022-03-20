package com.vangood.chatfrag0315

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TalkRoomViewModel: ViewModel() {
    val talkRooms = MutableLiveData<DefaultMessage>()
    fun getALLRooms(message:DefaultMessage){
            talkRooms.postValue(message)
        }

}