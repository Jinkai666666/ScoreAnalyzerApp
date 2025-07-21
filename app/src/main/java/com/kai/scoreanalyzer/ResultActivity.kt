package com.kai.scoreanalyzer

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.kai.scoreanalyzer.R

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// 结果界面：显示成绩并保存记录
class ResultActivity : AppCompatActivity() {

    private var finished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // 获取传入的数据
        val name = intent.getStringExtra("name") ?: "未知"
        val score = intent.getStringExtra("score") ?: "0"
        val grade = intent.getStringExtra("grade") ?: "未知"

        // 绑定视图
        val textViewName = findViewById<TextView>(R.id.textViewName)
        val textViewScore = findViewById<TextView>(R.id.textViewScore)
        val textViewGrade = findViewById<TextView>(R.id.textViewGrade)
        val buttonBack = findViewById<Button>(R.id.buttonBack)

        // 显示结果
        textViewName.text = "姓名: $name"
        textViewScore.text = "成绩: $score"
        textViewGrade.text = "等级: $grade"

        // 保存记录并关闭页面
        saveRecord(name, score.toIntOrNull() ?: 0, grade)

        // 返回按钮点击时关闭页面
        buttonBack.setOnClickListener {
            finishPage()
        }
    }

    // 在后台插入记录并更新上次成绩，完成后关闭页面
    private fun saveRecord(name: String, score: Int, grade: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val record = ScoreRecord(name = name, score = score, grade = grade)
            val db = ScoreDatabase.getDatabase(applicationContext)
            db.scoreDao().insertScore(record)
            updateLastScore(name, score.toString(), grade)
            withContext(Dispatchers.Main) {
                finishPage()
            }
        }
    }

    // 更新 SharedPreferences 保存上次成绩
    private fun updateLastScore(name: String, score: String, grade: String) {
        val pref = getSharedPreferences("score_pref", MODE_PRIVATE)
        pref.edit().apply {
            putString("name", name)
            putString("score", score)
            putString("grade", grade)
            apply()
        }
    }

    // 安全关闭页面，防止重复调用
    private fun finishPage() {
        if (!finished && !isFinishing && !isDestroyed) {
            finished = true
            finish()
        }
    }
}
