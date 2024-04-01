package com.riskee.livestorybyriski.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.riskee.livestorybyriski.data.response.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: List<Story>)

    @Query("SELECT * FROM stories")
    fun getAllQuote(): PagingSource<Int, Story>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}