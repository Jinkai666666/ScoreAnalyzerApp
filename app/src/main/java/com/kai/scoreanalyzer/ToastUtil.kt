package com.kai.scoreanalyzer

import android.content.Context
import android.widget.Toast

// 工具类：统一弹出短时 Toast 提示
object ToastUtil {
    // 在指定 Context 中显示一条短时提示
    fun show(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
