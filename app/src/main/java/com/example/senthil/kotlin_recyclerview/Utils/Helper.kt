package com.example.senthil.kotlin_recyclerview.Utils

import com.example.senthil.kotlin_recyclerview.Model.AndroidVersionModel
import com.example.senthil.kotlin_recyclerview.R

class Helper{
    companion object {
        fun <ArrayList> getVersionsList(): ArrayList {
            var androidVersionList = ArrayList<AndroidVersionModel>()
            androidVersionList.add(AndroidVersionModel(R.drawable.superleague, "Ολυμπιακός - Παναθηναίκός", "Super League", soundLang = "Ελληνικό", matchTime = "10:00"))
            return androidVersionList as ArrayList
        }
    }
}