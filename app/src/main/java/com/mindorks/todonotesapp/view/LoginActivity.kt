package com.mindorks.todonotesapp.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mindorks.todonotesapp.R
import com.mindorks.todonotesapp.utils.AppConstant
import com.mindorks.todonotesapp.utils.PrefConstant
import com.mindorks.todonotesapp.utils.StoreSession

class LoginActivity : AppCompatActivity() {
    lateinit var editTextFullName: EditText
    lateinit var editTextUserName: EditText
    lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        bindViews()
        sharedPreference()

    }

    private fun sharedPreference() {
        StoreSession.init(this)

    }

    private fun bindViews() {
        editTextFullName = findViewById(R.id.editTextFullName)
        editTextUserName = findViewById(R.id.editTextUserName)
        buttonLogin = findViewById(R.id.buttonLogin)
        val clickAction = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val fullName = editTextFullName.text.toString()
                val userName = editTextUserName.text.toString()
                if (fullName.isNotEmpty() && userName.isNotEmpty()) {
                    val intent = Intent(this@LoginActivity, MyNotesActivity::class.java)
                    intent.putExtra(AppConstant.FULL_NAME,fullName)
                    startActivity(intent)
                    saveFullName(fullName)
                    saveLoginState()
                }else{
                    Toast.makeText(this@LoginActivity, "FullName and UserName can't be empty", Toast.LENGTH_SHORT).show()
                }
            }

        }
        buttonLogin.setOnClickListener(clickAction)

    }

    private fun saveLoginState() {
        StoreSession.write(PrefConstant.IS_LOGGED_IN, true)
    }

    private fun saveFullName(fullName: String) {
        StoreSession.write(PrefConstant.FULL_NAME, fullName)
    }
}