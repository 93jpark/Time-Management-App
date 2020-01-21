package edu.cmich.timemanagement

data class CategoryModel(var cId: Int, val name: String, var times: Long){
    constructor() : this(0,"",0)
}


