package com.htetwill.portier.launcher.viewholder

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.htetwill.portier.launcher.R
import com.htetwill.portier.launcher.databinding.LayoutListItemBinding
import com.htetwill.portier.launcher.model.AppInfo

class CustomViewHolder (private val rootView: View) : RecyclerView.ViewHolder(rootView){
    private val binding = LayoutListItemBinding.bind(rootView)
    companion object {
        fun from(parent: ViewGroup): CustomViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.layout_list_item, parent, false)
            return CustomViewHolder(view)
        }
    }

    fun setValue(item: AppInfo?) {
        if (item != null) {
            binding.listAppsRowName.text = item.label
            binding.listAppsRowIcon.setImageDrawable(item.icon)

            binding.listAppsRowIcon.setOnClickListener { onClickListener(item.packageName) }
            binding.listAppsRowName.setOnClickListener { onClickListener(item.packageName) }
        }
    }

    private fun onClickListener(packageName: CharSequence?) {
        val appPackageName = packageName.toString()
        val launchIntent: Intent = rootView.context.packageManager
            .getLaunchIntentForPackage(appPackageName)!!
        rootView.context.startActivity(launchIntent)
    }
}
