package com.htetwill.portier.launcher.viewmodel

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.htetwill.portier.launcher.model.AppInfo

class AppViewModel:ViewModel() {
    private val appInfoList = mutableListOf<AppInfo>()

    fun getAppInfoList() : List<AppInfo> = appInfoList

    fun loadApps(packageManager: PackageManager) {
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val allApps = packageManager.queryIntentActivities(i, 0)
        for (ri in allApps) {
            val app = AppInfo()
            app.label = ri.loadLabel(packageManager)
            app.packageName = ri.activityInfo.packageName
            app.icon = ri.activityInfo.loadIcon(packageManager)
            appInfoList.add(app)
        }
        appInfoList.sortBy { it.label.toString() }
//        appsList.clear()
//        appsList.addAll(loadList)
    }



    class Factory() : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AppViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}