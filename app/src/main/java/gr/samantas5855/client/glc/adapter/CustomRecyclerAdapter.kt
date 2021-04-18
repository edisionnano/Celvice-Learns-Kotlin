package gr.samantas5855.client.glc.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gr.samantas5855.client.glc.R
import gr.samantas5855.client.glc.model.AndroidVersionModel
import gr.samantas5855.client.glc.player.Exo
import gr.samantas5855.client.glc.utils.Helper

class CustomRecyclerAdapter(private val androidVersionList: ArrayList<AndroidVersionModel>, var mContext: Context) : RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder>() {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.txtTitle.text = androidVersionList[p1].matchTeams
        p0.txtContent.text = "⚽ ${androidVersionList[p1].championshipName} \uD83D\uDD0A ${androidVersionList[p1].soundLang} \uD83D\uDD53 ${androidVersionList[p1].matchTime}"
        p0.image.setImageResource(androidVersionList[p1].imgResId!!)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.recycler_view_item, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return androidVersionList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.appOSTitle_txtVw)
        val txtContent: TextView = itemView.findViewById(R.id.appOSDetails_txtVw)
        val image: ImageView = itemView.findViewById(R.id.appOS_imageVw)
        private val intent = Intent(mContext,Exo::class.java)
        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition
                //Toast.makeText(itemView.context, "You click on $position", Toast.LENGTH_SHORT).show()
                intent.putExtra("pos", Helper.matchList[position].m3u8Link)
                mContext.startActivity(intent)
            }
        }

    }
}