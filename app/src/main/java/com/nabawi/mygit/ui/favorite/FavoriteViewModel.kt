package com.nabawi.mygit.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.nabawi.mygit.data.model.UserEntity
import com.nabawi.mygit.data.source.remote.local.MyGitDao
import com.nabawi.mygit.data.source.remote.local.MyGitDatabase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    private var dao: MyGitDao?
    private var database: MyGitDatabase? = MyGitDatabase.getDatabase(application)

    init{
        dao = database?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<UserEntity>>?{
        return dao?.getFavoriteUser()
    }
}