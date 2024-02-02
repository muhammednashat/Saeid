package com.mnashat_dev.saeid.auth.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mnashat_dev.saeid.R
import com.mnashat_dev.saeid.databinding.FragmentSignUpBinding
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.mnashat_dev.saeid.ui.MainActivity
import com.mnashat_dev.saeid.models.SingingModel
import com.mnashat_dev.saeid.models.UserInFirebase
import com.google.firebase.firestore.FirebaseFirestore
import com.mnashat_dev.saeid.util.SharedPreferencesManager

class SignUpFragment : Fragment() {


    private lateinit var binding: FragmentSignUpBinding
    private val database = FirebaseDatabase.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var  sharedPreferencesManager : SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        return binding.root

    }
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnSign.setOnClickListener {
            if (validateFields()) {
                binding.container.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                signUp(SingingModel)
            }
        }

    }

    private fun validateFields(): Boolean {
        return validateName() && validateEmail() && validatePassword()
    }

    private fun validateName(): Boolean {
        val name = binding.editTextName.text.toString().trim()

        if (name.isEmpty()) {
            binding.editTextName.error = "رجاءا ادخل الأسم"
            return false
        }
        SingingModel.name = name
        UserInFirebase.name = name
        binding.editTextName.error = null
        return true
    }

    private fun validateEmail(): Boolean {
        val email = binding.editEmail.text.toString().trim()
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editEmail.error = "رجاءا ادخل بريد إلكترونى صحيح"
            return false
        }
        SingingModel.email = email
        UserInFirebase.email = email
        binding.editEmail.error = null
        return true
    }

    private fun validatePassword(): Boolean {
        val password = binding.passwordEditText.text.toString().trim()

        if (password.length < 6) {
            binding.passwordEditText.error = "يجب ان لا كلمة المرور عن 6 ارقام "
            return false
        }
        SingingModel.password = password
        binding.passwordEditText.error = null
        return true
    }
    private fun signUp(signingModel: SingingModel) {
        firebaseAuth
            .createUserWithEmailAndPassword(signingModel.email!!, signingModel.password!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    val userId = user?.uid
                    if (userId != null) {
                        getDeviceToken(userId)
                    }

                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.container.visibility = View.VISIBLE
                    task.exception?.message?.let { context?.let { it1 -> showToast(it1, it) } }
                    Log.e("TAG", "task.exception  ${task.exception?.message.toString()}")

                }
            }
    }


    private fun saveUserAdditionalInfoToFirestore(userId: String) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("users")
        val userDocument = usersCollection.document(userId)
        val userData = hashMapOf(
            "name" to UserInFirebase.name,
            "email" to UserInFirebase.email,
            "deviceToken" to UserInFirebase.deviceToken
        )
        userDocument.set(userData)
            .addOnSuccessListener {
                showToast(requireContext(), "مرحبا")
                startActivity(
                    Intent(
                        requireActivity(),
                        MainActivity::class.java
                    )
                )
                Log.d("TAG", "User data stored in Firestore successfully!")
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error writing document to Firestore", e)
                binding.progressBar.visibility = View.GONE
                binding.container.visibility = View.VISIBLE
            }
    }

    private fun getDeviceToken(userId: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                UserInFirebase.deviceToken = task.result
                saveUserAdditionalInfoToFirestore(userId)
                Log.e("TAG", "Device Tok: ${task.result}")
            } else {
                binding.progressBar.visibility = View.GONE
                binding.container.visibility = View.VISIBLE
                task.exception?.message?.let { context?.let { it1 -> showToast(it1, it) } }
                Log.e("TAG", "Error getting device token: ${task.exception}")
            }
        }
    }

}