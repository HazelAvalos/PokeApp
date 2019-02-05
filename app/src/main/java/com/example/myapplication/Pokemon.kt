package com.example.myapplication

import java.io.Serializable

data class Pokemon  (var name: String, var urlImage : String = "null", var abilities:Array<Ability> = arrayOf(Ability("none", 1, "none")), var flavorTextUrl: String = "null", var genus : String = "null") : Serializable {

}