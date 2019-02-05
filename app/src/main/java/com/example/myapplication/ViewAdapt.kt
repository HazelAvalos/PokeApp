package com.example.myapplication

import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class ViewAdapt(val users: ArrayList<String>, val pokeHolder: ArrayList<Pokemon>): RecyclerView.Adapter<ViewAdapt.ViewHolder>() {

    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val pokeName : TextView = itemView.findViewById(R.id.pName)
        val pokeImg : ImageView = itemView.findViewById(R.id.pImage)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view: View = LayoutInflater.from(p0.context).inflate(R.layout.layoutref, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(p0: ViewAdapt.ViewHolder, p1: Int) {
        p0.pokeName.text = users[p1]
        Picasso.get().load("${pokeHolder[p1].urlImage}").resize(400,400).into(p0.pokeImg)
        p0.pokeImg.contentDescription = p1.toString()
    }
    fun loadImg(imagehold: String, itemView:View) {
        var imageView = itemView.findViewById<View>(R.id.imageView) as ImageView
        Picasso.get().load(imagehold).resize(200, 200).into(imageView)
    }


}