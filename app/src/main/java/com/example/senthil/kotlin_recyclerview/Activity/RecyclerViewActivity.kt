package com.example.senthil.kotlin_recyclerview.Activity

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.example.senthil.kotlin_recyclerview.Adapter.CustomRecyclerAdapter
import com.example.senthil.kotlin_recyclerview.Model.AndroidVersionModel
import com.example.senthil.kotlin_recyclerview.R
import com.example.senthil.kotlin_recyclerview.Utils.Helper
import org.jsoup.Jsoup
import java.io.IOException
import kotlin.concurrent.fixedRateTimer

class RecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        val rvRecyclerView = findViewById<RecyclerView>(R.id.sample_recyclerView)
        rvRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        var adapter = CustomRecyclerAdapter(this, Helper.Companion.getVersionsList())
        rvRecyclerView.adapter = adapter
        fixedRateTimer("timer", false, 0, 2000){
            this@RecyclerViewActivity.runOnUiThread {
                //Toast.makeText(this@RecyclerViewActivity, "text", Toast.LENGTH_SHORT).show()
                adapter.notifyDataSetChanged()
            }
        }
        fixedRateTimer("timer", false, 0, 720000){
            this@RecyclerViewActivity.runOnUiThread {
                //Toast.makeText(this@RecyclerViewActivity, "text", Toast.LENGTH_SHORT).show()
                WebScratch().execute()
            }
        }
    }

    inner class WebScratch : AsyncTask<Void, Void, Void>() {
        private lateinit var words: String
        override fun doInBackground(vararg params: Void): Void? {
            try {
                Helper.matchList.clear()
                val document =  Jsoup.connect("https://greeklivechannels.ml/livematches.html").get().run {
                    select("div.match").forEachIndexed { index, element ->
                        val championship = element.select("h3").get(0).text()
                        val sound = element.select("h3").get(1).text()
                        val teams = element.select("h3").get(2).text()
                        val hour = element.select("h3").get(3).text()
                        val links = element.select("a").size
                        for (i in 1..(element.select("a").size) step 1) {
                            var link = element.select("a").get(i - 1).attr("href")
                            println("$link")
                        }
                        var logoname = "$championship".toLowerCase().replace("\\s+".toRegex(), "")
                        logoname = logoname.replace("νβα","nba")
                        val resID = resources.getIdentifier(logoname, "drawable", packageName)
                        println("$logoname")
                        Helper.matchList.add(AndroidVersionModel(resID, "$teams", "$championship", "$sound", "$hour"))
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
    }
}
