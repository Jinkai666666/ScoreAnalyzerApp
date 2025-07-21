package com.kai.scoreanalyzer


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kai.scoreanalyzer.R

// Room 数据库：定义实体和版本号
@Database(entities = [ScoreRecord::class], version = 2, exportSchema = false)
abstract class ScoreDatabase : RoomDatabase() {

    // 获取 DAO 接口
    abstract fun scoreDao(): ScoreDao

    companion object {
        @Volatile
        private var INSTANCE: ScoreDatabase? = null

        // 获取数据库单例，保证线程安全
        fun getDatabase(context: Context): ScoreDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScoreDatabase::class.java,
                    "score_database"
                )
                    // 版本冲突时销毁旧库并重建
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
