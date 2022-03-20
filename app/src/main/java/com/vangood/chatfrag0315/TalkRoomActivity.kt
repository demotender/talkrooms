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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.vangood.chatfrag0315.databinding.ActivityTalkRoomBinding
import com.vangood.chatfrag0315.databinding.RowMsgBinding
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class TalkRoomActivity : AppCompatActivity() {
    private val TAG = TalkRoomActivity::class.java.simpleName
    lateinit var binding:ActivityTalkRoomBinding

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
                var msg = text
                Log.d(TAG, ": onMessage $text");
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

    }
    /*inner class TalkRoomAdapter :RecyclerView.Adapter<TalkViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalkViewHolder {
            val binding = RowMsgBinding.inflate(layoutInflater,parent,false)
            return TalkViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TalkViewHolder, position: Int) {
            val Msg = talks[position]
            holder.left_msg.setText(Msg.body.text)
            holder.user_msg.setText(Msg.body.nickname)
        }

        override fun getItemCount(): Int {
            return 1
        }

    }
    inner class TalkViewHolder(val binding: RowMsgBinding): RecyclerView.ViewHolder(binding.root){
        var left_msg = binding.leftMsg
        var user_msg = binding.leftName
    }*/



}
data class Message(val action:String, val content: String)