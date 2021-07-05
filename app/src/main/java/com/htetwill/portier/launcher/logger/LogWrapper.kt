package com.htetwill.portier.launcher.logger

import android.os.Environment
import android.util.Log
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*


/**
 * Helper class which wraps Android's native Log utility in the Logger interface.  This way
 * normal DDMS output can be one of the many targets receiving and outputting logs simultaneously.
 */
class LogWrapper : LogNodeInterface {

    // For piping:  The next node to receive Log data after this one has done its work.
    var next: LogNodeInterface? = null

    /**
     * Prints data out to the console using Android's native log mechanism.
     * @param priority Log level of the data being logged.  Verbose, Error, etc.
     * @param tag Tag for for the log data.  Can be used to organize log statements.
     * @param msg The actual message to be logged. The actual message to be logged.
     * @param tr If an exception was thrown, this can be sent along for the logging facilities
     * to extract and print useful information.
     */
    override fun println(priority: Int, tag: String?, msg: String?, tr: Throwable?) {
        val priorityStr = when (priority) {
            android.util.Log.VERBOSE -> "VERBOSE"
            android.util.Log.DEBUG -> "DEBUG"
            android.util.Log.INFO -> "INFO"
            android.util.Log.WARN -> "WARN"
            android.util.Log.ERROR -> "ERROR"
            android.util.Log.ASSERT -> "ASSERT"
            else -> null
        }
        var msg = msg
        // There actually are log methods that don't take a msg parameter.  For now,
        // if that's the case, just convert null to the empty string and move on.
        var useMsg: String? = msg
        if (useMsg == null) {
            useMsg = ""
        }

        // If an exception was provided, convert that exception to a usable string and attach
        // it to the end of the msg method.
        if (tr != null) {
            msg += "\n$Log.getStackTraceString(tr)"
        }

        // This is functionally identical to Log.x(tag, useMsg);
        // For instance, if priority were Log.VERBOSE, this would be the same as Log.v(tag, useMsg)
        Log.println(priority, tag, useMsg)

        val externalStorageDir = Environment.getExternalStorageDirectory()
        val logFile = File(externalStorageDir, "log.txt")
        if (!logFile.exists()) {
            try {
                logFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            val buf = BufferedWriter(FileWriter(logFile, true))
            buf.append("date: ${Date()} priority: $priorityStr tag: $tag msg: $useMsg")
            buf.newLine()
            buf.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
