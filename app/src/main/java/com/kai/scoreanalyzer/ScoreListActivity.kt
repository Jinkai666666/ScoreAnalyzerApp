package com.kai.scoreanalyzer

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.kai.scoreanalyzer.R

// 历史成绩列表界面，显示所有保存的成绩
class ScoreListActivity : AppCompatActivity() {

    private lateinit var adapter: ScoreAdapter    // 成绩适配器，用于显示数据库中的成绩数据

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 隐藏应用栏图标和 Logo（界面简洁化）
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(false)
            setLogo(null)
            setDisplayUseLogoEnabled(false)
        }

        // 设置页面布局
        setContentView(R.layout.activity_score_list)

        // 初始化 RecyclerView（用于列表展示成绩）
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewScores)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 初始化适配器并绑定数据库
        adapter = ScoreAdapter(mutableListOf(), ScoreDatabase.getDatabase(this))
        recyclerView.adapter = adapter

        // 添加每项之间的分隔线
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        // 首次进入页面时加载成绩数据
        loadScores()
    }

    override fun onResume() {
        super.onResume()
        // 每次返回此页面时重新加载数据，确保数据更新
        loadScores()
    }

    // 从数据库读取成绩并更新 RecyclerView
    private fun loadScores() {
        lifecycleScope.launch(Dispatchers.IO) {
            // 在 IO 线程中读取数据库成绩记录
            val records = ScoreDatabase
                .getDatabase(applicationContext)
                .scoreDao()
                .getAllScores()

            withContext(Dispatchers.Main) {
                // 主线程中更新 RecyclerView 数据
                adapter.updateList(records.toMutableList())  //调用自定义函数更新适配器内容

                // 查找页面上的“空数据提示文字”控件
                val textViewEmpty = findViewById<TextView>(R.id.textViewEmpty)

                // 如果没有记录，显示提示；否则隐藏提示
                if (records.isEmpty()) {
                    textViewEmpty.visibility = View.VISIBLE
                } else {
                    textViewEmpty.visibility = View.GONE
                }
            }
        }
    }
}
