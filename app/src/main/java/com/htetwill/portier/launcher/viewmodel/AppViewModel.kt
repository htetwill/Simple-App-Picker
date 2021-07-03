package com.htetwill.portier.launcher.viewmodel

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.htetwill.portier.launcher.model.AppInfo

class AppViewModel:ViewModel() {

    @SuppressLint("QueryPermissionsNeeded")
    fun getApps(packageManager: PackageManager) : List<AppInfo>{
        val appInfoList = mutableListOf<AppInfo>()
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
        return appInfoList.toList()
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