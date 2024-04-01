package com.griffin.core.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


object TimeUtils {

    const val FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
    const val FORMAT_HH_MM_SS = "HH:mm:ss"
    const val FORMAT_HH_MM = "HH:mm"

    private val SDF_THREAD_LOCAL: ThreadLocal<MutableMap<String, SimpleDateFormat>> =
        object : ThreadLocal<MutableMap<String, SimpleDateFormat>>() {
            override fun initialValue(): MutableMap<String, SimpleDateFormat> {
                return mutableMapOf()
            }
        }

    /**
     * 获取时间格式
     */
    private fun getDefFormat(format: String = FORMAT_YYYY_MM_DD_HH_MM_SS): SimpleDateFormat {
        val sdfMap: MutableMap<String, SimpleDateFormat>? = SDF_THREAD_LOCAL.get()
        var simpleDateFormat = sdfMap?.get(format)
        if (simpleDateFormat == null) {
            simpleDateFormat = SimpleDateFormat(format, Locale.CHINA)
            sdfMap?.put(format, simpleDateFormat)
        }
        return simpleDateFormat
    }

    /**
     * 将String类型的时间转换为指定的格式，输出String类型的时间
     *
     * @param pattern 输入时间格式
     * @param format 输出时间格式
     */
    fun String.toFormatTime(pattern: String = FORMAT_YYYY_MM_DD_HH_MM_SS,format: String = FORMAT_YYYY_MM_DD_HH_MM_SS): String {
        // 先将String转换为Date，然后再将Date转换为String
        val defFormat = getDefFormat(pattern)
        return defFormat.parse(this)?.toFormatTime(format) ?: ""
    }

    /**
     * 将Date类型的时间转换为指定的格式，输出String类型的时间
     *
     * @param format 输出时间格式
     */
    fun Date.toFormatTime(format: String = FORMAT_YYYY_MM_DD_HH_MM_SS): String {
        val defFormat = getDefFormat(format)
        return defFormat.format(this)
    }

    /**
     * 将时间转换为友好的时间
     */
    fun String.toFriendlyTime(pattern: String = FORMAT_YYYY_MM_DD_HH_MM_SS): String {
        val date = getDefFormat(pattern).parse(this) ?: return ""
        // 获取Calendar
        val calendar = Calendar.getInstance()
        calendar.time = date
        val now = System.currentTimeMillis()
        val span: Long = now - calendar.timeInMillis
        return if (span < 0L) {
            String.format("%tc", calendar.timeInMillis)
        } else if (span < 1000L) {
            "刚刚"
        } else if (span < 60000L) {
            String.format(Locale.getDefault(), "%d秒前", span / 1000L)
        } else if (span < 3600000L) {
            String.format(Locale.getDefault(), "%d分钟前", span / 60000L)
        } else {
            val wee: Long = getWeeOfToday()
            if (calendar.timeInMillis >= wee) {
                String.format("今天%tR", calendar.timeInMillis)
            } else {
                if (calendar.timeInMillis >= wee - 86400000L) java.lang.String.format(
                    "昨天%tR",
                    calendar.timeInMillis
                ) else String.format("%tF", calendar.timeInMillis)
            }
        }
    }

    private fun getWeeOfToday(): Long {
        val cal = Calendar.getInstance()
        cal[11] = 0
        cal[13] = 0
        cal[12] = 0
        cal[14] = 0
        return cal.getTimeInMillis()
    }

}