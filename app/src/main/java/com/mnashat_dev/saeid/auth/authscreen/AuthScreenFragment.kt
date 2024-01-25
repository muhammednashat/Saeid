package com.mnashat_dev.saeid.auth.authscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mnashat_dev.saeid.R
import com.mnashat_dev.saeid.databinding.FragmentAuthScreenBinding
import com.mnashat_dev.saeid.BaseFragment


class AuthScreenFragment : BaseFragment() {

    private lateinit var binding: FragmentAuthScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

       binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth_screen,container,false);

        binding.tvLogin.setOnClickListener{

        }
        binding.tvSignUp.setOnClickListener{

        }
       return binding.root

    }




}