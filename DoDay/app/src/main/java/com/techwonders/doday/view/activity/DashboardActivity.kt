package com.techwonders.doday.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.techwonders.doday.R
import com.techwonders.doday.databinding.ActivityDashboardBinding

class DashboardActivity : BaseActivity() {

    lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        init()
    }

    private fun init() {

        binding.cvToDo.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, DoDayActivity::class.java))
            finishAffinity()
        }

        binding.cvFoodDelivery.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, FoodExpenseActivity::class.java))
            finishAffinity()
        }

        binding.cvSettings.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, SettingsActivity::class.java))
        }

        binding.cvComingSoon.setOnClickListener {
            Toast.makeText(this, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }

    }

}
