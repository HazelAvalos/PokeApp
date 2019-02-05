package com.example.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle



import android.os.AsyncTask

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection



class MainActivity : AppCompatActivity() {

    var isRunning = false
    var url = "https://pokeapi.co/api/v2/pokemon/ditto/"
    private var pokeHolder = mutableListOf<Pokemon>()
    var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }

    fun next(view:View) {
        var intent = Intent(this, Main2Activity::class.java)
        intent.putExtra("Pokemon", pokeHolder.toTypedArray())
        startActivity(intent)
    }

    fun getAbility(view: View) {
        if (!isRunning && PokeName.text.toString() != "") {
            if(pokeHolder.count()!=0) {
                if (pokeHolder.last().name != PokeName.text.toString().decapitalize().trim()) {
                    var poke: String = PokeName.text.toString()
                    poke = poke.trim()
                    poke = poke.toLowerCase()
                    url = "https://pokeapi.co/api/v2/pokemon/$poke/"
                    getAbilityJSON().execute()
                }
            }
            else{
                var poke: String = PokeName.text.toString()
                poke = poke.trim()
                poke = poke.toLowerCase()
                url = "https://pokeapi.co/api/v2/pokemon/$poke/"
                getAbilityJSON().execute()
            }
        }
    }

    fun getAbilityDes(view: View) {
        if(counter > pokeHolder.last().abilities.count()-1) {
            counter = 0
        }
        getAbilityDescriptionJSON(counter).execute()
        counter ++
    }

    fun loadImg(imagehold: String) {
        var imageView = findViewById<View>(R.id.imageView) as ImageView
        Picasso.get().load(imagehold).resize(1400, 1400).into(imageView)
    }

    fun convertStreamToString(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        var line: String? = reader.readLine()
        while (line != null) {
            sb.append(line).append("\n")
            line = reader.readLine()
        }
        reader.close()
        return sb.toString()
    }

    fun getJSON(url:String) : String
    {
        var result = ""
        try {
            val trueUrl = URL(url)
            val conn = trueUrl.openConnection() as HttpsURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Acccept", "application/json")
            conn.connect()
            val response : Int = conn.responseCode
            if(response == 200) {
                val inputStream = conn.inputStream
                if(inputStream!=null) {
                    result = convertStreamToString(inputStream)
                }
            }
        }
        catch(e: IOException) {

        }
        return result
    }

    inner class getFlavorText( ) : AsyncTask<Void, Void, String?>() {
        var result = "error"

        override fun doInBackground(vararg params: Void?): String? {
            isRunning = true
            return getJSON(pokeHolder.last().flavorTextUrl)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var flavorDes = ""
            var pokeType = "The "
            var inttemp = 0
            try {
                inttemp = JSONArray((JSONObject(result.toString()).get("flavor_text_entries")).toString()).length()
                flavorDes  = JSONObject(JSONArray((JSONObject(result.toString()).get("flavor_text_entries")).toString())[inttemp-1].toString()).get("flavor_text").toString().trim()
                pokeType += JSONObject(JSONArray(JSONObject(result.toString()).get("genera").toString())[2].toString()).get("genus").toString()
                FlavorText.text = flavorDes.trim().replace("\n"," ")
                PokeGenus.text = pokeType
                pokeHolder.last().genus =("$pokeType \n${flavorDes.trim().replace("\n"," ")}")
                isRunning = false
            }
            catch( e : Exception) {
                Toast.makeText(this@MainActivity, "Enter valid Pokemon name", Toast.LENGTH_SHORT).show()
                isRunning = false
            }
            if(counter > pokeHolder.last().abilities.count()-1) {
                counter = 0
            }
                getAbilityDescriptionJSON(counter).execute()
                counter ++


        }
    }





    inner class getAbilityDescriptionJSON( var x: Int ) : AsyncTask<Void, Void, String?>() {
        var result = "error"

        override fun doInBackground(vararg params: Void?): String? {
            isRunning = true
            return getJSON(pokeHolder.last().abilities[x].url)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var abilityDes = ""
            try {
                abilityDes  = ((JSONArray((JSONObject(result.toString()).get("effect_entries")).toString())).getJSONObject(0)).getString("short_effect")
                des.text ="${pokeHolder.last().abilities[x].name.replace("-", " ")}:\n${abilityDes.replace("-"," ").trim()}"
                isRunning = false
            }
            catch( e : Exception) {
                Toast.makeText(this@MainActivity, "Enter valid Pokemon name", Toast.LENGTH_SHORT).show()
                isRunning = false
            }

        }
    }

        inner class  getAbilityJSON( ) : AsyncTask<Void, Void, String?>() {
        var result = "error"

        override fun doInBackground(vararg params: Void?): String? {
            isRunning = true
            return getJSON(url)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            counter = 0
            var urlhold = mutableListOf<String>()
            var namehold = mutableListOf<String>()
            var flavorUrl = ""
            var imagehold = ""
            try {
                var abilitesHolder = mutableListOf<Ability>()
                var numAbilities = ((JSONArray(JSONObject(result.toString()).get("abilities").toString())).length() - 1)
                imagehold = (JSONObject(((JSONObject(result.toString())).get("sprites")).toString()).getString("front_default"))
                for (x in 0.. numAbilities) {
                    urlhold.add(JSONObject((JSONArray(JSONObject(result.toString()).get("abilities").toString())).getJSONObject(x).getString("ability")).getString("url"))
                    namehold.add(JSONObject((JSONArray(JSONObject(result.toString()).get("abilities").toString())).getJSONObject(x).getString("ability")).getString("name").capitalize())
                    flavorUrl = JSONObject((JSONObject(result.toString()).get("species")).toString()).get("url").toString()
                    abilitesHolder.add(Ability(namehold[x],x,urlhold[x]))
                }
                pokeHolder.add(Pokemon(PokeName.text.toString().trim().decapitalize(), imagehold, abilitesHolder.toTypedArray(), flavorUrl))
                loadImg(imagehold)
                var stringHolder = "Abilities:\n"
                pokeHolder.last().abilities.forEach{
                    stringHolder += ("${it.name.capitalize().replace("-", " ")} \n")
                }
                text.text =  stringHolder.trim()
                isRunning = false
        }
            catch( e : Exception) {
                Toast.makeText(this@MainActivity, "Enter valid Pokemon name",
                Toast.LENGTH_SHORT).show()
                isRunning = false
            }

            getFlavorText( ).execute()
        }
    }

}
