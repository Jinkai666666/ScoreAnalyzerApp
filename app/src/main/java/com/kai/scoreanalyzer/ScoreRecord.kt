package com.kai.scoreanalyzer

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kai.scoreanalyzer.R

// 实体类，表示数据库中的一条成绩记录
@Entity(tableName = "score_table")
data class ScoreRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 主键，自增
    val name: String,      // 学生姓名
    val score: Int,        // 学生成绩
    val grade: String      // 成绩等级（例如 A、B、C）
)
