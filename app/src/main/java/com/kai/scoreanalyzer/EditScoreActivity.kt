package com.kai.scoreanalyzer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// 编辑成绩界面，用户可修改后保存到数据库
class EditScoreActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextScore: EditText
    private lateinit var buttonSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_score)

        // 绑定界面控件
        editTextName = findViewById(R.id.editTextName)
        editTextScore = findViewById(R.id.editTextScore)
        buttonSave = findViewById(R.id.buttonSave)

        // 获取上一个页面传来的参数
        val id = intent.getIntExtra("score_id", -1)
        val name = intent.getStringExtra("score_name") ?: ""
        val score = intent.getIntExtra("score_score", 0)

        // 参数校验，不合法则关闭页面
        if (id < 0 || name.isEmpty()) {
            finish()
            return
        }

        // 显示原始数据
        editTextName.setText(name)
        editTextScore.setText(score.toString())

        // 点击保存时重新计算等级并更新数据库
        buttonSave.setOnClickListener {
            val newName = editTextName.text.toString()
            val newScore = editTextScore.text.toString().toIntOrNull() ?: 0
            val newGrade = GradeUtil.getGrade(newScore)

            CoroutineScope(Dispatchers.IO).launch {
                val db = ScoreDatabase.getDatabase(applicationContext)
                db.scoreDao().updateScore(ScoreRecord(id, newName, newScore, newGrade))

                // 更新完成后回到主线程关闭页面
                withContext(Dispatchers.Main) {
                    finish()
                }
            }
        }
    }
}
