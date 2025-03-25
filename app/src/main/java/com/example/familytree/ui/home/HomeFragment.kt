package com.example.familytree.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familytree.R
import com.example.familytree.databinding.FragmentHomeBinding
import com.example.familytree.util.SVGUtils

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory(requireActivity().application) }
    private lateinit var updatesAdapter: RecentUpdatesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        loadSvgImages()
    }

    private fun setupRecyclerView() {
        updatesAdapter = RecentUpdatesAdapter()
        binding.recyclerViewUpdates.apply {
            adapter = updatesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObservers() {
        // 家族概况数据
        viewModel.familyOverview.observe(viewLifecycleOwner) { overview ->
            binding.apply {
                textGenerationCount.text = overview.generationCount.toString()
                textMemberCount.text = getString(R.string.member_count_format, overview.totalMembers)
            }
        }

        // 最近动态数据
        viewModel.recentUpdates.observe(viewLifecycleOwner) { updates ->
            updatesAdapter.submitList(updates)
        }
    }

    private fun setupClickListeners() {
        // 功能模块点击
        binding.apply {
//            cardFamilyTree.setOnClickListener {
//                findNavController().navigate(R.id.navigation_family_tree)
//            }
//
//            cardFamilyStory.setOnClickListener {
//                findNavController().navigate(R.id.navigation_family_story)
//            }
//
//            cardFamilyActivity.setOnClickListener {
//                findNavController().navigate(R.id.action_to_family_activity)
//            }
//
//            cardWorshipCalendar.setOnClickListener {
//                findNavController().navigate(R.id.action_to_calendar)
//            }
//
//            // 查看全部按钮
//            textViewAll.setOnClickListener {
//                // TODO: 导航到全部动态页面
//            }
//
//            // 悬浮按钮
//            fabAdd.setOnClickListener {
//                // TODO: 显示添加选项菜单
//            }
        }
    }

    private fun loadSvgImages() {
        // 从assets加载SVG图像
        //SVGUtils.loadSvgIntoImageView(requireContext(), binding.imageFamilyEmblem, "svg/family_emblem.svg")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 