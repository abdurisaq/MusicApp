package com.example.modularapp.services

fun millisecondsToMinuteAndSeconds(duration:Int):String{
    val totalSeconds :Int = duration/1000
    val minutes: Int = totalSeconds/ 60
    val leftoverSeconds:Int = totalSeconds - (minutes*60)
    var leftoverSecondsString:String = leftoverSeconds.toString()
    if(leftoverSecondsString.length <= 1){
        leftoverSecondsString = "0${leftoverSecondsString}"
    }
    return "${minutes}:${leftoverSecondsString}"
}