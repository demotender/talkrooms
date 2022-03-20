package com.vangood.chatfrag0315


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class RoomViewModel : ViewModel() {
    // LiveData
    val chatRooms = MutableLiveData<List<Lightyear>>()
    val searchRooms = MutableLiveData<List<Lightyear>>()

    fun getAllRooms() {
        viewModelScope.launch(Dispatchers.IO) {

            val json = URL("https://api.jsonserve.com/qHsaqy").readText()
            val response = Gson().fromJson(json, ChatRooms::class.java)
            chatRooms.postValue(response.result.lightyear_list)
        }
    }
    fun getSearchRooms(keywords : String) {
        viewModelScope.launch(Dispatchers.IO) {

            val json = URL("https://api.jsonserve.com/qHsaqy").readText()
            val response = Gson().fromJson(json, ChatRooms::class.java)
            val searchKeyMap = mutableMapOf<String, Lightyear>()
            var keysearch = mutableListOf<String>()
            val resultRoomsSet = mutableSetOf<Lightyear>()

            response.result.lightyear_list.forEach {
                searchKeyMap.put(it.nickname, it)
                searchKeyMap.put(it.stream_title, it)
                searchKeyMap.put(it.tags, it)
                keysearch.add(it.nickname)
                keysearch.add(it.stream_title)
                keysearch.add(it.tags)
            }
            if (keywords == "") {
                resultRoomsSet.clear()
            } else {
                resultRoomsSet.clear()
                keysearch.forEach {
                    if (keywords in it) {
                        searchKeyMap[it]?.let {
                                matchRoom -> resultRoomsSet.add(matchRoom)
                        }
                    }
                }
            }
            searchRooms.postValue(resultRoomsSet.toList())
        }
    }

    fun getHitRooms() {
        viewModelScope.launch(Dispatchers.IO) {
            val json = URL("https://api.jsonserve.com/qHsaqy").readText()
            val response = Gson().fromJson(json, ChatRooms::class.java)
            val hitKeyMap = mutableMapOf<Int, Lightyear>()
            var keysearch = mutableListOf<Int>()
            val resultRooms = mutableListOf<Lightyear>()

            response.result.lightyear_list.forEach {
                hitKeyMap.put(it.online_num, it)
                keysearch.add(it.online_num)
            }

            keysearch.sortDescending()

            keysearch.forEach {
                hitKeyMap[it]?.let { sortedRoom -> resultRooms.add(sortedRoom) }
            }
            chatRooms.postValue(resultRooms)
            Log.d("Room viewModel", "first room = ${resultRooms[0].nickname}")
        }
    }

}