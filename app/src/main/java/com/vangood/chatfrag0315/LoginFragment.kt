package com.vangood.chatfrag0315

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.vangood.chatfrag0315.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    lateinit var binding:FragmentLoginBinding
    var remember =false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireContext().getSharedPreferences("chat",Context.MODE_PRIVATE)
        val checked = pref.getBoolean("remember_me",false)

        binding.checkBox.isChecked = checked
        binding.checkBox.setOnCheckedChangeListener { compoundButton, checked ->
            remember =checked
            pref.edit()
                .putBoolean("remember_me",remember)
                .apply()
            if (!checked){
                pref.edit()
                    .putString("USER_NAME","")
                    .putString("PASSWORD","")
                    .apply()
            }
        }
        val prefUser = pref.getString("USER_NAME","")
        val prefPass = pref.getString("PASSWORD","")
        if (prefUser != ""){
            binding.edName.setText(prefUser)
            binding.edPassword.setText(prefPass)
        }
        binding.bLogin.setOnClickListener {
            val username = binding.edName.text.toString()
            val password = binding.edPassword.text.toString()

            if (username == "jack" && password == "1234"){
                if (remember){
                    pref.edit()
                        .putString("USER_NAME",username)
                        .putString("PASSWORD",password)
                        .apply()
                }
                val intent=Intent(requireContext(),MainActivity::class.java)
                startActivity(intent)
                //go to home
            }else{
                AlertDialog.Builder(requireContext())
                    .setTitle("Login Fail")
                    .setMessage("would you like to try again?")
                    .setPositiveButton("OK"){d,w ->
                        binding.edName.setText("")
                        binding.edPassword.setText("")
                    }
                    .show()
            }
        }
        binding.bSignup.setOnClickListener {
            //go to sign up

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container,SignUpFragment())
                .commit()

        }





    }
}