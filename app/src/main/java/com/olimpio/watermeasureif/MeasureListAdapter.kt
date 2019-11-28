package com.olimpio.watermeasureif

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import kotlinx.android.synthetic.main.measure_item.view.*

class MeasureListAdapter(private var mMeasures: ArrayList<Measure>,
                         private val mContext: Context) : Adapter<MeasureListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val measure = mMeasures[position]
        holder.bindView(measure)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.measure_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = mMeasures.size

    fun updateList(measure: Measure) {
        insertItem(measure)
    }

    private fun insertItem(measure: Measure) {
        mMeasures.add(measure)
        notifyItemInserted(itemCount)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(measure: Measure) {

            val timestamp = itemView.tv_item_timestamp
            val temp = itemView.tv_item_temp
            val ph = itemView.tv_item_ph

            timestamp.text = measure.timestamp
            temp.text = measure.temp
            ph.text = measure.ph
        }
    }
}