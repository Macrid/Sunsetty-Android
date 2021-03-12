package com.example.sunsettyandroid

import android.util.Log
import com.example.sunsettyandroid.APIs.CityResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.BufferedReader
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class ContentViewModel{
   // private val client = OkHttpClient()

    var cityName = ""
    var cityCountry = ""
    var latitude:Float? = null
    var longitude:Float? = null

    var sunriseUTCTime:String? = null
    var sunriselocalTime:String? = null
    var sunsetUTCTime:String? = null
    var sunsetlocalTime:String? = null

    var gmtOffset:Int? = null

    var guessedTimeHourOffset:Int? = null
    var guessedTimeMinuteOffset:Int? = null
    var currentTime = Date()

    companion object {
        val shared = ContentViewModel()
    }

    /*
    fun getCity(url: String){
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.w("Debug", "Failed call")
            }
            override fun onResponse(call: Call, response: Response){
                Log.w("Debug", response.body()?.string()!!)
            }
        })

    }

     */

    suspend fun getCity()
    {
        withContext(Dispatchers.IO) {
            val theurl = URL("http://geodb-free-service.wirefreethought.com/v1/geo/cities?limit=1&offset=1&minPopulation=1000000&excludedCountryIds=CN")

            val theConnection = (theurl.openConnection() as? HttpURLConnection)!!.apply {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                setRequestProperty("Accept", "application/json")
            }

            val reader = BufferedReader(theConnection.inputStream.reader())

            val theResultString = reader.readText()

            val theCity = Gson().fromJson(theResultString, CityResponse::class.java)

            withContext(Dispatchers.Main) {
                Log.d("pia9debug", "RESULTAT")
                Log.d("pia9debug", theResultString)
            }


        }


    }
}