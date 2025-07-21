package com.kai.scoreanalyzer


// 工具类：根据分数返回对应等级
object GradeUtil {
    // 接收分数参数，判断并返回等级字符串
    fun getGrade(score: Int): String = when {
        score >= 90 -> "A"
        score >= 80 -> "B"
        score >= 70 -> "C"
        score >= 60 -> "D"
        else -> "F"
    }
}
