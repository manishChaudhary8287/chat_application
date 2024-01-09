package com.manish.meetup.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.manish.meetup.R
import com.manish.meetup.model.User

class SignUpActivity : AppCompatActivity() {

    lateinit var etName:EditText
    lateinit var etEmail:EditText
    lateinit var etPass:EditText
    lateinit var etCPass:EditText
    lateinit var etPhone:EditText
    lateinit var interest:Spinner
    lateinit var btnSignup:Button
    lateinit var txtLog:TextView
    lateinit var sp: SharedPreferences

    lateinit var mauth:FirebaseAuth
    lateinit var mDBRef:DatabaseReference

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etName=findViewById(R.id.etName)
        etEmail=findViewById(R.id.etEmail)
        etPass=findViewById(R.id.etPassword)
        etCPass=findViewById(R.id.etConfirmPassword)
        etPhone=findViewById(R.id.etNumber)
        interest=findViewById(R.id.interest)
        btnSignup=findViewById(R.id.btnSignup2)
        txtLog=findViewById(R.id.txtlog)


        mauth=Firebase.auth
        sp=getSharedPreferences("Exposys", Context.MODE_PRIVATE)
        val isLoggedIn =sp.getBoolean("isLoggedIn",false)
        if(isLoggedIn){
            val intent= Intent(this@SignUpActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val list=resources.getStringArray(R.array.interest_list)
        var selectedInterest:String="null"

        if(interest!=null){
            val adapter = ArrayAdapter(this,R.layout.my_spinner_layout,list)
            interest.adapter=adapter

            interest.onItemSelectedListener= object :
            AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                    selectedInterest=parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Toast.makeText(this@SignUpActivity,"Nothing Selected",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnSignup.setOnClickListener {
            val fieldChecked=isAllFieldChecked()
            val checkPass=checkPass()

            val email=etEmail.text.toString()
            val password=etPass.text.toString()
            val name=etName.text.toString()


            if(fieldChecked && checkPass){
                mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this) {
                    task->
                    if(task.isSuccessful){
                        Toast.makeText(this@SignUpActivity,"Sign Up Successfully",
                            Toast.LENGTH_SHORT).show()

                        addUserToDatabase(name,email,mauth.currentUser?.uid!!,selectedInterest)

                        val intent=Intent(this@SignUpActivity, MainActivity::class.java)
                        sp.edit().putBoolean("isLoggedIn",true).apply()
                        finish()
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@SignUpActivity,"Sign Up Failed",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        txtLog.setOnClickListener{
            val intent=Intent(this@SignUpActivity, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }

    }

    private fun isAllFieldChecked():Boolean{
        if(etName.length()==0 || etEmail.length()==0|| etPass.length()==0 || etCPass.length()==0 || etPhone.length()==0)
        {
            if(etName.length()==0)
            {
                etName.error="Name is required"
            }
            if(etEmail.length()==0)
            {
                etEmail.error="Email is required"
            }
            if(etPass.length()==0)
            {
                etPass.error="Password is must"
            }
            if(etCPass.length()==0){
                etCPass.error="Enter Password to Confirm"
            }
            if(etPhone.length()==0){
                etPhone.error="Enter your Phone number"
            }

            Toast.makeText(this@SignUpActivity,"Enter your details",Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            return true
        }
    }

   private fun checkPass():Boolean{
        val cpass1=etCPass.text.toString()
        val pass1=etPass.text.toString()
        if(cpass1==pass1)
        {
            return true
        }
        else
        {
            etCPass.error="Confirm password and password not matched"
            return false
        }
    }

    private fun addUserToDatabase(name:String,email:String,uid:String,selection:String)
    {
        mDBRef=FirebaseDatabase.getInstance().getReference()
        if(selection=="Cricket")
        {
            mDBRef.child("Cricket").child("user").child(uid).setValue(User(name,email,uid))
        }
        if(selection=="Football"){
            mDBRef.child("Football").child("user").child(uid).setValue(User(name,email,uid))
        }

    }
}