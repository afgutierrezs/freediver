package com.android.freediver.ui.co2table

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.freediver.R
import com.android.freediver.model.Row
import com.android.freediver.util.Coversions.Companion.secsToTime

class RowAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context
    lateinit var viewModel: CO2TableViewModel
    var data = listOf<Row>()

    constructor(viewModel: CO2TableViewModel, context: Context) : this(){
        this.viewModel = viewModel
        this.context = context
        this.data = viewModel.rows
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_list_row, parent, false)
        return RowViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        val rowViewHolder = holder as RowViewHolder
        rowViewHolder.rowIndex.text = (position + 1).toString()
        rowViewHolder.breathTime.text = secsToTime(item.breath)
        rowViewHolder.holdTime.text = secsToTime(item.hold)

    }

    class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rowIndex: TextView = itemView.findViewById(R.id.rowIndexTextView)
        val holdTime: TextView = itemView.findViewById(R.id.holdTimeTextView)
        val breathTime: TextView = itemView.findViewById(R.id.breathTimeTextView)
    }

}