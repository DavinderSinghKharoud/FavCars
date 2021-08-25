package com.example.favcars.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favcars.databinding.ItemCustomListBinding
import com.example.favcars.view.activities.AddUpdateCarsActivity

class CustomListItemAdapter(
    private val activity: Activity,
    private val lstItems: List<String>,
    private val selection: String
) : RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>() {

    class ViewHolder(view: ItemCustomListBinding) : RecyclerView.ViewHolder(view.root) {
        val tvText = view.tvText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCustomListBinding =
            ItemCustomListBinding.inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lstItems[position]
        holder.tvText.text = item
        holder.itemView.setOnClickListener {
            if (activity is AddUpdateCarsActivity) {
                activity.selectedListItem(item, selection)
            }
        }
    }

    override fun getItemCount(): Int {
        return lstItems.size
    }

}