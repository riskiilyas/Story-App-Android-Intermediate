package com.riskee.livestorybyriski.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.riskee.livestorybyriski.data.response.Story

@Database(entities = [Story::class, RemoteKeys::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}