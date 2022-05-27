package com.example.catchpokemon.utils

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.catchpokemon.R
import com.example.catchpokemon.activities.PokemonActivityDrop
import com.example.catchpokemon.model.Pokemon
import com.example.catchpokemon.model.User

class PokemonView(itemView: View): RecyclerView.ViewHolder(itemView) {
    var pokemon:Pokemon?=null
    var user: User?=null

    var nameRow : TextView = itemView.findViewById(R.id.nameRow)
    var dateRow : TextView = itemView.findViewById(R.id.dateRow)
    var imageRow : ImageView = itemView.findViewById(R.id.imageRow)
    var backRow : Button = itemView.findViewById(R.id.backBtn)

    init {
        backRow.setOnClickListener {
            val intent = Intent(itemView.context, PokemonActivityDrop::class.java).apply {
                putExtra("pokemon", pokemon!!)
                putExtra("user", user!!)
            }
            itemView.context.startActivity(intent)
        }
    }

}