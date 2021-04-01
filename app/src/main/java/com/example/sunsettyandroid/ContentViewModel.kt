package com.example.sunsettyandroid

import android.util.Log
import com.example.sunsettyandroid.APIs.CityResponse
import com.example.sunsettyandroid.APIs.SunResponse
import com.example.sunsettyandroid.APIs.TimezoneResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

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

    var guessedTime:String? = null
    var guessedTimeHourOffset:Double? = null
    var guessedTimeMinuteOffset:Double? = null
    var currentTime = Date()

    var isSunrise:Boolean? = null

    init {
        isSunrise = getRandomBoolean()
    }
    companion object {
        val shared = ContentViewModel()
    }

    fun getResult(guessedTime:String)
    {


    }

    fun getRandomBoolean(): Boolean {
        val random = Random()
        return random.nextBoolean()
    }

    suspend fun getCity()
    {
        withContext(Dispatchers.IO) {
            var randomNumber = Random().nextInt(870)
            val theurl = URL("http://geodb-free-service.wirefreethought.com/v1/geo/cities?limit=1&offset=$randomNumber&minPopulation=1000000&excludedCountryIds=CN")

            val theConnection = (theurl.openConnection() as? HttpURLConnection)!!.apply {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                setRequestProperty("Accept", "application/json")
            }

            val reader = BufferedReader(theConnection.inputStream.reader())

            val theResultString = reader.readText()

            val theCityData = Gson().fromJson(theResultString, CityResponse::class.java)

            cityName = theCityData.data[0].name
            cityCountry = theCityData.data[0].country
            latitude = theCityData.data[0].latitude
            longitude = theCityData.data[0].longitude

            withContext(Dispatchers.Main) {
              //  Log.d("pia9debug", "CITY RESULTAT")
               // Log.d("pia9debug", theResultString)
            }

            getSunriseSunset()
        }
    }

    suspend fun getSunriseSunset()
    {
        withContext(Dispatchers.IO) {
            val theurl = URL("https://api.sunrise-sunset.org/json?lat=$latitude&lng=$longitude&date=today")

            val theConnection = (theurl.openConnection() as? HttpURLConnection)!!.apply {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                setRequestProperty("Accept", "application/json")
            }

            val reader = BufferedReader(theConnection.inputStream.reader())

            val theResultString = reader.readText()

            val sunriseSunsetData = Gson().fromJson(theResultString, SunResponse::class.java)

            sunriseUTCTime = timeConvert24(sunriseSunsetData.results.sunrise)
            sunsetUTCTime = timeConvert24(sunriseSunsetData.results.sunset)

            withContext(Dispatchers.Main) {
              //  Log.d("pia9debug", "SUN RESULTAT")
               Log.d("pia9debug", theResultString)
            }

            Log.d("Debug", sunriseUTCTime!!)
            Log.d("Debug", sunsetUTCTime!!)
            getGMTOffset()
        }
    }

    suspend fun getGMTOffset()
    {
        withContext(Dispatchers.IO) {
            val theurl = URL("http://api.timezonedb.com/v2.1/get-time-zone?key=3WMNYAOTEDF5&format=json&by=position&lat=$latitude&lng=$longitude")

            val theConnection = (theurl.openConnection() as? HttpURLConnection)!!.apply {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                setRequestProperty("Accept", "application/json")
            }

            val reader = BufferedReader(theConnection.inputStream.reader())

            val theResultString = reader.readText()

            val gmtOffsetData = Gson().fromJson(theResultString, TimezoneResponse::class.java)


            withContext(Dispatchers.Main) {
                Log.d("pia9debug", "GMT RESULTAT")
                Log.d("pia9debug", theResultString)
                //Log.d("debug" , gmtOffsetData.gmtOffset.toString())
            }

            sunriselocalTime = adjustTimeOffset(sunriseUTCTime!!,gmtOffsetData.gmtOffset)
            sunsetlocalTime = adjustTimeOffset(sunsetUTCTime!!,gmtOffsetData.gmtOffset)

            Log.d("debug", adjustTimeOffset(sunriseUTCTime!!,gmtOffsetData.gmtOffset))
        }
    }

    fun timeConvert24(timeToConvert:String): String
    {
        val date12Format = SimpleDateFormat("hh:mm:ss a", Locale.US)
        val date24Format = SimpleDateFormat("HH:mm", Locale.US)
        return date24Format.format(date12Format.parse(timeToConvert))
    }

    fun adjustTimeOffset(UTCtime: String, offset: Int) : String
    {
        var dateFormat = SimpleDateFormat("HH:mm")
        var UTCTimeDate = Date(dateFormat.parse(UTCtime).time)

        var calendar = Calendar.getInstance()
        calendar.setTime(UTCTimeDate)

        calendar.add(Calendar.SECOND, offset)

        var localTime = calendar.getTime()

        return dateFormat.format(localTime)

        /*
        Log.d("Debug",UTCtime)
        Log.d("Debug", offset.toString())
        val df = SimpleDateFormat("HH:mm")
        var UTCtimeAsLong = df.parse(UTCtime).time
        Log.d("Debug",df.format(UTCtimeAsLong))
        Log.d("Debug",df.format(UTCtimeAsLong + offset))
        val date = Date(UTCtimeAsLong + offset)

        return df.format(date)


         */



        /*var UTCtimeAsDate = SimpleDateFormat("HH:mm").parse(UTCtime)
        Log.d("debug", UTCtimeAsDate.time.toString())
        var localTimeLong = UTCtimeAsDate.time + offset
        Log.d("debug", localTimeLong.toString())
        val localTimeDate = Date(localTimeLong)

        val date24Format = SimpleDateFormat("HH:mm")
       // Log.d("debug", date24Format.format(localTime))
        return date24Format.format(localTimeDate)

         */
    }

    fun compareTime(guessedTime: String)
    {
        var calendar = Calendar.getInstance()

        val dateFormat = SimpleDateFormat("HH:mm")

        this.guessedTime = guessedTime
        val guessedTimeDate = dateFormat.parse(guessedTime)
        val actualTimeDate : Date
        if (isSunrise == true)
        {
            actualTimeDate = dateFormat.parse(sunriselocalTime)
        }
        else{
            actualTimeDate = dateFormat.parse(sunsetlocalTime)
        }

        //LÄGG TILL SEKUNDER OM ÖVER 12 TIMMAR
        var differenceInSeconds = kotlin.math.abs(guessedTimeDate.time - actualTimeDate.time)/1000
        if (differenceInSeconds>43200)
        {
            var secondsOver12Hours = differenceInSeconds - 43200
            differenceInSeconds = 43200 - secondsOver12Hours
            differenceInSeconds = kotlin.math.abs(differenceInSeconds)
        }

        val hoursOff = floor((differenceInSeconds / 3600).toDouble())
        val minutesOff = (differenceInSeconds - (hoursOff * 3600))/60

        guessedTimeHourOffset = hoursOff
        guessedTimeMinuteOffset = minutesOff
    }
}