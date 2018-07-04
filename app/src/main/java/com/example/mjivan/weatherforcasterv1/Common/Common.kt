package com.example.mjivan.weatherforcasterv1.Common

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Milesh on 2018/07/03.
 */
object Common {
val API_KEY = "f808d4d0e9edf8ae9f9051499f229194"
    val API_LINK = "http://api.openweathermap.org/data/2.5/"

    fun apiRequest (lat:String,lng:String):String
    {
        var sb=StringBuilder(API_LINK)
        sb.append("?lat=${lat}&lon=${lng}&APPID=${API_KEY}&units=metric")
        return sb.toString()
    }

    fun unixTimeSDtampToDateTime(unixTimeStamp:Double):String
    {
        val dateFormat = SimpleDateFormat("HH:mm")
        val date= Date()
        date.time= unixTimeStamp.toLong()*1000
        return dateFormat.format(date)
    }

    fun getImage(icon:String):String
    {
        return "http://openweathermap.org/img/w/${icon}.png"
    }

    val dateNow:String
    get()
    {
        val dateFormat = SimpleDateFormat("dd MM yyyy HH:mm")
        val date=Date()
        return dateFormat.format(date)
    }
}
