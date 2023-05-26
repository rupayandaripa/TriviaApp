package com.example.trivia


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trivia.databinding.GenreBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: GenreBinding
    private lateinit var listIntent: Intent
    lateinit var mytype: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = GenreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1.setOnClickListener { launchArts() }
        binding.button2.setOnClickListener { launchSports() }
        binding.button3.setOnClickListener { launchCelebrities() }
        binding.button4.setOnClickListener { launchEntertainment() }
        binding.button5.setOnClickListener { launchScience() }

    }

    private fun launchArts(){
        listIntent = Intent(this, Arts::class.java)
        startActivity(listIntent)
        mytype = "25"
        finish()
    }

    private fun launchSports(){
        listIntent = Intent(this,Sports::class.java)
        startActivity(listIntent)
        mytype = "21"
        finish()
    }

    private fun launchCelebrities(){
        listIntent = Intent(this,Celebrities::class.java)
        startActivity(listIntent)
        mytype = "26"
        finish()
    }

    private fun launchEntertainment(){
        listIntent = Intent(this,Entertainment::class.java)
        startActivity(listIntent)
        mytype= "11"
        finish()
    }

    private fun launchScience(){
        listIntent = Intent(this,Science::class.java)
        startActivity(listIntent)
        mytype= "17"
        finish()
    }

}