package com.vangood.chatfrag0315

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.vangood.chatfrag0315.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    lateinit var binding:FragmentSignUpBinding
    val selectPictureFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()){
            uri ->
            uri?.let{
                binding.imageHead.setImageURI(it)
                uri.toString()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        val s ="ssssss"
        val uri = Uri.parse(s)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageBHead.setOnClickListener {
            pickFromGallery()
        }
        binding.bSend.setOnClickListener {
            if (binding.edNickname!=null && binding.edUseraccount!=null && binding.edUserpassword!= null){
                val pref = requireContext().getSharedPreferences("chat", Context.MODE_PRIVATE)
                pref.edit()
                    .putString("USER_NAME",binding.edUseraccount.text.toString())
                    .putString("PASSWORD",binding.edUserpassword.text.toString())
                    .apply()
            }
        }
    }

    private fun pickFromGallery(){
        selectPictureFromGallery.launch("image/*")

    }


}