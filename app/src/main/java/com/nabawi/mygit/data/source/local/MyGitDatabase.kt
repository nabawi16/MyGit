package com.nabawi.mygit.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nabawi.mygit.data.model.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1
)
abstract class MyGitDatabase: RoomDatabase(){
    companion object {
        private var INSTANCE : MyGitDatabase? = null

        fun getDatabase(context: Context): MyGitDatabase?{
            if(INSTANCE == null){
                synchronized(MyGitDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, MyGitDatabase::class.java, "mygit_db").build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun favoriteUserDao(): MyGitDao
}