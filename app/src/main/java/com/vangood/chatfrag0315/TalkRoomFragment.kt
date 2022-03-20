package com.vangood.chatfrag0315

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.vangood.chatfrag0315.databinding.FragmentTalkRoomBinding
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
        val instance : TalkRoomFragment by lazy {
            TalkRoomFragment()
        }
    }
    lateinit var binding: FragmentTalkRoomBinding
    lateinit var websocket: WebSocket
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

        val parentActivity =  requireActivity() as MainActivity
        val prefLogin = requireContext().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        val prefUser = requireContext().getSharedPreferences("userinfo", AppCompatActivity.MODE_PRIVATE)
        var login = prefLogin.getBoolean("login_state", false)
        var user = prefLogin.getString("login_userid", "")
        var username = prefUser.getString("${user}name", "guest")
        var requestName = "guest"

        if (login) {
            requestName = username.toString()
        }

        var uri = Uri.parse("android.resource://"+requireContext().packageName+"/raw/her")

        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url("wss://lott-dev.lottcube.asia/ws/chat/chat:app_test?nickname=$requestName")
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
                val json = text
                if("default_message" in json){
                    val req = Gson().fromJson(json,DefaultMessage::class.java)
                    Log.d(TAG, req.body.nickname)
                    Log.d(TAG, req.body.text)

                }else if ("sys_updateRoomStatus" in json) {
                    val req = Gson().fromJson(json,SysUpdateRoomStatus::class.java)
                    var action = req.body.entry_notice.action
                    if (action == "enter") {
                        Log.d(TAG, "Hello ${req.body.entry_notice.username} come")
                    } else if (action == "leave") {
                        Log.d(TAG, " ${req.body.entry_notice.username} leave")
                    }
                }else if ("admin_all_broadcast" in json) {
                    val req = Gson().fromJson(json,AdminInAllBroadcast::class.java)
                    Log.d(TAG, req.body.content.cn)
                }else if("sys_room_endStream" in json){
                    val req = Gson().fromJson(json,SysRoomEndStream::class.java)
                    Log.d(TAG, req.body.type)
                }
                else{
                    Log.d(TAG, "onMessage: -> event: undefined")
                }

            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Log.d(TAG, ": onMessage ${bytes.hex()}");
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

        binding.btLeave.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.message))
                .setMessage(getString(R.string.leave))
                .setPositiveButton(getString(R.string.yes)) { d, w ->
                    //goto home
                }
                .setNegativeButton(getString(R.string.nono), null)
                .show()

        }

    }

}