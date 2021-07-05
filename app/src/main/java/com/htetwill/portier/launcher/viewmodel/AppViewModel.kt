package com.htetwill.portier.launcher.viewmodel

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.htetwill.portier.launcher.model.AppInfo
import java.util.*
import kotlin.math.min

class AppViewModel : ViewModel() {
    private val appsListDisplayed = mutableListOf<AppInfo>()
    private val appInfoList = mutableListOf<AppInfo>()
    private val appListDisplayedLiveData = MutableLiveData<List<AppInfo>>()

    fun listLiveData(): LiveData<List<AppInfo>> = appListDisplayedLiveData

    fun loadSuggestion(keyword: String?) {
        keyword?.let {
            appsListDisplayed.clear()
            if (it.trim().isEmpty()) {
                appsListDisplayed.addAll(appInfoList)
            } else {
                val map = mutableMapOf<AppInfo, Double>()
                for (item in appInfoList) {
                    map[item] = similarity(
                        it,
                        item.label.toString()
                    )
                }
                val sorted = map.toList().sortedBy { (_, value) -> value }.reversed().toMap()
                if (sorted.values.first().equals(1.0)) {
                    appsListDisplayed.add(sorted.keys.first())
                } else {
                    appsListDisplayed.addAll(sorted.keys)
                    appsListDisplayed.subList(3, appsListDisplayed.size).clear()
                }
            }
            appListDisplayedLiveData.postValue(appsListDisplayed.toList())
        }
    }

    fun findAppName(keyword: String?) {
        keyword?.let {
            appsListDisplayed.clear()
            if (it.isEmpty()) {
                appsListDisplayed.addAll(appInfoList)
            } else {
                for (item in appInfoList) {
                    if (item.label.toString().toLowerCase(Locale.ROOT).contains(
                            it.toLowerCase(
                                Locale.ROOT
                            )
                        )
                    ) {
                        appsListDisplayed.add(item)
                    }
                }
            }
            appListDisplayedLiveData.postValue(appsListDisplayed.toList())
        }
    }

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     * https://stackoverflow.com/questions/955110/similarity-string-comparison-in-java
     */
    private fun similarity(s1: String, s2: String): Double {
        var longer = s1
        var shorter = s2
        if (s1.length < s2.length) { // longer should always have greater length
            longer = s2
            shorter = s1
        }
        val longerLength = longer.length
        if (longerLength == 0) {
            return 1.0 /* both strings are zero length */
        } else
            return (longerLength - editDistance(longer, shorter)) / longerLength.toDouble()

        /* // If you have StringUtils, you can use it to calculate the edit distance:
        return (longerLength - StringUtils.getLevenshteinDistance(longer, shorter)) /
                                                             (double) longerLength; */
    }

    // Example implementation of the Levenshtein Edit Distance
    // See http://r...content-available-to-author-only...e.org/wiki/Levenshtein_distance#Java
    private fun editDistance(s1: String, s2: String): Int {
        var s1 = s1
        var s2 = s2
        s1 = s1.toLowerCase()
        s2 = s2.toLowerCase()
        val costs = IntArray(s2.length + 1)
        for (i in 0..s1.length) {
            var lastValue = i
            for (j in 0..s2.length) {
                if (i == 0) costs[j] = j else {
                    if (j > 0) {
                        var newValue = costs[j - 1]
                        if (s1[i - 1] != s2[j - 1]) newValue = Math.min(
                            min(newValue, lastValue),
                            costs[j]
                        ) + 1
                        costs[j - 1] = lastValue
                        lastValue = newValue
                    }
                }
            }
            if (i > 0) costs[s2.length] = lastValue
        }
        return costs[s2.length]
    }


    @SuppressLint("QueryPermissionsNeeded")
    fun loadInstalledPackage(packageManager: PackageManager) {
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
        appListDisplayedLiveData.postValue(appInfoList.toList())
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