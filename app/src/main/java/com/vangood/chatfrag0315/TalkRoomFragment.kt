package com.vangood.chatfrag0315

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TalkRoomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TalkRoomFragment : Fragment() {
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
        lyviewModel.getroom()
        val room2 = lyviewModel.roomvalue
        Log.d(TAG, "room: ${room?.stream_title}");
        Log.d(TAG, "room2: ${room2.toString()}")


    }
}