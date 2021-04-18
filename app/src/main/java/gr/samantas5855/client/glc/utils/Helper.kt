package gr.samantas5855.client.glc.utils

import gr.samantas5855.client.glc.model.AndroidVersionModel

class Helper{
    companion object {
        var matchList = ArrayList<AndroidVersionModel>()
        fun <ArrayList> getVersionsList(): ArrayList = matchList as ArrayList
    }
}