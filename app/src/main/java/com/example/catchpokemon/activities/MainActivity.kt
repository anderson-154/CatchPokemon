package com.example.catchpokemon.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.catchpokemon.databinding.ActivityMainBinding
import com.example.catchpokemon.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener(::logUser)
    }

    private fun logUser(view:View){
        val user = User(binding.userName.text.toString(),UUID.randomUUID().toString())
        Firebase.firestore.collection("users").whereEqualTo("username",user.userName).get().addOnCompleteListener { item ->
            if(item.result.size() == 0){
                Firebase.firestore.collection("users").document(user.userName).set(user)
                val intent = Intent(this@MainActivity, Pokedex::class.java).apply {
                    putExtra("user",user)
                }
                startActivity(intent)
                finish()
            }else{
                lateinit var loggedIn : User
                for (document in item.result){
                    loggedIn = document.toObject(User::class.java)
                    break
                }
                val intent = Intent(this@MainActivity, Pokedex::class.java).apply {
                    putExtra("user",loggedIn)
                }
                startActivity(intent)
            }
        }
    }


}