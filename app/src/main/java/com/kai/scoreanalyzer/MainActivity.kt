package com.kai.scoreanalyzer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

// 主界面：输入姓名和成绩，导航到结果、列表和查询页面
class MainActivity : ComponentActivity() {

    private lateinit var nameInput: EditText      // 姓名输入框
    private lateinit var scoreInput: EditText     // 成绩输入框
    private lateinit var buttonSubmit: Button     // 提交成绩按钮
    private lateinit var buttonViewList: Button   // 查看历史成绩按钮
    private lateinit var buttonGoQuery: Button    // 查询成绩按钮
    private lateinit var textViewLastScore: TextView // 上次成绩显示

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)    // 设置布局文件

        // 初始化视图引用
        nameInput = findViewById(R.id.editTextName)
        scoreInput = findViewById(R.id.editTextScore)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        buttonViewList = findViewById(R.id.buttonViewList)
        buttonGoQuery = findViewById(R.id.buttonGoQuery)
        textViewLastScore = findViewById(R.id.textViewLastScore)

        // 点击“提交成绩”后执行校验并跳转到结果页面
        buttonSubmit.setOnClickListener {
            val name = nameInput.text.toString()
            val scoreText = scoreInput.text.toString()
            val resultText = ScoreService.processScore(name, scoreText, this)

            // 如果任一输入框为空，提示用户填写
            if (name.isEmpty() || scoreText.isEmpty()) {
                    Toast.makeText(this, "请填写姓名和成绩", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
            }
            // 转成整数（已验证过不为空）
            //val score = scoreText.toInt()
            if (resultText != null) {
                val detail = ScoreService.generateDetailMap(
                    name,
                    InputValidator.parseScore(scoreText)!!
                )
                Intent(this, ResultActivity::class.java).apply {
                    putExtra("name", detail["name"])
                    putExtra("score", detail["score"])
                    putExtra("grade", detail["grade"])
                    startActivity(this)
                }
            }
        }

        // 点击“查看历史成绩”后跳转到列表页面
        buttonViewList.setOnClickListener {
            startActivity(Intent(this, ScoreListActivity::class.java))
        }

        // 点击“查询成绩”后跳转到查询页面
        buttonGoQuery.setOnClickListener {
            startActivity(Intent(this, QueryActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        showLastScore()  // 显示上次保存的成绩
    }

    // 从 SharedPreferences 中读取并显示上次成绩
    private fun showLastScore() {
        val pref = getSharedPreferences("score_pref", MODE_PRIVATE)
        val lastName = pref.getString("name", null)
        val lastScore = pref.getString("score", null)
        val lastGrade = pref.getString("grade", null)
        textViewLastScore.text = if (
            lastName != null && lastScore != null && lastGrade != null
        ) {
            "上次成绩：\n姓名: $lastName\n成绩: $lastScore\n等级: $lastGrade"
        } else {
            "上次成绩：暂无"
        }
    }
}
