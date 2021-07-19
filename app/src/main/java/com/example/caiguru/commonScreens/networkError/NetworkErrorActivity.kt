package com.example.caiguru.commonScreens.networkError

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.caiguru.R
import kotlinx.android.synthetic.main.activity_network_error.*

class NetworkErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_error)
        btnnext.setOnClickListener {
            finish()
        }
    }
}
