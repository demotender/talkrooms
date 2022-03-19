package com.vangood.chatfrag0315

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.vangood.chatfrag0315.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    lateinit var binding:FragmentSignUpBinding
    val viewModel by viewModels<SignUpViewModel>()
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageBHead.setOnClickListener {
            pickFromGallery()
        }
        binding.bSend.setOnClickListener {
            val nickname = binding.edNickname.text.toString()
            val useraccount = binding.edUseraccount.text.toString()
            val userpass = binding.edUserpassword.text.toString()


            if (viewModel.accountcheck(useraccount)){
                if (viewModel.passwordcheck(userpass)){
                    val pref = requireContext().getSharedPreferences("chat", Context.MODE_PRIVATE)
                    pref.edit()
                        .putString("DATA_NICKNAME",nickname)
                        .putString("DATA_USER_NAME",useraccount)
                        .putString("DATA_PASSWORD",userpass)
                        .putBoolean("login_state",true)
                        .apply()
                    gototFragment(SignOkFragment())
                }else{
                    AlertDialog.Builder(requireContext())
                        .setTitle("password sign up error")
                        .setMessage("Please enter 8~12 letters or numbers")
                        .setPositiveButton("ok",null)
                        .show()
                }
            }else{
                AlertDialog.Builder(requireContext())
                    .setTitle("account sign up error")
                    .setMessage("Please enter 4~20 letters or numbers")
                    .setPositiveButton("ok",null)
                    .show()

            }
        }
    }

    private fun pickFromGallery(){
        selectPictureFromGallery.launch("image/*")

    }
    private fun gototFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
            .disallowAddToBackStack()
            .commit()
    }


}