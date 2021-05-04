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
import java.net.URL
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
        fixedRateTimer("timer", false, 0, 600000){
            this@RecyclerViewActivity.runOnUiThread {
                WebScratch(adapter).execute()
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class WebScratch(private var mAdapter: CustomRecyclerAdapter) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void): Void? {
            try {
                Helper.matchList.clear()
                Jsoup.connect("https://greeklivechannels.ml/livematches.html").userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:87.0) Gecko/20100101 Firefox/87.0").get().run {
                    select("div.match").forEachIndexed { _, element ->
                        val championship = element.select("h3")[0].text()
                        val sound = element.select("h3")[1].text()
                        val teams = element.select("h3")[2].text()
                        val hour = element.select("h3")[3].text()
                        var m3u8 = "empty"
                        var yt = "empty"
                        var twitch = "empty"
                        //for (i in 1..(element.select("a").size) step 1) {
                            val link = element.select("a")[0].attr("href")
                            Jsoup.connect("https://greeklivechannels.ml/$link").userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:87.0) Gecko/20100101 Firefox/87.0").get().run {
                                select("div.container").forEachIndexed { _, element ->
                                    m3u8 = element.select("source").attr("src")
                                    yt = (element.select("iframe").attr("src")).substringAfterLast("/", "")
                                    twitch = element.select("iframe").attr("src").substringAfter("=").substringBefore('&')
                                }
                        }
                        if (yt.isNotEmpty() && "channel" !in yt) {
                            val doc = Jsoup.connect(("https://www.youtube.com/watch?v=").plus(yt)).userAgent("curl/7.37.0").get()
                            val regex: Pattern = Pattern.compile("https://manifest.googlevideo.com/api/manifest/hls_variant/(.*)m3u8")
                            val regexMatcher: Matcher = regex.matcher(doc.toString())
                            while (regexMatcher.find()) {
                                for (i in 1..regexMatcher.groupCount()) {
                                    m3u8 = ("https://manifest.googlevideo.com/api/manifest/hls_variant/").plus(regexMatcher.group(i)).plus("m3u8")
                                }
                            }
                        }
                        if (twitch.isNotEmpty() && "http" !in twitch) {
                            val doc = URL(("https://pwn.sh/tools/streamapi.py?url=twitch.tv/").plus(twitch)).readText()
                            m3u8 = doc.substringAfterLast("\": \"").substringBefore("\"}}")
                        }
                        var logoName = championship.toLowerCase(Locale.ROOT).replace("\\s+".toRegex(), "")
                        logoName = logoName.replace("νβα", "nba").replace("τεννις", "tennis").replace("Κύπελλο Ελλάδας", "kypeloelladas").replace("uefa", "")
                        val resID = resources.getIdentifier(logoName, "drawable", packageName)
                        if (sound!="Ξένο") {
                            Helper.matchList.add(AndroidVersionModel(resID, teams, championship, sound, hour, m3u8))
                            //You can move runOnUiThread { mAdapter.notifyDataSetChanged() } here to make them load one by one
                        }
                    }
                    runOnUiThread { mAdapter.notifyDataSetChanged() }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
    }
}