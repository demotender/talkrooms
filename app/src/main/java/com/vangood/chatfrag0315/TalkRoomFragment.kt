package com.vangood.chatfrag0315

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.vangood.chatfrag0315.databinding.FragmentTalkRoomBinding
import com.vangood.chatfrag0315.databinding.RowMsgBinding
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit


class TalkRoomFragment : Fragment() {
    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val room = arguments?.getParcelable<Lightyear>("room")
        Log.d(TAG, "room: ${room?.stream_title}");
    }*/

    companion object {
        val TAG = TalkRoomFragment::class.java.simpleName

    }
    lateinit var binding: FragmentTalkRoomBinding
    lateinit var websocket: WebSocket
    lateinit var adapter : TalkRoomFAdapter
    val messageViewModel by viewModels<TalkRoomViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTalkRoomBinding.inflate(inflater)
//        return super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val Activity =  requireActivity() as MainActivity
        val pref = requireContext().getSharedPreferences("chat", Context.MODE_PRIVATE)
        var user = "Guest"

        if(pref.getBoolean("login_state",true)){
            user=pref.getString("DATA_USER_NAME","")!!
        }

        var uri = Uri.parse("android.resource://"+requireContext().packageName+"/raw/her")

        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url("wss://lott-dev.lottcube.asia/ws/chat/chat:app_test?nickname=$user")
            .build()

        websocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d(TAG, ": onClosed")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d(TAG, ": onClosing")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.d(TAG, ": onFailure")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val Msg = text
                var msg = ""
                if("default_message" in Msg){
                    val req = Gson().fromJson(Msg,DefaultMessage::class.java)
                    Log.d(TAG, req.body.nickname)
                    Log.d(TAG, req.body.text)
                    msg = req.body.nickname+":"+req.body.text

                }else if ("sys_updateRoomStatus" in Msg) {
                    val req = Gson().fromJson(Msg,SysUpdateRoomStatus::class.java)
                    var act = req.body.entry_notice.action
                    if (act == "enter") {
                        Log.d(TAG, "Hello ${req.body.entry_notice.username} come")
                    } else if (act == "leave") {
                        Log.d(TAG, " ${req.body.entry_notice.username} leave")
                    }
                }else if ("admin_all_broadcast" in Msg) {
                    val req = Gson().fromJson(Msg,AdminInAllBroadcast::class.java)
                    Log.d(TAG, req.body.content.cn)
                }else if("sys_room_endStream" in Msg){
                    val req = Gson().fromJson(Msg,SysRoomEndStream::class.java)
                    Log.d(TAG, req.body.type)
                }
                else{
                    Log.d(TAG, "onMessage: -> event: undefined")
                }
                messageViewModel.setMsgData(msg)

            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Log.d(TAG, ": onMessage ${bytes.hex()}")
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d(TAG, ": onOpen: response = $response")
            }
        })

        binding.vHer.setVideoURI(uri)
        binding.vHer.setOnPreparedListener {
            binding.vHer.start()
        }
        binding.btSend.setOnClickListener {
            var message = binding.edSendMessage.text.toString()
            var json = Gson().toJson(Message("N", message))
            binding.edSendMessage.setText("")
            websocket.send(json)
        }

        binding.btLeave.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.message))
                .setMessage(getString(R.string.leave))
                .setPositiveButton(getString(R.string.yes)) { d, w ->
                    //goto home
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.container, HomePopulerFragment())
                        .disallowAddToBackStack()
                        .commit()
                }
                .setNegativeButton(getString(R.string.nono), null)
                .show()

        }
        //recycler
        binding.msgRecycler.setHasFixedSize(true)
        binding.msgRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        adapter = TalkRoomFAdapter()
        binding.msgRecycler.adapter = adapter

        messageViewModel.talkRooms.observe(viewLifecycleOwner) { messages ->
            adapter.submitMessages(messages)
        }


    }
    inner class TalkRoomFAdapter : RecyclerView.Adapter<MessageViewHolder>() {
        val sendMessage = mutableListOf<String>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val binding = RowMsgBinding.inflate(layoutInflater, parent, false)
            return MessageViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            val msg = sendMessage[position]
            holder.messagetv.text = msg
        }
        override fun getItemCount(): Int {
            return sendMessage.size
        }
        fun submitMessages(messages: String) {
            sendMessage.add(0,messages)
            notifyDataSetChanged()
        }

    }

    inner class MessageViewHolder(val binding:RowMsgBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val messagetv = binding.leftMsg
    }

}