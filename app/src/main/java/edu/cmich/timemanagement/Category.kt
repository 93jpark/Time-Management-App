package edu.cmich.timemanagement

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_category.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

//var app = MainApp()
//var categories = app.storage;
//var categoryList: ArrayList<CategoryModel>? = null
var listSize:Int = categories.getCategoryList().size


class Category : AppCompatActivity(), AnkoLogger {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var user = auth.currentUser
    var reference = database.child("users").child(user!!.uid)
    var userId = user!!.uid


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        info("in Category::"+categories.getCategoryList())
        val simpleAdapter = SimpleAdapter(this, categories.getCategoryList())
        val recyclerView = findViewById(R.id.recyclerView) as RecyclerView

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        //ProcessingJSON().readJSON(this)
        recyclerView.adapter = simpleAdapter


        val swipeHandler = object :SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val swipingAdapter = recyclerView.adapter as SimpleAdapter
                swipingAdapter.removeAt(viewHolder.adapterPosition)
                //ProcessingJSON().createJsonData(this@Category)
                reference.child("categoryList").removeValue()
                database.child("users").child(userId).child("categoryList").setValue(categories.getCategoryList())
//                reference.addListenerForSingleValueEvent(object : ValueEventListener{
//                    override fun onDataChange(snapshot: DataSnapshot) {
////                        userName = snapshot.child("name").getValue().toString()
////                        info(userName)
////                        dispUserName.text = "Welcome, $userName!"
//                        snapshot.child("categoryList").children.forEach{
//                            var category = it.getValue(CategoryModel::class.java)
//                            categories.addCategory(category!!)
//                        }
//                        info(categories.getCategoryList())
//                    }
//                    override fun onCancelled(error: DatabaseError) {
//                        info(error!!.message)
//                    }
//                })
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        btnAdd.setOnClickListener {
            val intent = Intent(this, Add::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.option_list, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.displayPieChart -> startActivity(Intent(this@Category,BarChart::class.java))
            R.id.optionHome -> startActivity(Intent(this@Category,Home::class.java))
            R.id.optionPieChart -> startActivity(Intent(this@Category,PieChart::class.java))
            R.id.optionBarChart -> startActivity(Intent(this@Category,BarChart::class.java))
            R.id.optionSignOut -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }


}
