package com.kai.scoreanalyzer

// 校验姓名和成绩输入
object InputValidator {
    // 判断姓名不为空也不全是空白
    fun isValidName(name: String): Boolean = name.isNotBlank()

    // 将成绩字符串转换为整数，转换失败返回 null
    fun parseScore(scoreText: String): Int? = scoreText.toIntOrNull()
}
