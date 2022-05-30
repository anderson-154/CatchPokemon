package com.example.catchpokemon.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.example.catchpokemon.databinding.ActivityPokedexBinding
import com.example.catchpokemon.model.Pokemon
import com.example.catchpokemon.model.User
import com.example.catchpokemon.utils.Constants
import com.example.catchpokemon.utils.PokemonAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.internal.Internal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class Pokedex : AppCompatActivity() {

    private val binding: ActivityPokedexBinding by lazy{
        ActivityPokedexBinding.inflate(layoutInflater)
    }
    private lateinit var user: User
    private lateinit var pokemon: Pokemon
    private var adapter = PokemonAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pokeRecycler = binding.pokemonRecycler
        pokeRecycler.setHasFixedSize(true)
        pokeRecycler.layoutManager = LinearLayoutManager(this)
        pokeRecycler.adapter = adapter
        setContentView(binding.root)

        user  = intent.extras?.get("user") as User
        adapter.user = user
        var pokemon: Pokemon? = null

        Firebase.firestore.collection("users")
            .document(user.userName).collection("pokemons")
            .orderBy("date", Query.Direction.DESCENDING)
            .get().addOnCompleteListener { item ->
                for(i in item.result!!){
                    val pok = i.toObject(Pokemon::class.java)
                    adapter.addPokemon(pok)
                    adapter.notifyDataSetChanged()
                }
            }

        binding.searchBtn.setOnClickListener {
            searchPokemon(binding.pokemonPT.text.toString())
        }

        binding.catchBtn.setOnClickListener {
            if(binding.searchPT.text.isBlank()){
                Toast.makeText(this, "Campo vacio", Toast.LENGTH_SHORT).show()
            }else{
                requestPokemon(binding.searchPT.text.toString(), false)
            }
        }

        binding.watchBtn.setOnClickListener {
            if(binding.searchPT.text.isBlank()){
                Toast.makeText(this, "Campo vacio", Toast.LENGTH_SHORT).show()
            }else{
                requestPokemon(binding.searchPT.text.toString(), true)
            }
        }
    }

    private fun requestPokemon(pokemonName:String, show:Boolean){
        lifecycleScope.launch(Dispatchers.IO){
            val url = URL("${Constants.POKE_API}/pokemon/${pokemonName}")
            val requestClient = url.openConnection() as HttpURLConnection
            requestClient.requestMethod = "GET"
            try{

                val json = requestClient.inputStream.bufferedReader().readText()
                val jsonObject = JSONObject(json)
                val stat = jsonObject.optJSONArray("stats")
                val life = stat?.getJSONObject(0)?.optInt("base_stat")
                val attack = stat?.getJSONObject(1)?.optInt("base_stat")
                val defense = stat?.getJSONObject(2)?.optInt("base_stat")
                val speed = stat?.getJSONObject(5)?.optInt("base_stat")
                val name = jsonObject.optJSONObject("species")?.optString("name")
                val type = jsonObject.optJSONArray("types")?.getJSONObject(0)?.optJSONObject("type")?.optString("name")
                val img = jsonObject.optJSONObject("sprites")?.optString("front_default")

                pokemon = Pokemon(name!!, type!!,img!!,"${life!!}","${attack!!}","${defense!!}","${speed!!}",user.userName,UUID.randomUUID().toString(),Date().time)
                if(show) showPokemon() else catchPokemon()
            }catch (e: FileNotFoundException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Pokedex, "el pokemon no existe", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun searchPokemon(pokemonName: String){
        lifecycleScope.launch(Dispatchers.IO){
            val query = Firebase.firestore.collection("users").document(user.userName).collection("pokemons").whereEqualTo("name",pokemonName)
            query.get().addOnCompleteListener { item->
                if (item.result?.size()!=0){
                    lateinit var pokemonSearch : Pokemon
                    adapter.deletePokemon()
                    for (i in item.result!!){
                        pokemonSearch = i.toObject(Pokemon::class.java)
                        adapter.addPokemon(pokemonSearch)
                        adapter.notifyDataSetChanged()
                        break
                    }
                }else Toast.makeText(this@Pokedex,"Error con algun campo solicitado",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun catchPokemon(){
        lifecycleScope.launch(Dispatchers.IO){
            Firebase.firestore.collection("users").document(user.userName).collection("pokemons")
                .document(pokemon.id).set(pokemon)
            Firebase.firestore.collection("users").document(user.userName).collection("pokemons")
                .orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener { item->
                    adapter.deletePokemon()
                    for (i in item.result!!){
                        val pok = i.toObject(Pokemon::class.java)
                        adapter.addPokemon(pok)
                        adapter.notifyDataSetChanged()
                    }
                }
        }
    }
    private fun showPokemon(){
        val intent = Intent(this@Pokedex, PokemonActivityCatch::class.java).apply {
            putExtra("user", user)
            putExtra("pokemon",pokemon)
        }
        startActivity(intent)
    }
}