package com.example.week02.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.week02.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart,container,false)
        val button1 = view.findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
                .selectedItemId = R.id.Fragment_puchase
        }
        return view
    }
}