package com.example.week01

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.howfeel)

        val btnBack = findViewById<ImageView>(R.id.btnBack)

        val btnHappy = findViewById<LinearLayout>(R.id.btnHappy)
        val btnExcited = findViewById<LinearLayout>(R.id.btnExcited)
        val btnNormal = findViewById<LinearLayout>(R.id.btnNormal)
        val btnAnxious = findViewById<LinearLayout>(R.id.btnAnxious)
        val btnAngry = findViewById<LinearLayout>(R.id.btnAngry)

        val txtHappy = findViewById<TextView>(R.id.txtHappy)
        val txtExcited = findViewById<TextView>(R.id.txtExcited)
        val txtNormal = findViewById<TextView>(R.id.txtNormal)
        val txtAnxious = findViewById<TextView>(R.id.txtAnxious)
        val txtAngry = findViewById<TextView>(R.id.txtAngry)

        btnBack.setOnClickListener {
            finish()
        }

        fun resetTextColors() {
            txtHappy.setTextColor(Color.parseColor("#111111"))
            txtExcited.setTextColor(Color.parseColor("#111111"))
            txtNormal.setTextColor(Color.parseColor("#111111"))
            txtAnxious.setTextColor(Color.parseColor("#111111"))
            txtAngry.setTextColor(Color.parseColor("#111111"))
        }

        btnHappy.setOnClickListener {
            resetTextColors()
            txtHappy.setTextColor(Color.parseColor("#D6A300"))
        }

        btnExcited.setOnClickListener {
            resetTextColors()
            txtExcited.setTextColor(Color.parseColor("#4AA3DF"))
        }

        btnNormal.setOnClickListener {
            resetTextColors()
            txtNormal.setTextColor(Color.parseColor("#7D79D8"))
        }

        btnAnxious.setOnClickListener {
            resetTextColors()
            txtAnxious.setTextColor(Color.parseColor("#5FA36A"))
        }

        btnAngry.setOnClickListener {
            resetTextColors()
            txtAngry.setTextColor(Color.parseColor("#D85C5C"))
        }
    }
}