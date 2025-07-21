package com.kai.scoreanalyzer

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kai.scoreanalyzer.R

import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// 成绩列表适配器，用于将成绩数据绑定到 RecyclerView 上
class ScoreAdapter(
    private val scoreList: MutableList<ScoreRecord>, // 成绩数据列表
    private val db: ScoreDatabase                     // Room 数据库实例
) : RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    // 每一项的 ViewHolder，持有当前项的控件引用
    inner class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.textViewName)         // 姓名
        val tvScore: TextView = itemView.findViewById(R.id.textViewScore)       // 成绩
        val tvGrade: TextView = itemView.findViewById(R.id.textViewGrade)       // 等级
        val buttonCleanAll: Button = itemView.findViewById(R.id.cleanAll)       // 清空按钮（仅最后一项显示）
    }

    // 创建列表项的布局
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_score, parent, false)
        return ScoreViewHolder(view)
    }

    // 绑定数据到控件上
    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val record = scoreList[position]
        holder.tvName.text = "姓名：${record.name}"
        holder.tvScore.text = "成绩：${record.score}"
        holder.tvGrade.text = "等级：${record.grade}"

        // 最后一项显示“清空成绩”按钮
        if (position == scoreList.size - 1) {
            holder.buttonCleanAll.visibility = View.VISIBLE
        } else {
            holder.buttonCleanAll.visibility = View.GONE
        }

        // 点击列表项跳转到编辑页面
        holder.itemView.setOnClickListener {
            Intent(holder.itemView.context, EditScoreActivity::class.java).apply {
                putExtra("score_id", record.id)
                putExtra("score_name", record.name)
                putExtra("score_score", record.score)
                putExtra("score_grade", record.grade)
                holder.itemView.context.startActivity(this)
            }
        }

        // 点击“清空全部”按钮时执行清除操作
        holder.buttonCleanAll.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("确认清空")
                .setMessage("是否确定要清空所有成绩记录？此操作无法恢复。")
                .setPositiveButton("清空") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        db.scoreDao().deleteAll()
                        withContext(Dispatchers.Main) {
                            scoreList.clear()
                            notifyDataSetChanged()
                        }
                    }
                }
                .setNegativeButton("取消", null)
                .show()
        }

        // 长按删除当前项
        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("确认删除")
                .setMessage("是否确定删除该成绩记录？")
                .setPositiveButton("删除") { _, _ ->

                    deleteRecord(position)
                }
                .setNegativeButton("取消", null)
                .show()
            true
        }
    }

    // 获取列表项数量
    override fun getItemCount(): Int = scoreList.size

    // 用于外部更新数据列表的函数
    fun updateList(newList: MutableList<ScoreRecord>) {
        scoreList.clear()              // 清除旧数据
        scoreList.addAll(newList)     // 添加新数据
        notifyDataSetChanged()        // 通知 RecyclerView 刷新
    }

    // 删除指定位置的记录
    private fun deleteRecord(position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            db.scoreDao().deleteScore(scoreList[position])
            withContext(Dispatchers.Main) {
                scoreList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, scoreList.size)
            }
        }
    }
}
