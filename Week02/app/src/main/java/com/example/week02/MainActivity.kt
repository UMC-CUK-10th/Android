package com.example.week02

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.week02.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fcv, fragment)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)){view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fcv, HomeFragment())
            .commit()

        binding.mainBnv.setOnItemSelectedListener{item ->
            when(item.itemId){
                R.id.Fragment_home -> changeFragment(HomeFragment())
                R.id.Fragment_puchase -> changeFragment(PurchaseFragment())
                R.id.Fragment_wishlist -> changeFragment(WishlistFragment())
                R.id.Fragment_cart -> changeFragment(CartFragment())
                R.id.Fragment_profile -> changeFragment(ProfileFragment())
            }
            true
        }
    }
}