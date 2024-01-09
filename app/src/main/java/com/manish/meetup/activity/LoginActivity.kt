package com.manish.meetup.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.manish.meetup.R

class LoginActivity : AppCompatActivity() {

    lateinit var etEmail:EditText
    lateinit var etPass:EditText
    lateinit var btnLogin:Button
    lateinit var btnSignUp:Button
    lateinit var sp: SharedPreferences

    lateinit var mauth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail=findViewById(R.id.Email1)
        etPass=findViewById(R.id.Pass)
        btnLogin=findViewById(R.id.btnLogin)
        btnSignUp=findViewById(R.id.btnSignUp)



        mauth= FirebaseAuth.getInstance()
        sp=getSharedPreferences("Exposys", Context.MODE_PRIVATE)
        val isLoggedIn =sp.getBoolean("isLoggedIn",false)
        if(isLoggedIn){
            val intent= Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            val allFieldCheked=isAllFieldChecked()
            val email=etEmail.text.toString()
            val password=etPass.text.toString()

            if(allFieldCheked){
                mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if(it.isSuccessful){
                        val intent=Intent(this@LoginActivity, MainActivity::class.java)
                        finish()
                        sp.edit().putBoolean("isLoggedIn",true).apply()
                        startActivity(intent)
                        Toast.makeText(this@LoginActivity,"Login Successfully",
                            Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this@LoginActivity,"Login Failed",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnSignUp.setOnClickListener {
            val intent=Intent(this@LoginActivity, SignUpActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun isAllFieldChecked():Boolean{
        if(etEmail.length()==0 || etPass.length()==0){

            if(etEmail.length()==0){
                etEmail.error="Enter Email"
            }
            if(etPass.length()==0)
            {
                etPass.error="Enter Password"
            }
            Toast.makeText(this@LoginActivity,"Enter details",Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            return true
        }
    }
}