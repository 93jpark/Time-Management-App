package edu.cmich.timemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import kotlin.reflect.typeOf

var app = MainApp()
var categories = app.storage;
lateinit var categoryList:Array<CategoryModel>

class Home : AppCompatActivity(), AnkoLogger {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        user = auth.currentUser!!
        var reference = database.child("users").child(user!!.uid)
        lateinit var userName:String
        var totalTime:Long = 0

        categories.initList()

        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userName = snapshot.child("name").getValue().toString()

                dispUserName.text = "Welcome, $userName!"
                snapshot.child("categoryList").children.forEach{
                    var category = it.getValue(CategoryModel::class.java)
                    categories.addCategory(category!!)
                    totalTime += category.times
                }
                //info(categories.getCategoryList())

                txtTotalHours.text = timeFormat(totalTime)

            }
            override fun onCancelled(error: DatabaseError) {
                info(error!!.message)
            }
        })

        btnChooseCategory.setOnClickListener {
            val intent = Intent(this, Category::class.java)
            startActivity(intent)
        }

        btnSignOut.setOnClickListener{
            //Toast.makeText(applicationContext,"$name logged out", Toast.LENGTH_LONG).show()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun timeFormat(mTime:Long):String{

        var str:String = ""

        var Seconds = (mTime / 1000).toInt()
        var Minutes = (Seconds / 60)
        var Hours = Minutes / 60

        Minutes = Minutes % 60
        Seconds = Seconds % 60

        if (Seconds < 10 && Minutes < 10 && Hours < 10){
            str = "0$Hours:0$Minutes:0$Seconds"
        }
        else if(Seconds > 10 && Minutes < 10 && Hours < 10){
            str = "0$Hours:0$Minutes:$Seconds"
        }
        else if(Seconds > 10 && Minutes > 10 && Hours < 10){
            str = "0$Hours:$Minutes:$Seconds"
        }
        else if(Seconds > 10 && Minutes > 10 && Hours >10){
            str = "$Hours:$Minutes:$Seconds"
        }
        else if(Seconds < 10 && Minutes > 10 && Hours > 10) {
            str = "$Hours:$Minutes:0$Seconds"
        }
        else if(Seconds < 10 && Minutes < 10 && Hours > 10){
            str = "$Hours:0$Minutes:0$Seconds"
        }
        else if(Seconds < 10 && Minutes > 10 && Hours < 10){
            str = "0$Hours:$Minutes:0$Seconds"
        }
        else if(Seconds > 10 && Minutes < 10 && Hours > 10){
            str = "$Hours:0$Minutes:$Seconds"
        }
        return str
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.option_home, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.displayPieChart -> startActivity(Intent(this@Home,PieChart::class.java))
            R.id.optionList -> startActivity(Intent(this@Home,Category::class.java))
            R.id.optionPieChart -> startActivity(Intent(this@Home,PieChart::class.java))
            R.id.optionBarChart -> startActivity(Intent(this@Home,BarChart::class.java))
            R.id.optionSignOut -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }

}