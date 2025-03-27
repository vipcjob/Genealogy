package com.fcz.genealogy.ui.familytree

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.fcz.genealogy.R
import com.fcz.genealogy.databinding.FragmentFamilyTreeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FamilyTreeFragment : Fragment() {
    
    private var _binding: FragmentFamilyTreeBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FamilyTreeViewModel by viewModels { FamilyTreeViewModelFactory(requireActivity().application) }
    private lateinit var viewPagerAdapter: FamilyTreeViewPagerAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamilyTreeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViewPager()
        setupSearchView()
        setupFab()
    }
    
    private fun setupViewPager() {
        viewPagerAdapter = FamilyTreeViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter
        
        // 将TabLayout与ViewPager2关联
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.view_tree)
                1 -> getString(R.string.view_list)
                2 -> getString(R.string.view_generation)
                3 -> getString(R.string.view_filter)
                else -> ""
            }
        }.attach()
        
        // 页面切换监听
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setCurrentTab(position)
            }
        })
        
        // 观察当前选项卡
        viewModel.currentTab.observe(viewLifecycleOwner) { tabPosition ->
            if (binding.viewPager.currentItem != tabPosition) {
                binding.viewPager.currentItem = tabPosition
            }
        }
    }
    
    private fun setupSearchView() {
        binding.cardSearch.setOnClickListener {
            // TODO: 打开搜索界面
        }
    }
    
    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            // TODO: 打开添加成员界面
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 