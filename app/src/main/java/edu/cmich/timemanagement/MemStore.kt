package edu.cmich.timemanagement

import com.google.gson.Gson
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.File

class MemStore: AnkoLogger {

    var categoryList = ArrayList<CategoryModel>()

    fun getCategoryList(): List<CategoryModel>{
        return categoryList;
    }

    fun addCategory(c:CategoryModel){
        this.categoryList.add(c)
    }

    fun addCategory(cId: Int, name: String, times: Long){
        val newOne = CategoryModel(cId,name,times)
        this.categoryList.add(newOne)
    }

    fun delCategory(cId:Int){
        this.categoryList.removeAt(cId)
        resetId()
    }

    fun resetId(){
        for(i in 0..this.categoryList.size-1){
            this.categoryList.get(i).cId = i;
        }
    }

    fun initList(){
        this.categoryList.clear()
    }

}

