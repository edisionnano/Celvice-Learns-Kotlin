package gr.samantas5855.client.glc.utils

import gr.samantas5855.client.glc.model.AndroidVersionModel

class Helper{
    companion object {
        var matchList = ArrayList<AndroidVersionModel>()
        fun <ArrayList> getVersionsList(): ArrayList {
            //matchList.add(AndroidVersionModel(R.drawable.superleague, "Ολυμπιακός - Παναθηναίκός", "Super League", soundLang = "Ελληνικό", matchTime = "10:00"))
            return matchList as ArrayList
        }
    }
}