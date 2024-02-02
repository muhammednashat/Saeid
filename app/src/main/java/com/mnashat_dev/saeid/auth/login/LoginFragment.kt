package com.mnashat_dev.saeid.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.mnashat_dev.saeid.R
import com.mnashat_dev.saeid.databinding.FragmentLoginBinding
import com.mnashat_dev.saeid.ui.MainActivity
import com.mnashat_dev.saeid.util.SharedPreferencesManager

class LoginFragment : Fragment(){
    private lateinit var binding: FragmentLoginBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var  sharedPreferencesManager : SharedPreferencesManager
    private lateinit var senderEmail:String
    private lateinit var receivedEmail:String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        sharedPreferencesManager = SharedPreferencesManager(requireContext())
        senderEmail= "azzah.gm305@gmail.com"
        receivedEmail= "johara.am@gmail.com"

        binding.sign.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.btnLogin.setOnClickListener {
            if (validateFields()) {
                binding.container.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                login(
                    binding.editEmail.text.toString(),
                    binding.passwordEditText.text.toString(),
                )
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
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    private fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (email == senderEmail ){
                        getReceivedTokenByEmail(receivedEmail)
                    }else{
                        getToken(receivedEmail)
                    }
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.container.visibility = View.VISIBLE
                    task.exception?.message?.let { context?.let { it1 -> showToast(it1, it) } }
                }
            }
    }
    private fun getReceivedTokenByEmail(email: String)
    {
        Log.d("TAG", "getReceivedTokenByEmail $email")

        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("users")
        usersCollection.whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val deviceToken = document.getString("deviceToken")
                    Log.d("TAG", "Device Token for $email: $deviceToken")
                    sharedPreferencesManager.storeString("token",deviceToken!!)
                    showToast(requireContext(), "مرحبا")
                    startActivity(
                        Intent(
                            requireActivity(),
                            MainActivity::class.java
                        )
                    )
                    activity?.finish()


                } else {
                    Log.d("TAG", "No user found with email: $email")
                }
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error getting device token by email: $email", e)
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this
    }

    private fun updateSenderTokenByEmail(email: String, newDeviceToken:String) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("users")
        usersCollection.whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val userId = document.id
                    usersCollection.document(userId)
                        .update("deviceToken", newDeviceToken)
                        .addOnSuccessListener {
                            Log.d("TAG", "Device Token updated for $email: $newDeviceToken")
                            sharedPreferencesManager.storeString("token", newDeviceToken)
                            showToast(requireContext(), "مرحبا")
                            startActivity(
                                Intent(
                                    requireActivity(),
                                    MainActivity::class.java
                                )
                            )
                            activity?.finish()
                        }
                        .addOnFailureListener { e ->
                            Log.e("TAG", "Error updating device token in Firestore: $e")
                        }
                } else {
                    Log.d("TAG", "No user found with email: $email")
                }
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error getting device token by email: $email", e)
            }
    }

    private fun getToken(email: String){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            updateSenderTokenByEmail(email,token)
        })
    }




}