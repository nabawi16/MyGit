package com.nabawi.mygit.ui.favorite

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.nabawi.mygit.data.model.UserEntity
import com.nabawi.mygit.databinding.FragmentFavoriteBinding
import com.nabawi.mygit.ui.detailuser.DetailUserActivity
import com.nabawi.mygit.ui.main.UsersAdapter

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersAdapter = UsersAdapter()
        usersAdapter.notifyDataSetChanged()
        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        usersAdapter.setOnItemClickCallback(object: UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity) {
                Intent(requireContext(), DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.username)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR_URL, data.avatarUrl)
                    startActivity(it)
                }
            }

        })
        binding.apply {
            rvUsers.setHasFixedSize(true)
            rvUsers.layoutManager = LinearLayoutManager(requireContext())
            rvUsers.adapter = usersAdapter

            favoriteViewModel.getFavoriteUser()?.observe(requireActivity()) {
                if (it != null) {
                    usersAdapter.submitList(it)
                    if (it.isEmpty()) {
                        tvNoData.visibility = View.VISIBLE
                    } else {
                        tvNoData.visibility = View.GONE
                    }
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}