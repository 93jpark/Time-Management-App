package edu.cmich.timemanagement


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Chronometer
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_add.view.*
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.list_layout.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.nio.file.Files.createFile
import java.nio.file.Path
import java.nio.file.Paths

class Add : AppCompatActivity(), AnkoLogger{

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var user = auth.currentUser
    var reference = database.child("users").child(user!!.uid)
    var userId = user!!.uid


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        addCat.setOnClickListener(){
            if(addText.text.isNullOrEmpty()){
                Toast.makeText(applicationContext,"Textfield is empty.", Toast.LENGTH_LONG).show()
            } else {
                categories.categoryList.add(CategoryModel(categories.categoryList.size+1, addText.text.toString(), 0))
                reference.child("categoryList").removeValue()
                database.child("users").child(userId).child("categoryList").setValue(categories.getCategoryList())
                //ProcessingJSON().createJsonData(this)
                Toast.makeText(applicationContext,addText.text.toString()+" is added", Toast.LENGTH_LONG).show()
                val intent = Intent(this, Category::class.java)
                startActivity(intent)
            }


        }

    }

}