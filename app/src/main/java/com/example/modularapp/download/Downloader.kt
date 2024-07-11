package com.example.modularapp.download

interface Downloader {

    fun downloadFile(url: String,title: String,type:String):Long?


}