package com.getyourguide.uitestglaze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.getyourguide.uitestglaze.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.loadingViewsButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToLoadingViewsFragment())
        }
        binding.inputButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToInputViewsFragment())
        }
        binding.listsButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToListViewFragment())
        }
        binding.jetpackComposeButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToJetpackComposeViewsFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
