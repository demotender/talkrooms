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
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.vangood.chatfrag0315.databinding.FragmentTalkRoomBinding
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit


class TalkRoomFragment : Fragment() {
    /*
    val lyviewModel by viewModels<MyViewmodel>()
    val TAG = TalkRoomActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_talk_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val room = arguments?.getParcelable<Lightyear>("room")
        Log.d(TAG, "room: ${room?.stream_title}");

        lyviewModel.getroom()
        val room2 = lyviewModel.roomvalue
        Log.d(TAG, "room2: ${room2.toString()}")


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

        var vidPath = "android.resource://"+requireContext().packageName+"/raw/her"
        var uri = Uri.parse(vidPath)

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
                if ("sys_updateRoomStatus" in json) {
                    val response = Gson().fromJson(json, UpdateRoomStatus::class.java)
                    var action = response.body.entry_notice.action
                    if (action == "enter") {
                        Log.d(TAG, "Hello ${response.body.entry_notice.username} come")
                    } else if (action == "leave") {
                        Log.d(TAG, " ${response.body.entry_notice.username} leave")
                    }
                } else if ("admin_all_broadcast" in json) {
                    val response = Gson().fromJson(json, AllBroadcast::class.java)
                    Log.d(TAG, response.body.content.en)
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

        binding.vGirl.setVideoURI(uri)
        binding.vGirl.setOnPreparedListener {
            binding.vGirl.start()
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