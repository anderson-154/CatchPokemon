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
    val trainer:String="") : Serializable{

    val id = UUID.randomUUID().toString()
    val date: Date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Calendar.getInstance().time
    } else TODO("VERSION.SDK_INT < N")

}