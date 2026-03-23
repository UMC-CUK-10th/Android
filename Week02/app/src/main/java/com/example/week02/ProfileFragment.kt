package com.example.week02

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile,container,false)
        val button2 = view.findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fcv, EditProfileFragment())
                .addToBackStack(null)
                .commit()
        }
        return view
    }
}