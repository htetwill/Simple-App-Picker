package com.htetwill.portier.launcher.logger

/**
 * Helper class for a list (or tree) of LoggerNodes.
 *
 *
 * When this is set as the head of the list,
 * an instance of it can function as a drop-in replacement for [android.util.Log].
 * Most of the methods in this class server only to map a method call in Log to its equivalent
 * in LogNode.
 */
object Log {
    // Grabbing the native values from Android's native logging facilities,
    // to make for easy migration and interop.
    val NONE = -1
    val VERBOSE = android.util.Log.VERBOSE
    val DEBUG = android.util.Log.DEBUG
    val INFO = android.util.Log.INFO
    val WARN = android.util.Log.WARN
    val ERROR = android.util.Log.ERROR
    val ASSERT = android.util.Log.ASSERT

    // Stores the beginning of the LogNode topology.
    var logNode: LogNodeInterface? = null

    /**
     * Instructs the LogNode to print the log data provided. Other LogNodes can
     * be chained to the end of the LogNode as desired.
     *
     * @param priority Log level of the data being logged. Verbose, Error, etc.
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     * @param tr If an exception was thrown, this can be sent along for the logging facilities
     * to extract and print useful information.
     */
    fun println(priority: Int, tag: String, msg: String?, tr: Throwable? = null) {

        logNode?.println(priority, tag, msg, tr)

    }

    /**
     * Prints a message at VERBOSE priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     * @param tr If an exception was thrown, this can be sent along for the logging facilities
     * to extract and print useful information.
     */
    fun v(tag: String, msg: String? = null, tr: Throwable? = null) {
        println(VERBOSE, tag, msg, tr)
    }


    /**
     * Prints a message at DEBUG priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     * @param tr If an exception was thrown, this can be sent along for the logging facilities
     * to extract and print useful information.
     */
    fun d(tag: String, msg: String? = null, tr: Throwable? = null) {
        println(DEBUG, tag, msg, tr)
    }

    /**
     * Prints a message at INFO priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     * @param tr If an exception was thrown, this can be sent along for the logging facilities
     * to extract and print useful information.
     */
    fun i(tag: String, msg: String, tr: Throwable? = null) {
        println(INFO, tag, msg, tr)
    }

    /**
     * Prints a message at WARN priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     * @param tr If an exception was thrown, this can be sent along for the logging facilities
     * to extract and print useful information.
     */
    fun w(tag: String, msg: String? = null, tr: Throwable? = null) {
        println(WARN, tag, msg, tr)
    }



    /**
     * Prints a message at ERROR priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     * @param tr If an exception was thrown, this can be sent along for the logging facilities
     * to extract and print useful information.
     */
    fun e(tag: String, msg: String, tr: Throwable? = null) {
        println(ERROR, tag, msg, tr)
    }

    /**
     * Prints a message at ASSERT priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     * @param tr If an exception was thrown, this can be sent along for the logging facilities
     * to extract and print useful information.
     */
    fun wtf(tag: String, msg: String? = null, tr: Throwable? = null) {
        println(ASSERT, tag, msg, tr)
    }


}
