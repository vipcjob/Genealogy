package com.fcz.genealogy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fcz.genealogy.R
import com.fcz.genealogy.databinding.FragmentHomeBinding
import com.fcz.genealogy.ui.activity.FamilyActivityActivity
import com.fcz.genealogy.ui.familytree.FamilyTreeActivity
import com.fcz.genealogy.ui.story.FamilyStoryActivity
import com.fcz.genealogy.util.SVGUtils

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
            moduleFamilyTree.setOnClickListener {
                startActivity(FamilyTreeActivity.newIntent(requireContext()))
            }

            moduleFamilyStory.setOnClickListener {
                startActivity(FamilyStoryActivity.newIntent(requireContext()))
            }

            moduleFamilyActivity.setOnClickListener {
                startActivity(FamilyActivityActivity.newIntent(requireContext()))
            }

            moduleFamilyAlbum.setOnClickListener {
                // TODO: 启动祭祖日历活动
            }

            // 查看全部按钮
            textViewAll.setOnClickListener {
                // TODO: 跳转到全部动态页面
            }
            
            moduleAddMember.setOnClickListener {
                // TODO: 跳转到添加成员页面
            }
            
            moduleFamilyStatistics.setOnClickListener {
                // TODO: 跳转到家族统计页面
            }
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