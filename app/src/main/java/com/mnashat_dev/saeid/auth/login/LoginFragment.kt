package com.mnashat_dev.saeid.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.mnashat_dev.saeid.R
import com.mnashat_dev.saeid.base.BaseFragment
import com.mnashat_dev.saeid.databinding.FragmentLoginBinding
import com.mnashat_dev.saeid.ui.MainActivity

class LoginFragment : BaseFragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.sign.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.btnLogin.setOnClickListener {

            if (validateFields()){
                binding.container.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                login( binding.editEmail.text.toString(),
                    binding.passwordEditText.text.toString(),)
            }
        }
        return binding.root
    }

    private fun validateFields(): Boolean {
        return validateEmail() && validatePassword()
    }

    private fun validateEmail(): Boolean {
        val name = binding.editEmail.text.toString().trim()

        if (name.isEmpty()) {
            binding.editEmail.error = "رجاءا ادخل البريد اﻹلكترونى"
            return false
        }
        binding.editEmail.error = null
        return true
    }
    private fun validatePassword(): Boolean {
        val name = binding.passwordEditText.text.toString().trim()

        if (name.isEmpty()) {
            binding.passwordEditText.error = "رجاءا ادخل كلمة المرور"
            return false
        }
        binding.passwordEditText.error = null
        return true
    }
        private fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast(requireContext(),"مرحبا")
                    startActivity(
                        Intent(
                            requireActivity(),
                            MainActivity::class.java
                        )
                    )
                    activity?.finish()
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.container.visibility = View.VISIBLE
                    task.exception?.message?.let { context?.let { it1 -> showToast(it1, it) } }
                }
            }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this
    }
}