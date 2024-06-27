package com.example.modularapp.download

interface Downloader {
    fun downloadFile(url: String): Long
    fun downloadFile2(url: String,title: String,type:String):Long


}