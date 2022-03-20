package com.vangood.chatfrag0315

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.vangood.chatfrag0315.databinding.ActivityTalkRoomBinding
import com.vangood.chatfrag0315.databinding.RowMsgBinding
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class TalkRoomActivity : AppCompatActivity() {
    private val TAG = TalkRoomActivity::class.java.simpleName
    lateinit var binding:ActivityTalkRoomBinding
    private  lateinit var  adapter:TalkRoomAdapter
    val viewModel by viewModels<TalkRoomViewModel>()

    val messages = mutableListOf<String>("呼呼","嘿","哈哈哈哈哈","呼呼","嘿","哈哈哈哈哈","呼呼","嘿","哈哈哈哈哈")
    var map = mutableMapOf<Int,String>(0 to "Sun", 1 to "Mon")

    /*val talks = mutableListOf<default_message>()
    private lateinit var  adapter :TalkRoomAdapter*/

    //websocket
    lateinit var websocket: WebSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTalkRoomBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pref = getSharedPreferences("chat", Context.MODE_PRIVATE)
        var user = "Guest"

        if(pref.getBoolean("login_state",true)){
            user=pref.getString("DATA_USER_NAME","")!!
        }

        //Web socket
        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url("wss://lott-dev.lottcube.asia/ws/chat/chat:app_test?nickname=$user")
            .build()
        websocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d(TAG, ": onClosed");
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d(TAG, ": onClosing");
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.d(TAG, ": onFailure");
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val Msg = text
                if("default_message" in Msg){
                    val req = Gson().fromJson(Msg,DefaultMessage::class.java)
                    Log.d(TalkRoomFragment.TAG, req.body.nickname)
                    Log.d(TalkRoomFragment.TAG, req.body.text)

                }else if ("sys_updateRoomStatus" in Msg) {
                    val req = Gson().fromJson(Msg,SysUpdateRoomStatus::class.java)
                    var action = req.body.entry_notice.action
                    if (action == "enter") {
                        Log.d(TalkRoomFragment.TAG, "Hello ${req.body.entry_notice.username} come")
                    } else if (action == "leave") {
                        Log.d(TalkRoomFragment.TAG, " ${req.body.entry_notice.username} leave")
                    }
                }else if ("admin_all_broadcast" in Msg) {
                    val req = Gson().fromJson(Msg,AdminInAllBroadcast::class.java)
                    Log.d(TalkRoomFragment.TAG, req.body.content.cn)
                }else if("sys_room_endStream" in Msg){
                    val req = Gson().fromJson(Msg,SysRoomEndStream::class.java)
                    Log.d(TalkRoomFragment.TAG, req.body.type)
                }
                else{
                    Log.d(TalkRoomFragment.TAG, "onMessage: -> event: undefined")
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Log.d(TAG, ": onMessage ${bytes.hex()}");
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d(TAG, ": onOpen success connect");
//                webSocket.send("Hello, I am Hank")
            }
        })
        /*binding.recyclerTalkBar.setHasFixedSize(true)
        binding.recyclerTalkBar.layoutManager = LinearLayoutManager(this)
        adapter = TalkRoomAdapter()
        binding.recyclerTalkBar.adapter = adapter*/

        binding.bSendtalking.setOnClickListener {
            val message = binding.talkSend.text.toString()
//            val json = "{\"action\": \"N\", \"content\": \"$message\" }"
//            websocket.send(json)
            websocket.send(Gson().toJson(Message("N", message)))
            binding.talkSend.setText("")
        }

        binding.bTalkout.setOnClickListener {

            val item = LayoutInflater.from(this).inflate(R.layout.heart, null)
            AlertDialog.Builder(this)
                .setView(item)
                .setPositiveButton(getString(R.string.ok)) { d, w ->
                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton(getString(R.string.stay)){
                    d,w->null
                }
                .show()

        }

        //binding.videoView.setVideoURI((Uri.parse("@")))
        var videoview = binding.videoView
        val uri :Uri = Uri.parse("android.resource://$packageName/raw/her")
        videoview.setVideoURI(uri)
        videoview.setOnPreparedListener {
            videoview.start()
        }
        binding.recyclerTalkBar.setHasFixedSize(true)
        binding.recyclerTalkBar.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true)
        binding.recyclerTalkBar.scrollToPosition(adapter.itemCount-1)
        adapter = TalkRoomAdapter()
        binding.recyclerTalkBar.adapter = adapter
        /*viewModel.talkRooms.observe(viewLifecycleOwner) { message ->
            adapter.submitRooms(message)
        }
        viewModel.getALLRooms()*/


    }
    inner class TalkRoomAdapter:RecyclerView.Adapter<TalkRoomViewHolder>(){
        val talkRooms = mutableListOf<DefaultMessage>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalkRoomViewHolder {
            val binding = RowMsgBinding.inflate(layoutInflater,parent,false)
            return TalkRoomViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TalkRoomViewHolder, position: Int) {
            val message =talkRooms[position]
            //holder.nick.text = message.body.nickname
            val Mesage = message.body.text +":"+ message.body.text
            holder.msg.text = Mesage
        }

        override fun getItemCount(): Int {
            return talkRooms.size
        }
        fun submitRooms(rooms: List<DefaultMessage>) {
            talkRooms.addAll(rooms)
            notifyDataSetChanged()
        }

    }
    inner class TalkRoomViewHolder(val binding:RowMsgBinding) : RecyclerView.ViewHolder(binding.root) {
        //val nick = binding.leftName
        val msg = binding.leftMsg

    }

}
data class Message(val action:String, val content: String)