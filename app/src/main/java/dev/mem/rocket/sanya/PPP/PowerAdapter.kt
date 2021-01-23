package dev.mem.rocket.sanya.PPP

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import dev.mem.rocket.sanya.OOP.PowersClass
import dev.mem.rocket.sanya.R

/**
 * Created by intag pc on 2/16/2017.
 */

class PowerAdapter(var apps: List<PowersClass>) : RecyclerView.Adapter<PowerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.powe_itemlist, parent, false)
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val app = apps[position]
        holder.size.text = app.text
    }

    override fun getItemCount(): Int {
        return apps.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var size: TextView


        init {
            size = view.findViewById<View>(R.id.items) as TextView
        }
    }
}
