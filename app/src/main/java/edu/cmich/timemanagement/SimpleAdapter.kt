package edu.cmich.timemanagement

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_layout.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import kotlin.jvm.internal.MagicApiIntrinsics



class SimpleAdapter(context: Context, val categoryList: List<CategoryModel>) : RecyclerView.Adapter<SimpleAdapter.VH>(), AnkoLogger {

    var activity:Context = context


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.findViewById(R.id.textViewName) as TextView
        val textViewTimes = itemView.findViewById(R.id.textViewTimes) as TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val category: CategoryModel = categoryList[position]

        holder.textViewName.text = category.name
        holder.textViewTimes.text = timeFormat(category.times!!)

        holder.itemView.setOnClickListener{

            info("simple adapter is clicked")

            var intent = Intent(this.activity, StopWatch::class.java)
            intent.putExtra("cName",category.name)
            intent.putExtra("cTimes",category.times!!)
            intent.putExtra("index",position.toString())

            ContextCompat.startActivity(this.activity, intent, null)

        }
    }

//    fun addItem(name: String) {
//        //categoryList.add(name)
//        notifyItemInserted(categoryList.size)
//    }

    fun removeAt(position: Int) {
        categories.delCategory(position)
        info("$position was deleted")
        notifyItemRemoved(position)
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


}