package com.example.emotion

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val yellow = findViewById<ImageView>(R.id.yellow)
        val blue = findViewById<ImageView>(R.id.blue)
        val purple = findViewById<ImageView>(R.id.purple)
        val green = findViewById<ImageView>(R.id.green)
        val red = findViewById<ImageView>(R.id.red)

        val txtYellow = findViewById<android.widget.TextView>(R.id.txt_yellow)
        val txtBlue = findViewById<android.widget.TextView>(R.id.txt_blue)
        val txtPurple = findViewById<android.widget.TextView>(R.id.txt_purple)
        val txtGreen = findViewById<android.widget.TextView>(R.id.txt_green)
        val txtRed = findViewById<android.widget.TextView>(R.id.txt_red)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        yellow.setOnClickListener {
            txtYellow.setTextColor(android.graphics.Color.parseColor("#FFEFB6"))
        }

        blue.setOnClickListener {
            txtBlue.setTextColor(android.graphics.Color.parseColor("#CEE7F5"))
        }

        purple.setOnClickListener {
            txtPurple.setTextColor(android.graphics.Color.parseColor("#BEC3ED"))
        }

        green.setOnClickListener {
            txtGreen.setTextColor(android.graphics.Color.parseColor("#B1D3B9"))
        }

        red.setOnClickListener {
            txtRed.setTextColor(android.graphics.Color.parseColor("#EB8B8B"))
        }

    }
}