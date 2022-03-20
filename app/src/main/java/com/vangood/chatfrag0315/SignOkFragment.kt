package com.vangood.chatfrag0315

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.vangood.chatfrag0315.databinding.FragmentSignOkBinding


class SignOkFragment : Fragment() {
    lateinit var binding:FragmentSignOkBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignOkBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireContext().getSharedPreferences("chat", Context.MODE_PRIVATE)
        val name = pref.getString("DATA_USER_NAME","")
        val nick = pref.getString("DATA_NICKNAME","")

        binding.tvNickSignok.text = nick
        binding.tvNameSignok.text = name
        binding.bOut.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.logout_check))
                .setMessage(getString(R.string.want_to_log_out))
                .setPositiveButton(getString(R.string.signok_ok)){ d, w ->
                    pref.edit().putBoolean("login_state",false)
                        .apply()
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.container, LoginFragment())
                        .disallowAddToBackStack()
                        .commit()
                }
                .setNegativeButton(getString(R.string.no),null)
                .show()

        }
    }
}