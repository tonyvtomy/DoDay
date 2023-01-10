package com.techwonders.doday.view.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import com.techwonders.doday.databinding.ActivitySettingsBinding
import com.techwonders.doday.utility.BREAKFAST_AMOUNT
import com.techwonders.doday.utility.DINNER_AMOUNT
import com.techwonders.doday.utility.LUNCH_AMOUNT
import com.techwonders.doday.utility.MySharedPrefUtils

class SettingsActivity : BaseActivity() {

    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()
    }

    private fun init() {
        title = "Settings"

        val breakfast = MySharedPrefUtils(this).getInt(BREAKFAST_AMOUNT)
        val lunch = MySharedPrefUtils(this).getInt(LUNCH_AMOUNT)
        val dinner = MySharedPrefUtils(this).getInt(DINNER_AMOUNT)

        if (breakfast > 0)
            binding.etBreakfast.setText("$breakfast")

        if (lunch > 0)
            binding.etLunch.setText("$lunch")

        if (dinner > 0)
            binding.etDinner.setText("$dinner")


        binding.etBreakfast.afterTextChanged {
            it.let {
                MySharedPrefUtils(this).setInt(BREAKFAST_AMOUNT, it.toInt())
            }
        }

        binding.etLunch.afterTextChanged {
            it.let {
                MySharedPrefUtils(this).setInt(LUNCH_AMOUNT, it.toInt())
            }
        }

        binding.etDinner.afterTextChanged {
            it.let {
                MySharedPrefUtils(this).setInt(DINNER_AMOUNT, it.toInt())
            }
        }
    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                if (!TextUtils.isEmpty(editable.toString()))
                    afterTextChanged.invoke(editable.toString())
                else
                    afterTextChanged.invoke("0")
            }
        })
    }

}