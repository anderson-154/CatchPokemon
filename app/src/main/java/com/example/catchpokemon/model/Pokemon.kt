package com.example.catchpokemon.model

import android.os.Build
import java.io.Serializable
import java.util.*

data class Pokemon(
    val name:String="",
    val type:String="",
    val imgUrl:String="",
    val hp:Int=0,
    val attack:Int=0,
    val defense:Int=0,
    val speed:Int=0,
    val trainer:String="",
    val id : Int=0,
    val date: Date) : Serializable{

}