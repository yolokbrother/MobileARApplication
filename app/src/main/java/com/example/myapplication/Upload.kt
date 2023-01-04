package com.example.myapplication

import com.google.firebase.auth.FirebaseAuth

class Upload {
    var name: String? = null
    var imageUrl: String? = null
    var uid: String? = null

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
}