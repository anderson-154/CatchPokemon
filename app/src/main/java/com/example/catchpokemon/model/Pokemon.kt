package com.example.catchpokemon.model

import android.os.Build
import java.io.Serializable
import java.util.*

data class Pokemon(
    val name:String="",
    val type:String="",
    val img:String="",
    val hp:String="",
    val attack:String="",
    val defense:String="",
    val speed:String="",
    val trainer:String="",
    val id : String="",
    val date: Long=0 ) : Serializable