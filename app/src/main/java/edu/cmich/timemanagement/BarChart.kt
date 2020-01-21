package edu.cmich.timemanagement

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.AnkoLogger
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import org.jetbrains.anko.info
import com.google.firebase.database.*

import com.google.firebase.auth.FirebaseAuth
import java.util.*

class BarChart : AppCompatActivity(), AnkoLogger {

    var categoryList = categories.getCategoryList()
    var listSize = categoryList.size

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var user = auth.currentUser
    var reference = database.child("users").child(user!!.uid)
    var userId = user!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart)

        var barChart = findViewById<BarChart>(R.id.barGraph)

        var barEntries: ArrayList<BarEntry> = ArrayList<BarEntry>(listSize)
        var list: List<CategoryModel> = categoryList.sortedWith(compareBy({it.times}))
        if (listSize > 5) {
            for (i in listSize - 1 downTo (listSize - 5)) {
                barEntries.add(
                    BarEntry(
                        list.get(i).cId.toFloat(),
                        (list.get(i).times / 1000).toFloat()

                )
                )
            }
        } else {
            for (i in 0..listSize - 1) {
                barEntries.add(
                    BarEntry(
                        list.get(i).cId.toFloat(),
                        (list.get(i).times/1000).toFloat()
                    )
                )
            }
        }
        var barDataSet = BarDataSet(barEntries,"Times")
        barDataSet.setColor(Color.RED)
        var barData:BarData = BarData(barDataSet)
        barChart.data = barData
        barChart.description.isEnabled = false

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.option_bar, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.optionList-> startActivity(Intent(this@BarChart,Category::class.java))
            R.id.displayPieChart -> startActivity(Intent(this@BarChart,PieChart::class.java))
            R.id.optionHome -> startActivity(Intent(this@BarChart,Home::class.java))
            R.id.optionPieChart -> startActivity(Intent(this@BarChart,PieChart::class.java))
            R.id.optionSignOut -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }




}