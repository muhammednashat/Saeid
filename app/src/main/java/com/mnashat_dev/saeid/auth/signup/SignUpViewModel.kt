package com.mnashat_dev.saeid.auth.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {


    val name = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val gender = MutableLiveData<String>()
    val ageGroup = MutableLiveData<String>()
    val religion = MutableLiveData<String>()
    val physicalDisability = MutableLiveData<String>()



}