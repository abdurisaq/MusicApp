package com.example.modularapp.download.logic

interface Downloader {

    fun downloadFile(url: String,title: String,type:String):Long?


}