package com.example.crime


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReportsAdapter(private val reportsList: List<Report>) :
    RecyclerView.Adapter<ReportsAdapter.ReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.report_item, parent, false)
        return ReportViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val currentReport = reportsList[position]
        holder.descriptionText.text = currentReport.description
        holder.timestampText.text = currentReport.timestamp.toString()
    }

    override fun getItemCount() = reportsList.size

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
        val timestampText: TextView = itemView.findViewById(R.id.timestampText)
    }
}
