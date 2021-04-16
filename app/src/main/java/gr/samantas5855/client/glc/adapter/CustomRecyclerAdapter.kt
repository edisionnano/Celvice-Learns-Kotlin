package gr.samantas5855.client.glc.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import gr.samantas5855.client.glc.model.AndroidVersionModel
import gr.samantas5855.client.glc.R

class CustomRecyclerAdapter(private val androidVersionList: ArrayList<AndroidVersionModel>) : RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder>() {

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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.appOSTitle_txtVw)
        val txtContent: TextView = itemView.findViewById(R.id.appOSDetails_txtVw)
        val image: ImageView = itemView.findViewById(R.id.appOS_imageVw)

        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition
                Toast.makeText(itemView.context, "You click on $position", Toast.LENGTH_SHORT).show()
            }
        }

    }
}