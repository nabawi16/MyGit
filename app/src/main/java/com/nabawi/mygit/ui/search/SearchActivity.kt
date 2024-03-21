package com.nabawi.mygit.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nabawi.mygit.R
import com.nabawi.mygit.data.model.UserEntity
import com.nabawi.mygit.databinding.ActivitySearchBinding
import com.nabawi.mygit.ui.detailuser.DetailUserActivity
import com.nabawi.mygit.ui.main.UsersAdapter
import com.nabawi.mygit.utils.DataMapper

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchResultViewModel: SearchResultViewModel
    private lateinit var searchResultAdapter: UsersAdapter

    private var etQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }

        initializeRecyclerView()
        initializeViewModel()
        initializeSearchView()
    }

    private fun initializeRecyclerView() {
        searchResultAdapter = UsersAdapter()

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            setHasFixedSize(true)
            adapter = searchResultAdapter
        }
    }

    private fun initializeViewModel() {
        searchResultViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[SearchResultViewModel::class.java]

        searchResultViewModel.apply {
            isLoading.observe(this@SearchActivity) { showLoading(it) }
            onFailure.observe(this@SearchActivity) { onFailure(it) }
            totalUserFound.observe(this@SearchActivity) { totalUserCheck(it) }
            getSearchUsers().observe(this@SearchActivity) {
                searchResultAdapter.submitList(DataMapper.mapResponsesToEntities(it))
            }
        }
    }

    private fun initializeSearchView() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = (menuInflater.inflate(R.menu.search_menu, null) as? SearchView)

        searchView?.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = resources.getString(R.string.search_hint)
            setIconifiedByDefault(false)
            onActionViewExpanded()

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    etQuery = query
                    searchUser()
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    etQuery = newText
                    searchUser()
                    return false
                }
            })
        }

        supportActionBar?.apply {
            setDisplayShowCustomEnabled(true)
            customView = searchView
        }
    }

    private fun navigateToDetailUser(userEntity: UserEntity) {
        Intent(this@SearchActivity, DetailUserActivity::class.java).apply {
            putExtra(DetailUserActivity.EXTRA_USERNAME, userEntity.username)
            putExtra(DetailUserActivity.EXTRA_ID, userEntity.id)
            putExtra(DetailUserActivity.EXTRA_AVATAR_URL, userEntity.avatarUrl)
            putExtra(DetailUserActivity.EXTRA_HTML_URL, userEntity.htmlUrl)
            startActivity(this)
        }
    }

    private fun searchUser() {
        searchResultViewModel.setSearchUser(etQuery)
    }

    private fun totalUserCheck(userFound: Int?) {
        binding.tvNoData.visibility = if (userFound == 0) View.VISIBLE else View.GONE
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun onFailure(fail: Boolean) {
        binding.tvOnFailMsg.visibility = if (fail) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
