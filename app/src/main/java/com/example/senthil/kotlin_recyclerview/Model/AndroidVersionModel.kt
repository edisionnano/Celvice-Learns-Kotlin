package com.example.senthil.kotlin_recyclerview.Model

class  AndroidVersionModel{
    var imgResId : Int? = 0
    var matchTeams: String? = null
    var championshipName: String? = null
    var soundLang: String? = null
    var matchTime: String? = null

    constructor(imgResId: Int, matchTeams: String, championshipName: String, soundLang: String, matchTime: String) {
        this.imgResId = imgResId
        this.matchTeams = matchTeams
        this.championshipName = championshipName
        this.soundLang = soundLang
        this.matchTime = matchTime
    }
}
