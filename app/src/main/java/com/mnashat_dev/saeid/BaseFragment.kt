package com.mnashat_dev.saeid

import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


open class BaseFragment : Fragment() {

//    private val database = FirebaseDatabase.getInstance()
//    val usersReference = database.getReference("users")
//    val firebaseAuth = FirebaseAuth.getInstance()


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


    fun getSpinnerAdapter(arrString: Array<String>): ArrayAdapter<String> {
        return ArrayAdapter(
            requireActivity(),
            R.layout.simple_spinner_item,
            arrString
        )
    }

}