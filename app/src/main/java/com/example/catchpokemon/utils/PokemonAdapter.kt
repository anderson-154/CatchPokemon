package com.example.catchpokemon.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catchpokemon.R
import com.example.catchpokemon.activities.PokemonActivityDrop
import com.example.catchpokemon.model.Pokemon
import com.example.catchpokemon.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PokemonAdapter: RecyclerView.Adapter<PokemonView>(), PokemonActivityDrop.OnPokemonDrop {

    private val pokedex = ArrayList<Pokemon>()
    lateinit var user:User

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonView {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.view_pokemon, parent, false)
        val pokemonView = PokemonView(item)
        return pokemonView
    }

    override fun onBindViewHolder(holder: PokemonView, position: Int) {
        val pokemon = pokedex[position]
        holder.pokemon = pokemon
        holder.user = user
        Picasso.get().load(pokemon.img).into(holder.imageRow)
        holder.nameRow.text = pokemon.name
        holder.dateRow.text = SimpleDateFormat("MMM dd, yy ").format(Date(pokemon.date))
    }

    override fun getItemCount(): Int {
        return pokedex.size
    }

    fun addPokemon(pokemon:Pokemon){
        pokedex.add(pokemon)

    }

    fun deletePokemon() {
        pokedex.clear()
    }

    fun removePokemon(pokemon: Pokemon){
        pokedex.remove(pokemon)
    }

    override fun dropPokemon(task: Task<QuerySnapshot>) {
        for(doc in task.result!!){
            val pk = doc.toObject(Pokemon::class.java)
            removePokemon(pk)
        }
    }
}