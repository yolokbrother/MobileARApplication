package com.example.myapplication

class Upload {

    var name: String? = null
    var imageUrl: String? = null
    var uid: String? = null
    var mKey: String? = null


    constructor() {
        //empty constructor needed
    }

    constructor(name: String, imageUrl: String?,uid:String) {
        var name = name
        if (name.trim { it <= ' ' } == "") {
            name = "No Name"
        }
        this.name = name
        this.imageUrl = imageUrl
        this.uid = uid
    }

    fun setKey(key: String?) {
        mKey = key;
    }
}