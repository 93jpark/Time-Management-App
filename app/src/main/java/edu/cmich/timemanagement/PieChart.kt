package edu.cmich.timemanagement

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.AnkoLogger
import com.github.mikephil.charting.charts.PieChart;
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.info
import com.google.firebase.database.*

import java.util.*
import kotlin.random.Random

class PieChart : AppCompatActivity(), AnkoLogger {

    var categoryList = categories.getCategoryList()
    var listSize = categoryList.size

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var user = auth.currentUser
    var reference = database.child("users").child(user!!.uid)
    var userId = user!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart)
        var random = Random.nextInt(100)
        info(random)
        var pieChart = findViewById<PieChart>(R.id.pieGraph)
        pieChart.setUsePercentValues(true)
        var pieEntries: ArrayList<PieEntry> = ArrayList<PieEntry>(listSize)
        var list: List<CategoryModel> = categoryList.sortedWith(compareBy({it.times}))

        if (listSize >= 5) {
            for (i in listSize - 1 downTo (listSize - 5)) {
                pieEntries.add(
                    PieEntry(
                        list.get(i).times.toFloat(),
                        list.get(i).name
                    )
                )
            }
        }

        else {
            for (i in 0..listSize - 1) {
                pieEntries.add(
                    PieEntry(
                        list.get(i).times.toFloat(),
                        list.get(i).name
                    )
                )
            }
        }


        var pieDataSet = PieDataSet(pieEntries,"")

        pieDataSet.setColors(Color.RED,Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA)



        var pieData:PieData = PieData(pieDataSet)
        pieData.setValueFormatter(PercentFormatter())

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.description.text = ""
        pieChart.isDrawHoleEnabled = false
        pieChart.isRotationEnabled = false
        pieData.setValueTextSize(13f)




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.option_pie, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.optionList-> startActivity(Intent(this@PieChart,Category::class.java))
            R.id.displayBarChart -> startActivity(Intent(this@PieChart,BarChart::class.java))
            R.id.optionHome -> startActivity(Intent(this@PieChart,Home::class.java))
            R.id.optionBarChart -> startActivity(Intent(this@PieChart,BarChart::class.java))
            R.id.optionSignOut -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }



}