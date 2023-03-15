package com.getyourguide.uitestglaze.loadingviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.getyourguide.uitestglaze.R
import com.getyourguide.uitestglaze.databinding.FragmentLoadingViewsBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingViewsFragment : Fragment(R.layout.fragment_loading_views) {

    private var _binding: FragmentLoadingViewsBinding? = null
    private val binding: FragmentLoadingViewsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingViewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.swipeRefresh.setOnRefreshListener {
            startPullToRefreshDelay()
        }
        binding.pullToRefreshButton.setOnClickListener {
            binding.swipeRefresh.isRefreshing = true
            startPullToRefreshDelay()
        }
        binding.loadingViewButton.setOnClickListener {
            binding.progressBarView.visibility = View.VISIBLE
            viewLifecycleOwner.lifecycleScope.launch {
                delay(5_000)
                binding.progressBarView.visibility = View.GONE
            }
        }
    }

    private fun startPullToRefreshDelay() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(5_000)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
