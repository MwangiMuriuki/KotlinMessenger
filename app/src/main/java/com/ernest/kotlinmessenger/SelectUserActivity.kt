package com.ernest.kotlinmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_select_user.*

class SelectUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user)

        setSupportActionBar(selectUserToolbar)
        supportActionBar?.apply {
            title = "Select User"
        }
    }
}