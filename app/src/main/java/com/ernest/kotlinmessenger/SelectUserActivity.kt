package com.ernest.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_select_user.*

class SelectUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user)
        setSupportActionBar(selectUserToolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayUseLogoEnabled(true)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}