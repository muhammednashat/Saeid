package com.mnashat_dev.saeid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mnashat_dev.saeid.R
import com.mnashat_dev.saeid.databinding.FragmentWhoAreBinding

class WhoAreFragment : Fragment() {
    private var _binding: FragmentWhoAreBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWhoAreBinding.inflate(inflater, container, false)
        return binding.root
    }

}