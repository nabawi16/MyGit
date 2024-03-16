package com.nabawi.mygit.ui.detailuser.following

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nabawi.mygit.data.source.remote.RetrofitClient
import com.nabawi.mygit.data.source.remote.responses.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    val listFollowing = MutableLiveData<ArrayList<UserResponse>>()

    private lateinit var context: Context
    fun setContext(context: Context) {
        this.context = context
    }
    fun setListFollowing(username: String) {
        RetrofitClient.apiInstance
            .getFollowing(username)
            .enqueue(object : Callback<ArrayList<UserResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<UserResponse>>,
                    response: Response<ArrayList<UserResponse>>
                ) {
                    if (response.isSuccessful) {
                        listFollowing.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserResponse>>, t: Throwable) {
                    val errorMessage = "Terjadi kesalahan: ${t.message}"
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }

            })
    }

    fun getListFollowing(): LiveData<ArrayList<UserResponse>> {
        return listFollowing
    }

}