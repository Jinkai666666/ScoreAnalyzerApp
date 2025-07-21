package com.kai.scoreanalyzer

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kai.scoreanalyzer.R

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// 查询界面，根据输入姓名查询数据库并显示结果
class QueryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_query)

        // 绑定控件
        val nameInput = findViewById<EditText>(R.id.editTextName)
        val buttonQuery = findViewById<Button>(R.id.buttonQuery)
        val resultView = findViewById<TextView>(R.id.textViewResult)
        val buttonEdit = findViewById<Button>(R.id.buttonEdit)

        // 查询按钮点击事件
        buttonQuery.setOnClickListener {
            val name = nameInput.text.toString().trim()
            if (name.isEmpty()) {
                resultView.text = "请输入学生姓名"
                return@setOnClickListener
            }

            // 后台查询数据库
            lifecycleScope.launch(Dispatchers.IO) {
                val record = ScoreDatabase
                    .getDatabase(this@QueryActivity)
                    .scoreDao()
                    .findByName(name)

                // 回到主线程更新 UI
                withContext(Dispatchers.Main) {
                    if (record != null) {
                        resultView.text = "姓名: ${record.name}\n分数: ${record.score}\n等级: ${record.grade}"
                        buttonEdit.visibility = View.VISIBLE
                        buttonEdit.setOnClickListener {
                            Intent(this@QueryActivity, EditScoreActivity::class.java).apply {
                                putExtra("score_id", record.id)
                                putExtra("score_name", record.name)
                                putExtra("score_score", record.score)
                                putExtra("score_grade", record.grade)
                                startActivity(this)
                            }
                        }
                    } else {
                        resultView.text = "未找到该学生成绩"
                        buttonEdit.visibility = View.GONE
                    }
                }
            }
        }
    }
}
