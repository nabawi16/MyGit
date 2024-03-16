package com.nabawi.mygit.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nabawi.mygit.data.model.UserEntity
import com.nabawi.mygit.databinding.FragmentHomeBinding
import com.nabawi.mygit.ui.detailuser.DetailUserActivity
import com.nabawi.mygit.ui.main.UsersAdapter
import com.nabawi.mygit.ui.search.SearchActivity
import com.nabawi.mygit.utils.DataMapper

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersAdapter = UsersAdapter()
        homeViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(HomeViewModel::class.java)

        usersAdapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity) {
                Intent(requireContext(), DetailUserActivity::class.java).apply {
                    putExtra(DetailUserActivity.EXTRA_USERNAME, data.username)
                    putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    putExtra(DetailUserActivity.EXTRA_AVATAR_URL, data.avatarUrl)
                    putExtra(DetailUserActivity.EXTRA_HTML_URL, data.htmlUrl)
                    startActivity(this)
                }
            }
        })

        binding.apply {
            rvUsers.layoutManager = LinearLayoutManager(requireContext())
            rvUsers.setHasFixedSize(true)
            rvUsers.adapter = usersAdapter

            searchFabBtn.setOnClickListener {
                val intent = Intent(requireContext(), SearchActivity::class.java)
                startActivity(intent)
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        homeViewModel.onFailure.observe(viewLifecycleOwner) {
            onFailure(it)
        }

        homeViewModel.getListUsers().observe(viewLifecycleOwner) { users ->
            users?.let {
                onFailure(false)
                usersAdapter.setList(DataMapper.mapResponsesToEntities(it))
            }
        }
        refreshApp()
    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            homeViewModel.findUsers()
            binding.swipeToRefresh.isRefreshing = false
        }
    }


    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun onFailure(fail: Boolean) {
        binding.tvOnFailMsg.visibility = if (fail) View.VISIBLE else View.GONE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
