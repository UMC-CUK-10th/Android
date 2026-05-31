package com.example.nike

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nike.databinding.ActivityMainBinding // 1. 바인딩 클래스 임포트

class MainActivity : AppCompatActivity() {

    // 2. 바인딩 객체 선언 (나중에 초기화할 거라 lateinit 사용)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 3. 바인딩 초기화 및 레이아웃 설정
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 4. findViewById 대신 binding.ID(카멜케이스)로 접근
        // XML의 id가 bottom_nav라면 binding.bottomNav로 자동 변환됩니다.
        val bottomNav = binding.bottomNav

        // 앱 첫 화면 설정
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, HomeFragment())
                .commit()
        }

        // 하단 바 클릭 시 Fragment 전환하기
        bottomNav.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.menu_home -> HomeFragment()
                R.id.menu_buy -> BuyFragment()
                R.id.menu_wish -> WishFragment()
                R.id.menu_bag -> BagFragment()
                R.id.menu_profile -> ProfileFragment()
                else -> null
            }

            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, it)
                    .commit()
                true
            } ?: false
        }
    }
}