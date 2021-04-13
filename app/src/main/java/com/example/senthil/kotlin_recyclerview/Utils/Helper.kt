package com.example.senthil.kotlin_recyclerview.Utils

import com.example.senthil.kotlin_recyclerview.Model.AndroidVersionModel
import com.example.senthil.kotlin_recyclerview.R

class Helper{
    companion object {
        public var matchList = ArrayList<AndroidVersionModel>()
        public fun <ArrayList> getVersionsList(): ArrayList {
            //matchList.add(AndroidVersionModel(R.drawable.superleague, "Ολυμπιακός - Παναθηναίκός", "Super League", soundLang = "Ελληνικό", matchTime = "10:00"))
            return matchList as ArrayList
        }
    }
}