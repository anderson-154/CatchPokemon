package com.example.catchpokemon.utils

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catchpokemon.R
import com.example.catchpokemon.model.Pokemon

class PokemonAdapter: RecyclerView.Adapter<PokemonView>(), PokemonView.OnShowPokemon {

    private val pokedex = ArrayList<Pokemon>()
    private val imgs = ArrayList<Bitmap>()
    lateinit var listener : PokemonView.OnShowPokemon

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonView {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.view_pokemon, parent, false)
        return PokemonView(item)
    }

    override fun onBindViewHolder(holder: PokemonView, position: Int) {
        val pokemon = pokedex[position]
        holder.listener = this
        holder.pokemon = pokemon
        holder.imageRow.setImageBitmap(imgs[position])
        holder.nameRow.text = pokemon.name
        holder.dateRow.text = pokemon.date.toString()
    }

    override fun getItemCount(): Int {
        return pokedex.size
    }

    override fun show(pokemon: Pokemon) {
        listener.show(pokemon)
    }
    fun addPokemon(pokemon:Pokemon, img:Bitmap){
        pokedex.add(pokemon)
        imgs.add(img)
    }

    fun deletePokemon(pokemon: Pokemon) {
        val index = pokedex.indexOf(pokemon)
        pokedex.removeAt(index)
        imgs.removeAt(index)
    }
}