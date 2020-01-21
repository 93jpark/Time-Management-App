package edu.cmich.timemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.Android
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.InputStream
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.nio.file.Files
import java.nio.file.Paths
import com.google.firebase.auth.FirebaseAuth
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Patterns
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.btnSignup
import kotlinx.android.synthetic.main.activity_signup.*


class MainActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            signIn()
        }

        btnSignup.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }

    private fun signIn() {

        if(inputUserId.text.toString().isEmpty()){
            inputUserId.error = "Please enter email"
            Toast.makeText(applicationContext,"Please enter email", Toast.LENGTH_LONG).show()
            inputUserId.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(inputUserId.text.toString()).matches()){
            inputUserId.error = "Please enter valid email"
            Toast.makeText(applicationContext,"Please enter valid email", Toast.LENGTH_LONG).show()
            inputUserId.requestFocus()
            return
        }

        if(inputUserPw.text.toString().isEmpty()){
            inputUserPw.error = "Please enter password"
            Toast.makeText(applicationContext,"Please enter password", Toast.LENGTH_LONG).show()
            inputUserPw.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(inputUserId.text.toString(), inputUserPw.text.toString())
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    var user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }

            }

//        return true
    }

//    public override fun onStart() {
//        super.onStart()
//        //check if user is signed in (non-null) and update UI accordingly
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
//    }

    fun updateUI(currentUser : FirebaseUser?){
        if(currentUser != null) {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            Toast.makeText(applicationContext, "Login", Toast.LENGTH_LONG).show()
        } else {
            inputUserPw.error = "Incorrect password"
        }
    }
}