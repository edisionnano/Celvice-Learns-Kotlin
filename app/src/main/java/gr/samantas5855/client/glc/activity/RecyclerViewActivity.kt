package gr.samantas5855.client.glc.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.example.senthil.kotlin_recyclerview.R
import gr.samantas5855.client.glc.adapter.CustomRecyclerAdapter
import gr.samantas5855.client.glc.model.AndroidVersionModel
import gr.samantas5855.client.glc.utils.Helper
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*
import kotlin.concurrent.fixedRateTimer

class RecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        val rvRecyclerView = findViewById<RecyclerView>(R.id.sample_recyclerView)
        rvRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val adapter = CustomRecyclerAdapter(Helper.getVersionsList())
        rvRecyclerView.adapter = adapter
        WebScratch().execute()
        Thread.sleep(3000L)
        adapter.notifyDataSetChanged()
        fixedRateTimer("timer", false, 0, 60000){
            this@RecyclerViewActivity.runOnUiThread {
                //Toast.makeText(this@RecyclerViewActivity, "text", Toast.LENGTH_SHORT).show()
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
                        for (i in 1..(element.select("a").size) step 1) {
                            element.select("a")[i - 1].attr("href")
                            //println(link)
                        }
                        var logoName = championship.toLowerCase(Locale.ROOT).replace("\\s+".toRegex(), "")
                        logoName = logoName.replace("νβα","nba")
                        val resID = resources.getIdentifier(logoName, "drawable", packageName)
                        Helper.matchList.add(AndroidVersionModel(resID, teams, championship, sound, hour))
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
    }
}