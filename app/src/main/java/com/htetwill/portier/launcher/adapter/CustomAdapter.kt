package com.htetwill.portier.launcher.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.htetwill.portier.launcher.model.AppInfo
import com.htetwill.portier.launcher.viewholder.CustomViewHolder

class CustomAdapter (private val appList : List<AppInfo>):RecyclerView.Adapter<CustomViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.setValue(appList[position])
    }

    override fun getItemCount(): Int = appList.size
}