package com.example.week01

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.week01.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        binding.icHappy.setOnClickListener{
            binding.tvHappy.setTextColor(Color.parseColor("#FFEFB6"))
        }
        binding.icExcited.setOnClickListener{
            binding.tvExcited.setTextColor(Color.parseColor("#CEE7F5"))
        }
        binding.icSoso.setOnClickListener{
            binding.tvSoso.setTextColor(Color.parseColor("#BEC3ED"))
        }
        binding.icAnxious.setOnClickListener{
            binding.tvAnxious.setTextColor(Color.parseColor("#B1D3B9"))
        }
        binding.icAngry.setOnClickListener{
            binding.tvAngry.setTextColor(Color.parseColor("#EB8B8B"))
        }
    }
}