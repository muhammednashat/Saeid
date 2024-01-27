package com.mnashat_dev.saeid.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.mnashat_dev.saeid.R
import com.mnashat_dev.saeid.auth.AuthActivity

//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        Handler().postDelayed({
            determineTheActivityTONavigate()
        }, 2000)

    }

    private fun determineTheActivityTONavigate() {
//        val email = getEmail()
//        if (email != null){
//            Log.e("TAG" , "2 ")
//            determineTheUserTONavigate(email)
//        }
//        else{
//            Log.e("TAG" , "3 ")
//            startActivity(Intent(this, AuthActivity::class.java))
//            finish()
//        }

        startActivity(Intent(this, AuthActivity::class.java))
        finish()

    }

//   private fun getEmail():String? {
//      val user = FirebaseAuth.getInstance().currentUser
//       return user?.email
//
//   }


//    private fun getTypeUser(email: String?, callback: (String?) -> Unit) {
//        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
//
//        val query = databaseReference.orderByChild("email").equalTo(email)
//
//        query.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (userSnapshot in dataSnapshot.children) {
//                        val userType = userSnapshot.child("type").getValue(String::class.java)
//                        callback(userType)
//                        return
//                    }
//                } else {
//                    // No user found with the given email
//                    callback(null)
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle the error
//                callback(null)
//            }
//        })
//    }
//
//
//    private fun determineTheUserTONavigate(email: String?){
//
//        getTypeUser(email) { userType ->
//
//            if (userType != null) {
//                when (userType) {
//                    PatientType -> {
//                        startActivity(Intent(this, PatientScreenActivity::class.java))
//
//                    }
//                    CaregiverType -> {
//                        startActivity(Intent(this, CaregiverScreenActivity::class.java))
//
//                    }
//                    else -> {
//                        startActivity(Intent(this, AdminScreenActivity::class.java))
//                    }
//                }
//                finish()
//
//
//            } else {
//
//                // user does not exist or there was an error
//            }
//        }
//
//    }



}
