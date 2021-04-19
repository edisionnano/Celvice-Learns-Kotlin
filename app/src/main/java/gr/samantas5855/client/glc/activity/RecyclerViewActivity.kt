package gr.samantas5855.client.glc.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gr.samantas5855.client.glc.R
import gr.samantas5855.client.glc.adapter.CustomRecyclerAdapter
import gr.samantas5855.client.glc.model.AndroidVersionModel
import gr.samantas5855.client.glc.utils.Helper
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.concurrent.fixedRateTimer

class RecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        val rvRecyclerView = findViewById<RecyclerView>(R.id.sample_recyclerView)
        rvRecyclerView.layoutManager = LinearLayoutManager(this@RecyclerViewActivity, RecyclerView.VERTICAL, false)
        val adapter = CustomRecyclerAdapter(Helper.getVersionsList(), this@RecyclerViewActivity)
        rvRecyclerView.adapter = adapter
        WebScratch().execute()
        Thread.sleep(2500L)
        adapter.notifyDataSetChanged()
        fixedRateTimer("timer", false, 0, 60000){
            this@RecyclerViewActivity.runOnUiThread {
                WebScratch().execute()
                Thread.sleep(2000L)
                adapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class WebScratch : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void): Void? {
            try {
                Helper.matchList.clear()
                Jsoup.connect("https://greeklivechannels.ml/livematches.html").get().run {
                    select("div.match").forEachIndexed { _, element ->
                        val championship = element.select("h3")[0].text()
                        val sound = element.select("h3")[1].text()
                        val teams = element.select("h3")[2].text()
                        val hour = element.select("h3")[3].text()
                        var m3u8 = "empty"
                        //for (i in 1..(element.select("a").size) step 1) {
                            val link = element.select("a")[0].attr("href")
                            println(link)
                            Jsoup.connect("https://greeklivechannels.ml/$link").get().run {
                                select("div.container").forEachIndexed { _, element ->
                                    m3u8 = element.select("source").attr("src")
                                }
                        }
                        var logoName = championship.toLowerCase(Locale.ROOT).replace("\\s+".toRegex(), "")
                        logoName = logoName.replace("νβα", "nba").replace("τεννις", "tennis")
                        val resID = resources.getIdentifier(logoName, "drawable", packageName)
                        if (sound!="Ξένο") {
                            Helper.matchList.add(AndroidVersionModel(resID, teams, championship, sound, hour, m3u8))
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
    }
}