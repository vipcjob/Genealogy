package com.fcz.genealogy.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期工具类，用于格式化日期
 */
object DateUtils {
    
    private val fullDateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)
    private val yearFormat = SimpleDateFormat("yyyy", Locale.CHINA)
    private val monthDayFormat = SimpleDateFormat("MM月dd日", Locale.CHINA)
    
    /**
     * 格式化完整日期，如：2023年1月1日
     */
    fun formatDate(date: Date): String {
        return fullDateFormat.format(date)
    }
    
    /**
     * 只格式化年份，如：2023
     */
    fun formatYear(date: Date): String {
        return yearFormat.format(date)
    }
    
    /**
     * 只格式化月日，如：1月1日
     */
    fun formatMonthDay(date: Date): String {
        return monthDayFormat.format(date)
    }
    
    /**
     * 计算两个日期之间的差值（天数）
     */
    fun daysBetween(start: Date, end: Date): Int {
        val diff = end.time - start.time
        return (diff / (24 * 60 * 60 * 1000)).toInt()
    }
    
    /**
     * 计算年龄
     */
    fun calculateAge(birthDate: Date): Int {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        
        calendar.time = birthDate
        val birthYear = calendar.get(Calendar.YEAR)
        val birthMonth = calendar.get(Calendar.MONTH)
        val birthDay = calendar.get(Calendar.DAY_OF_MONTH)
        
        var age = currentYear - birthYear
        
        // 如果当前月份小于出生月份，或者月份相同但当前日期小于出生日期，则年龄减1
        if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
            age--
        }
        
        return age
    }
    
    /**
     * 判断日期是否在指定时间段内
     */
    fun isDateInRange(date: Date, start: Date, end: Date): Boolean {
        return date.after(start) && date.before(end)
    }
} 