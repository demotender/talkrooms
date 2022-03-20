package com.vangood.chatfrag0315

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.vangood.chatfrag0315.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val fragments = mutableListOf<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initFragments()
        val pref = getSharedPreferences("chat", Context.MODE_PRIVATE)
        pref.edit().putBoolean("login_state",false)
            .apply()
        if(pref.getBoolean("room_state",false)){
            pref.edit().putBoolean("login_state",true)
                .apply()
        }
        binding.bottomNavBar.setOnItemSelectedListener {
            item ->
            when(item.itemId){
                R.id.action_home ->{
                    supportFragmentManager.beginTransaction().run {
                        replace(R.id.container,fragments[1])
                        commit()
                        true
                }}
                R.id.action_search->{
                    supportFragmentManager.beginTransaction().run {
                        replace(R.id.container,fragments[2])
                        commit()
                        true}
                }
                R.id.action_personal->{
                    supportFragmentManager.beginTransaction().run {
                        replace(R.id.container,fragments[0])
                        commit()
                        true
                    }

                }
                else ->true
            }
        }

    }
    private fun initFragments(){
        fragments.add(0,LoginFragment())
        fragments.add(1,HomePopulerFragment())
        fragments.add(2,SearchFragment())

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container,fragments[1])
            commit()
        }

    }
}