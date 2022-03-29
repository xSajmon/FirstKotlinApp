package com.example.myapplication.utils

import android.app.Application
import android.content.Context

class MainApplication :Application(){
    init{
        instance = this
    }

   companion object{

       private  var instance: MainApplication? = null

       fun applicationContext(): Context {
           return instance!!.applicationContext
       }
   }
}