package com.nabawi.mygit.ui.detailuser

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nabawi.mygit.data.model.UserEntity
import com.nabawi.mygit.data.source.remote.RetrofitClient
import com.nabawi.mygit.data.source.local.MyGitDao
import com.nabawi.mygit.data.source.local.MyGitDatabase
import com.nabawi.mygit.data.source.remote.responses.DetailUserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    val user = MutableLiveData<DetailUserResponse>()

    private var dao: MyGitDao?
    private var database: MyGitDatabase? = MyGitDatabase.getDatabase(application)

    init{
        dao = database?.favoriteUserDao()
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _onFailure = MutableLiveData<Boolean>()
    val onFailure: LiveData<Boolean> = _onFailure

    fun setUserDetail(username: String) {

        _onFailure.value = false
        _isLoading.value = true

        RetrofitClient.apiInstance
            .getUserDetail(username)
            .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    _isLoading.value = false
                    _onFailure.value = false

                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    _isLoading.value = false
                    _onFailure.value = true
                    Log.d("Failure", t.message.toString())
                }

            })
    }

    fun getUserDetail(): LiveData<DetailUserResponse> {
        return user
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String, htmlUrl: String){
        CoroutineScope(Dispatchers.IO).launch {
            val user = UserEntity(
                id,
                username,
                avatarUrl,
                htmlUrl
            )
            dao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = dao?.checkUser(id)

    fun removeFromFavorite(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            dao?.removeFromFavorite(id)
        }
    }

}