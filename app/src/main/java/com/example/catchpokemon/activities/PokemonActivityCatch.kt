package com.example.catchpokemon.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.catchpokemon.R
import com.example.catchpokemon.databinding.ActivityPokemonCatchBinding
import com.example.catchpokemon.model.Pokemon
import com.example.catchpokemon.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class PokemonActivityCatch : AppCompatActivity() {
    private val binding: ActivityPokemonCatchBinding by lazy{
        ActivityPokemonCatchBinding.inflate(layoutInflater)
    }

    private lateinit var user: User
    private lateinit var pokemon: Pokemon


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        user = intent.extras?.get("user") as User
        pokemon = intent.extras?.get("pokemon") as Pokemon

        binding.pName.text = pokemon.name
        binding.pType.text = "(${pokemon.type})"
        binding.speedTV.text = pokemon.speed
        binding.hpTV.text = pokemon.hp
        binding.attackTV.text = pokemon.attack
        binding.defenseTV.text = pokemon.defense
        Picasso.get().load(pokemon.img).into(binding.imagePokemon)

        binding.pCatchBtn.setOnClickListener {
            Firebase.firestore.collection("users").document(user.userName).collection("pokemons").document(pokemon.id).set(pokemon)
            val intent = Intent(this, Pokedex::class.java).apply {
                putExtra("user",user)
            }
            startActivity(intent)
            finish()
        }
    }
}