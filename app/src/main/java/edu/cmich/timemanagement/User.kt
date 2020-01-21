package edu.cmich.timemanagement

data class User(
    var name: String? = "",
    var email: String? = "",
    var categoryList:ArrayList<CategoryModel>
)

