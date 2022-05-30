package com.example.catchpokemon.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.catchpokemon.databinding.ActivityPokemonDropBinding
import com.example.catchpokemon.model.Pokemon
import com.example.catchpokemon.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PokemonActivityDrop : AppCompatActivity() {
    private val binding: ActivityPokemonDropBinding by lazy{
        ActivityPokemonDropBinding.inflate(layoutInflater)
    }

    var pokemon: Pokemon? = null
    var user: User? = null
    var listener: OnPokemonDrop? = null
    interface OnPokemonDrop{
        fun dropPokemon(task: Task<QuerySnapshot>)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        pokemon = intent.extras?.get("pokemon") as Pokemon
        user = intent.extras?.get("user") as User


        binding.hpTV.text = pokemon?.hp.toString()
        binding.pType.text = pokemon?.type.toString()
        binding.pName.text = pokemon?.name.toString()
        binding.attackTV.text = pokemon?.attack.toString()
        binding.speedTV.text = pokemon?.speed.toString()
        binding.defenseTV.text = pokemon?.defense.toString()
        Picasso.get().load(pokemon?.img.toString()).into(binding.imagePokemon)
        binding.dropBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                Firebase.firestore.collection("users").document(user?.userName.toString())
                    .collection("pokemons").document(pokemon?.id.toString()).delete()
                Firebase.firestore.collection("users").document(user?.userName.toString())
                    .collection("pokemons").orderBy("date",Query.Direction.DESCENDING).get().addOnCompleteListener { item->
                        listener?.dropPokemon(item)
                    }
                val intent = Intent(this@PokemonActivityDrop, Pokedex::class.java).apply {
                    putExtra("user", user)
                }
                startActivity(intent)
                finish()
            }
        }
    }

}