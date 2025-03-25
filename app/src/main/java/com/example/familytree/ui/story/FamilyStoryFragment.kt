package com.example.familytree.ui.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familytree.data.model.StoryType
import com.example.familytree.databinding.FragmentFamilyStoryBinding

class FamilyStoryFragment : Fragment() {

    private var _binding: FragmentFamilyStoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FamilyStoryViewModel by viewModels {
        FamilyStoryViewModelFactory(requireActivity().application)
    }

    private lateinit var adapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamilyStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupStoryTypeChips()
        observeViewModel()

        // 设置添加按钮
        binding.fabAdd.setOnClickListener {
            // TODO: 添加新故事的实现
        }
    }

    private fun setupRecyclerView() {
        adapter = StoryAdapter { storyUiModel ->
            // TODO: 实现故事详情页导航，使用 storyUiModel.id
        }

        binding.recyclerViewStories.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewStories.adapter = adapter
    }

    private fun setupStoryTypeChips() {
        binding.chipAll.setOnClickListener {
            viewModel.setStoryTypeFilter(null)
        }

        binding.chipFamily.setOnClickListener {
            viewModel.setStoryTypeFilter(StoryType.FAMILY)
        }

        binding.chipIndividual.setOnClickListener {
            viewModel.setStoryTypeFilter(StoryType.BIOGRAPHY)
        }

        binding.chipEvent.setOnClickListener {
            viewModel.setStoryTypeFilter(StoryType.EVENT)
        }

        binding.chipHistory.setOnClickListener {
            viewModel.setStoryTypeFilter(StoryType.HISTORY)
        }
    }

    private fun observeViewModel() {
        // 观察筛选后的故事列表
        viewModel.filteredStories.observe(viewLifecycleOwner) { storyUiModels ->
            adapter.submitList(storyUiModels)

            // 更新统计信息
            binding.textStoryCount.text = "共 ${storyUiModels.size} 个故事"

            if (storyUiModels.isEmpty()) {
                binding.recyclerViewStories.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
            } else {
                binding.recyclerViewStories.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 