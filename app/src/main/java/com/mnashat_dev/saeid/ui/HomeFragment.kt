package com.mnashat_dev.saeid.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.mnashat_dev.saeid.auth.AuthActivity
import com.mnashat_dev.saeid.databinding.FragmentHomeBinding
import com.mnashat_dev.saeid.util.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var  sharedPreferencesManager : SharedPreferencesManager
    private val firebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        sharedPreferencesManager = SharedPreferencesManager(requireContext())
        askNotificationPermission()
        setUpClickingListener()
        return binding.root
    }
    private suspend fun sendNotification(token: String , body: String) {
        val data = mapOf(
            "click_action" to "FLUTTER_NOTIFICATION_CLICK",
            "id" to "1",
            "status" to "done",
            "message" to "message"
        )

        val json = """
        {
            "notification": {
                "title": "Saeid|ساعد",
                "body": "$body"
            },
            "priority": "high",
            "data": ${toJsonString(data)},
            "to": "$token"
        }
    """.trimIndent()

        try {
            val response = withContext(Dispatchers.IO) {
                val client = OkHttpClient()
                val request = okhttp3.Request.Builder()
                    .url("https://fcm.googleapis.com/fcm/send")
                    .post(json.toRequestBody("application/json".toMediaTypeOrNull()))
                    .header("Content-Type", "application/json")
                    .header(
                        "Authorization",
                        "key=AAAAUF_OY3s:APA91bHGqtXPIp-q5p0S8IXw5lzXyG1EiO1J-vAw4vFlzVEDN_A2EoYAI8NcgtDHElV9s4gNgfAALNRbWghlFK8Wa9u4Bs6HjmfowQNsYqshAyA_uw6DJti6EM3EHtejvT1k9jHFquWt"
                    )
                    .build()

                client.newCall(request).execute()
            }

            if (response.isSuccessful) {
                Toast.makeText(requireContext(),"تم إرسال إشعار إلى المرافق" ,Toast.LENGTH_SHORT).show()
                Log.e("TAG", "Yeh notification is sent")
            } else {
                Log.e("TAG", "Error")
            }
        } catch (e: IOException) {
            Log.e("TAG", "${e.message}")
        }
    }
    private fun toJsonString(data: Map<String, Any>): String {
        return Gson().toJson(data)
    }


    private fun logout() {
        firebaseAuth.signOut()
        startActivity(
            Intent(
                requireActivity(),
                AuthActivity::class.java
            )
        )
        activity?.finish()
    }
    private fun setUpClickingListener() {
        binding.logout.setOnClickListener{
            logout()
        }
        binding.food.setOnClickListener{
            lifecycleScope.launch {
                sendNotification(sharedPreferencesManager.getString("token") , "اريد الطعام")
            }
        }
        binding.water.setOnClickListener{
            lifecycleScope.launch {
                sendNotification(sharedPreferencesManager.getString("token") , "احتاج للماء")
            }
        }
        binding.doctor.setOnClickListener{
            lifecycleScope.launch {
                sendNotification(sharedPreferencesManager.getString("token") , "اشعر بالمرض")
            }
        }
        binding.getUp.setOnClickListener{
            lifecycleScope.launch {
                sendNotification(sharedPreferencesManager.getString("token") , "احتاج للنهوض")
            }
        }
        binding.toilet.setOnClickListener{
            lifecycleScope.launch {
                sendNotification(sharedPreferencesManager.getString("token") , "اريد الذهاب الى دورة المياه")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun askNotificationPermission() {
        Log.e("TAG" , "askNotificationPermission")
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.e("TAG", "1")
        } else {
            Log.e("TAG", "11")
        }
    }
}