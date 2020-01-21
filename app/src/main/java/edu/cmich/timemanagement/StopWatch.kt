package edu.cmich.timemanagement

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.MenuItemHoverListener
import kotlinx.android.synthetic.main.activity_stopwatch.*
import org.jetbrains.anko.AnkoLogger
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.info

class StopWatch : AppCompatActivity(), AnkoLogger {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var user = auth.currentUser
    var reference = database.child("users").child(user!!.uid)
    var userId = user!!.uid

    var handler: Handler? = null
    var dispHour: TextView? = null
    var dispMin: TextView? = null
    var dispSec: TextView? = null
    var dispStatus: TextView? = null

    internal var MillisecondTime: Long = 0
    internal var StartTime: Long = 0
    internal var TimeBuff: Long = 0
    internal var UpdateTime = 0L

    internal var Seconds: Int = 0
    internal var Minutes: Int = 0
    internal var Hours: Int = 0
    internal var Milliseconds: Int = 0

    internal var flag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stopwatch)
        bindViews()
        var cName:String = intent.getStringExtra("cName")
        val index:Int = intent.getStringExtra("index")!!.toInt()
        info("test1"+categories.getCategoryList())
        txtCname.text = cName

        btnSavetime.setOnClickListener(){
            flag = false
            info("test2"+categories.getCategoryList())
            info(UpdateTime)
            categories.getCategoryList().get(index).times += UpdateTime
            reference.child("categoryList").removeValue()
            database.child("users").child(userId).child("categoryList").setValue(categories.getCategoryList())

            var intent = Intent(this, Home::class.java)
            startActivity(intent)

            info(SystemClock.uptimeMillis())
        }

    }

    private fun bindViews() {
        dispHour = findViewById(R.id.dispHour)
        dispMin = findViewById(R.id.dispMin)
        dispSec = findViewById(R.id.dispSec)

        btnStart.setOnClickListener{
            if(flag){
                btnStart.text = "START"
                handler?.removeCallbacks(runnable)
                flag = false
            } else if (!flag){
                btnStart.text = "STOP"
                StartTime = SystemClock.uptimeMillis()
                handler?.postDelayed(runnable, 0)
                flag = true
            }
            else {
                flag = false
            }
        }
        handler = Handler()
    }

    var runnable: Runnable = object : Runnable {

        override fun run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime
            UpdateTime = TimeBuff + MillisecondTime
            Seconds = (UpdateTime / 1000).toInt()
            Minutes = Seconds / 60
            Hours = Minutes / 60
            Minutes = Minutes % 60
            Seconds = Seconds % 60


            dispStatus?.text = UpdateTime.toString()
            Milliseconds = (UpdateTime % 1000).toInt()

            if (Minutes.toString().length < 2) {
                dispMin?.text = "0" + Minutes.toString()
            } else {
                dispMin?.text = Minutes.toString()
            }

            if (Seconds.toString().length > 2){
                dispSec?.text = "0" + Seconds.toString()
            } else {
                if (Seconds < 10){
                    dispSec?.text = "0" + Seconds.toString()
                }
                else {
                    dispSec?.text = Seconds.toString()
                }
            }
            handler?.postDelayed(this,0)
        }

    }

    fun timeFormat(mTime:Long):String{

        var str:String = ""

        var Seconds = (mTime / 1000).toInt()
        var Minutes = (Seconds / 60)
        var Hours = Minutes / 60

        Minutes = Minutes % 60
        Seconds = Seconds % 60

        str = "$Hours:$Minutes:$Seconds"

        return str
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.displayPieChart -> startActivity(Intent(this@StopWatch,PieChart::class.java))
            R.id.optionHome -> startActivity(Intent(this@StopWatch,Home::class.java))
            R.id.optionList -> startActivity(Intent(this@StopWatch,Category::class.java))
            R.id.optionPieChart -> startActivity(Intent(this@StopWatch,PieChart::class.java))
            R.id.optionBarChart -> startActivity(Intent(this@StopWatch,BarChart::class.java))
            R.id.optionSignOut -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }


}