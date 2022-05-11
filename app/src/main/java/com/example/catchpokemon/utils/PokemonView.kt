package com.example.catchpokemon.utils

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.catchpokemon.R
import com.example.catchpokemon.model.Pokemon

class PokemonView(itemView: View): RecyclerView.ViewHolder(itemView) {
    lateinit var pokemon:Pokemon
    var listener : OnShowPokemon? = null
        set(value) {
            field = value
            imageRow.setOnClickListener{
                listener?.show(pokemon)
            }
        }
    var nameRow : TextView = itemView.findViewById(R.id.nameRow)
    var dateRow : TextView = itemView.findViewById(R.id.dateRow)
    var imageRow : ImageButton = itemView.findViewById(R.id.imageRow)

    interface OnShowPokemon{
        fun show(pokemon:Pokemon)
    }

}