package com.vangood.chatfrag0315

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vangood.chatfrag0315.databinding.ActivityTalkRoomBinding

class TalkRoomActivity : AppCompatActivity() {
    lateinit var binding:ActivityTalkRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTalkRoomBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}