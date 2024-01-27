package com.mnashat_dev.saeid.base

import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mnashat_dev.saeid.models.SingingModel
import com.mnashat_dev.saeid.util.CARE_PHONE
import com.mnashat_dev.saeid.util.EMAIL
import com.mnashat_dev.saeid.util.IS_LOGIN
import com.mnashat_dev.saeid.util.SharedPreferencesManager
import com.mnashat_dev.saeid.util.USER_NAME
import com.mnashat_dev.saeid.util.USER_PHONE


open class BaseFragment : Fragment() {

    private val database = FirebaseDatabase.getInstance()
    val usersReference = database.getReference("users")
    val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var  sharedPreferencesManager : SharedPreferencesManager

    fun navigateToActivity(currentActivity: Activity, targetActivityClass: Class<out Activity?>?) {
        val intent = Intent(currentActivity, targetActivityClass)
        currentActivity.startActivity(intent)
    }


    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun navigateToFragment(container: Int, fragment: Fragment) {
        val transaction: FragmentTransaction =
            requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(container, fragment)
        transaction.commit()
    }


    fun storeDataLocal(singingModel: SingingModel){
        sharedPreferencesManager = SharedPreferencesManager(requireContext())
        singingModel.name?.let { sharedPreferencesManager.storeString(USER_NAME , it) }
        singingModel.email?.let { sharedPreferencesManager.storeString(EMAIL , it) }
        singingModel.userPhone?.let { sharedPreferencesManager.storeString( USER_PHONE, it) }
        singingModel.carePhone?.let { sharedPreferencesManager.storeString( CARE_PHONE, it) }
        sharedPreferencesManager.storeBoolean(IS_LOGIN,true)
    }

    fun getSpinnerAdapter(arrString: Array<String>): ArrayAdapter<String> {
        return ArrayAdapter(
            requireActivity(),
            R.layout.simple_spinner_item,
            arrString
        )
    }

}