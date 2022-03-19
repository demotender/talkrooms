package com.vangood.chatfrag0315

import androidx.lifecycle.ViewModel

class SignUpViewModel: ViewModel() {

    fun accountcheck (SignId : String) : Boolean{
        return !(SignId.length < 4 || SignId.length > 20)
    }

    fun passwordcheck (SignPw : String) : Boolean{
        return !(SignPw.length < 6 || SignPw.length > 12)
    }
}