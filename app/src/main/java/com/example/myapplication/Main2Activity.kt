package com.example.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.layoutref.*


class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val pokeHolder = (intent.getSerializableExtra("Pokemon") as Array<Pokemon>).toCollection(ArrayList())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val pokeNames : ArrayList<String> = ArrayList()
        for(i in 0..pokeHolder.size-1){
            pokeNames.add("${pokeHolder[i].name.capitalize()}\n${pokeHolder[i].genus}")
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ViewAdapt(pokeNames,pokeHolder)

    }

    fun back(view:View)
    {
        finish()
    }




}
