package com.inventiv.multipaysdk.util

import android.text.TextUtils
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.min

internal object Logger {
    private const val LOG_TAG = "MultiPaySDK"
    private val methodCount = 1
    private val methodOffset = 0
    private val showThreadInfo = false
    private var enabled = false
    private const val DEBUG = 3
    private const val ERROR = 6
    private const val ASSERT = 7
    private const val INFO = 4
    private const val VERBOSE = 2
    private const val WARN = 5
    private const val CHUNK_SIZE = 4000
    private const val JSON_INDENT = 4
    private const val MIN_STACK_OFFSET = 3
    private const val TOP_RIGHT_CORNER = '╗'
    private const val TOP_LEFT_CORNER = '╔'
    private const val BOTTOM_LEFT_CORNER = '╚'
    private val BOTTOM_RIGHT_CORNER = '╝'
    private const val MIDDLE_CORNER = '╟'
    private const val HORIZONTAL_DOUBLE_LINE = '║'
    private const val DOUBLE_DIVIDER = "════════════════════════════════════════════"
    private const val SINGLE_DIVIDER = "────────────────────────────────────────────"
    private const val TOP_BORDER = "$TOP_LEFT_CORNER$DOUBLE_DIVIDER$DOUBLE_DIVIDER"
    private const val BOTTOM_BORDER = "$BOTTOM_LEFT_CORNER$DOUBLE_DIVIDER$DOUBLE_DIVIDER"
    private const val MIDDLE_BORDER = "$MIDDLE_CORNER$SINGLE_DIVIDER$SINGLE_DIVIDER"
    private val localTag: ThreadLocal<String?> = ThreadLocal()
    private val localMethodCount: ThreadLocal<Int?> = ThreadLocal()

    fun t(tag: String?, methodCount: Int): Logger? {
        if (tag != null) {
            localTag.set(tag)
        }
        localMethodCount.set(methodCount)
        return this
    }

    fun logging(enabled: Boolean) {
        Logger.enabled = enabled
    }

    fun d(message: String, vararg args: Any?) {
        log(DEBUG, message, args)
    }

    fun e(message: String?, vararg args: Any?) {
        this.e(null as Throwable?, message, *args)
    }

    fun e(throwable: Throwable?, message: String?, vararg args: Any?) {
        var message = message
        if (throwable != null && message != null) {
            message = "$message : $throwable"
        }
        if (throwable != null && message == null) {
            message = throwable.toString()
        }
        if (message == null) {
            message = "No message/exception is set"
        }
        log(ERROR, message, args)
    }

    fun w(message: String, vararg args: Any?) {
        log(WARN, message, args)
    }

    fun i(message: String, vararg args: Any?) {
        log(INFO, message, args)
    }

    fun v(message: String, vararg args: Any?) {
        log(VERBOSE, message, args)
    }

    fun wtf(message: String, vararg args: Any?) {
        log(ASSERT, message, args)
    }

    fun json(json: String) {
        if (json.isEmpty()) {
            d("Empty/Null json content")
        } else {
            try {
                val message: String
                if (json.startsWith("{")) {
                    val jsonObject = JSONObject(json)
                    message = jsonObject.toString(4)
                    d(message)
                    return
                }
                if (json.startsWith("[")) {
                    val jsonArray = JSONArray(json)
                    message = jsonArray.toString(4)
                    d(message)
                }
            } catch (var4: JSONException) {
                this.e(
                    """
                    ${var4.cause!!.message}
                    $json
                    """.trimIndent()
                )
            }
        }
    }

    @Synchronized
    private fun log(logType: Int, msg: String, vararg args: Any) {
        if (enabled) {
            val tag = getTag()
            val message = createMessage(msg, *args)
            val methodCount = getMethodCount()
            logTopBorder(logType, tag)
            logHeaderContent(logType, tag, methodCount)
            val bytes = message.toByteArray()
            val length = bytes.size
            if (length <= CHUNK_SIZE) {
                if (methodCount > 0) {
                    logDivider(logType, tag)
                }
                logContent(logType, tag, message)
                logBottomBorder(logType, tag)
            } else {
                if (methodCount > 0) {
                    logDivider(logType, tag)
                }
                var i = 0
                while (i < length) {
                    val count = min(length - i, CHUNK_SIZE)
                    logContent(logType, tag, String(bytes, i, count))
                    i += CHUNK_SIZE
                }
                logBottomBorder(logType, tag)
            }
        }
    }

    private fun logTopBorder(logType: Int, tag: String) {
        logChunk(
            logType,
            tag,
            TOP_BORDER
        )
    }

    private fun logHeaderContent(logType: Int, tag: String, methodCount: Int) {
        var methodCount = methodCount
        val trace = Thread.currentThread().stackTrace
        var level = ""
        val stackOffset = getStackOffset(trace) + 0
        if (methodCount + stackOffset > trace.size) {
            methodCount = trace.size - stackOffset - 1
        }
        for (i in methodCount downTo 1) {
            val stackIndex = i + stackOffset
            if (stackIndex < trace.size) {
                val builder = StringBuilder()
                builder.append("$HORIZONTAL_DOUBLE_LINE ").append(level)
                    .append(getSimpleClassName(trace[stackIndex].className)).append(".").append(
                        trace[stackIndex].methodName
                    )
                level = "$level   "
                logChunk(logType, tag, builder.toString())
            }
        }
    }

    private fun logBottomBorder(logType: Int, tag: String) {
        logChunk(
            logType,
            tag,
            BOTTOM_BORDER
        )
    }

    private fun logDivider(logType: Int, tag: String) {
        logChunk(
            logType,
            tag,
            MIDDLE_BORDER
        )
    }

    private fun logContent(logType: Int, tag: String, chunk: String) {
        val lines = chunk.split(System.getProperty("line.separator") ?: "\\n")
        val var6 = lines.size
        for (var7 in 0 until var6) {
            val line = lines[var7]
            logChunk(logType, tag, "$HORIZONTAL_DOUBLE_LINE $line")
        }
    }

    private fun logChunk(logType: Int, tag: String, chunk: String) {
        val finalTag = formatTag(tag)
        when (logType) {
            VERBOSE -> Log.v(finalTag, chunk)
            DEBUG -> Log.d(finalTag, chunk)
            INFO -> Log.i(finalTag, chunk)
            WARN -> Log.w(finalTag, chunk)
            ERROR -> Log.e(finalTag, chunk)
            ASSERT -> Log.wtf(finalTag, chunk)
            else -> Log.d(finalTag, chunk)
        }
    }

    private fun getSimpleClassName(name: String): String? {
        val lastIndex = name.lastIndexOf(".")
        return name.substring(lastIndex + 1)
    }

    private fun formatTag(tag: String): String {
        return if (!TextUtils.isEmpty(tag) && !TextUtils.equals(
                "MultiPaySDK",
                tag
            )
        ) "MultiPaySDK-$tag" else "MultiPaySDK"
    }

    private fun getTag(): String {
        val tag = localTag.get()
        return if (tag != null) {
            localTag.remove()
            tag
        } else {
            "MultiPaySDK"
        }
    }

    private fun createMessage(message: String, vararg args: Any): String {
        return if (args.isEmpty()) {
            message
        } else {
            String.format(message, args)
        }
    }

    private fun getMethodCount(): Int {
        val count = localMethodCount.get()
        var result = 1
        if (count != null) {
            localMethodCount.remove()
            result = count
        }
        return if (result < 0) {
            throw IllegalStateException("methodCount cannot be negative")
        } else {
            result
        }
    }

    private fun getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = 3
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (name != Logger::class.java.name) {
                --i
                return i
            }
            ++i
        }
        return -1
    }
}