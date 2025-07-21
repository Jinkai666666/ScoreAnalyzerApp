package com.kai.scoreanalyzer


import androidx.room.*

@Dao
interface ScoreDao {

    @Insert
    suspend fun insertScore(scoreRecord: ScoreRecord)

    @Update
    suspend fun updateScore(scoreRecord: ScoreRecord)


    @Delete
    suspend fun deleteScore(scoreRecord: ScoreRecord)


    @Query("DELETE FROM score_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM score_table ORDER BY id DESC")
    suspend fun getAllScores(): List<ScoreRecord>

    @Query("SELECT * FROM score_table WHERE name = :name LIMIT 1")
    suspend fun findByName(name: String): ScoreRecord?

    @Query("DELETE FROM score_table")
    suspend fun deleteAll()


}

