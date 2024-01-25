//package com.mnashat_dev.saeid.util
//
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//
//
//object FirebaseService {
//
//
//
//     fun getUserEmail():String? {
//        val user = FirebaseAuth.getInstance().currentUser
//        return user?.email
//    }
//
//     fun getTypeUser(email: String?, callback: (String?) -> Unit) {
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
//                    callback(null)
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                callback(null)
//            }
//        })
//    }
//
//
//}