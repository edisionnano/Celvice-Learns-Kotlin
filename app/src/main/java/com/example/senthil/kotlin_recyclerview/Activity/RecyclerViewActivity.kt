package com.example.senthil.kotlin_recyclerview.Activity

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.example.senthil.kotlin_recyclerview.Adapter.CustomRecyclerAdapter
import com.example.senthil.kotlin_recyclerview.Model.AndroidVersionModel
import com.example.senthil.kotlin_recyclerview.R
import com.example.senthil.kotlin_recyclerview.Utils.Helper
import org.jsoup.Jsoup
import java.io.IOException

class RecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        val rvRecyclerView = findViewById<RecyclerView>(R.id.sample_recyclerView)
        rvRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        WebScratch().execute()
        var adapter = CustomRecyclerAdapter(this, Helper.getVersionsList())
        rvRecyclerView.adapter = adapter
    }

    inner class WebScratch : AsyncTask<Void, Void, Void>() {
        private lateinit var words: String
        override fun doInBackground(vararg params: Void): Void? {
            try {
                val document =  Jsoup.connect("https://greeklivechannels.ml/livematches.html").get().run {
                    select("div.match").forEachIndexed { index, element ->
                        val titleAnchor = element.select("h3 a")
                        val title = titleAnchor.text()
                        val url = titleAnchor.attr("href")
                        //3. Dumping Search Index, Title and URL on the stdout.
                        Helper.matchList.add(AndroidVersionModel(R.drawable.superleague, "Ολυμπιακός - Παναθηναίκός", "Super League", soundLang = "Ελληνικό", matchTime = "10:00"))
                        println("$index. $title ($url)")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
    }
}
