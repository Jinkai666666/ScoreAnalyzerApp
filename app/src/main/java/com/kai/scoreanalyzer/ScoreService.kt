package com.kai.scoreanalyzer

import android.content.Context

// 服务类：校验输入并生成成绩结果
object ScoreService {

    // 校验姓名和成绩格式，错误时弹 Toast 提示并返回 null
    fun processScore(name: String, scoreText: String, context: Context): String? {
        if (!InputValidator.isValidName(name)) {
            ToastUtil.show(context, "请输入姓名")
            return null
        }
        val score = InputValidator.parseScore(scoreText)
        if (score == null) {
            ToastUtil.show(context, "成绩必须是数字")
            return null
        }
        return generateResultText(name, score)
    }

    // 根据姓名和分数生成结果文本
    fun generateResultText(name: String, score: Int): String {
        val grade = GradeUtil.getGrade(score)
        return "$name 的成绩是 $score，等级为 $grade"
    }

    // 生成详情数据，返回包含姓名、成绩和等级的 Map
    fun generateDetailMap(name: String, score: Int): Map<String, String> {
        val grade = GradeUtil.getGrade(score)
        return mapOf(
            "name" to name,
            "score" to score.toString(),
            "grade" to grade
        )
    }
}
