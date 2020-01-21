package edu.cmich.timemanagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btnSignup
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.annotations.NotNull
import java.security.Provider

class Signup : AppCompatActivity(), AnkoLogger {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        btnSignup.setOnClickListener {

            if(signUp()){
                Toast.makeText(applicationContext,"Registered!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun signUp(): Boolean {

        if(inputSignupEmail.text.toString().isEmpty()){
            inputSignupEmail.error = "Please enter email"
            Toast.makeText(applicationContext,"Please enter email", Toast.LENGTH_LONG).show()
            inputSignupEmail.requestFocus()
            return false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(inputSignupEmail.text.toString()).matches()){
            inputSignupEmail.error = "Please enter valid email"
            Toast.makeText(applicationContext,"Please enter valid email", Toast.LENGTH_LONG).show()
            inputSignupEmail.requestFocus()
            return false
        }

        if(inputSignupPw.text.toString().isEmpty()){
            inputSignupPw.error = "Please enter password"
            Toast.makeText(applicationContext,"Please enter password", Toast.LENGTH_LONG).show()
            inputSignupPw.requestFocus()
            return false
        }

        if (inputSignupPw.text.length < 6){
            inputSignupPw.error = "Must be six characters"
            Toast.makeText(applicationContext, "Please enter password", Toast.LENGTH_LONG).show()
            inputSignupPw.requestFocus()
            return false
        }

        if(!inputSignupPw.text.toString().equals(inputSingupPw2.text.toString())){
            inputSignupPw.error = "password not match"
            Toast.makeText(applicationContext,"Invalid password", Toast.LENGTH_LONG).show()
            inputSignupPw.requestFocus()
            return false
        }

        auth.createUserWithEmailAndPassword(inputSignupEmail.text.toString(), inputSignupPw.text.toString())
            .addOnCompleteListener {

                val user = auth.currentUser
                var reference = database.child("users").child(it.result!!.user!!.uid)
                reference.setValue(user)
                    .addOnCompleteListener {
                        if (it.isSuccessful)
                        // Write new user
                        writeNewUser(user!!.uid, inputUserName.text.toString(), user.email)

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
            }

        // Sign in success, update UI with the signed-in user's information
        return true
    }

    private fun writeNewUser(userId: String, name: String, email: String?) {
        var mock = ArrayList<CategoryModel>()

        //val user = User(name, email, ArrayList<CategoryModel>)
        val user = User(name, email, mock)
        database.child("users").child(userId).setValue(user)
    }

}