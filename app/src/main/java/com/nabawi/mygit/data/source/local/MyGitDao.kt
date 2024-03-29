package com.nabawi.mygit.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nabawi.mygit.data.model.UserEntity

@Dao
interface MyGitDao {
    @Insert
    suspend fun addToFavorite(userEntity: UserEntity)

    @Query("SELECT * FROM user_entity")
    fun getFavoriteUser(): LiveData<List<UserEntity>>

    @Query("SELECT count(*) FROM user_entity WHERE user_entity.id = :id")
    suspend fun checkUser(id: Int): Int

    @Query("DELETE FROM user_entity WHERE user_entity.id = :id")
    suspend fun removeFromFavorite(id : Int): Int
}