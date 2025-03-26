package com.example.familytree.ui.familytree

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.familytree.R
import com.example.familytree.databinding.FragmentFamilyTreeBinding
import com.example.familytree.ui.familytree.filter.FilterViewFragment
import com.example.familytree.ui.familytree.generation.GenerationViewFragment
import com.example.familytree.ui.familytree.list.ListViewFragment
import com.example.familytree.ui.familytree.tree.TreeViewFragment
import com.google.android.material.tabs.TabLayoutMediator

class FamilyTreeActivity : AppCompatActivity() {

    private lateinit var binding: FragmentFamilyTreeBinding
    private lateinit var tabAdapter: FamilyTreeTabAdapter
    
    companion object {
        private const val EXTRA_MEMBER_ID = "extra_member_id"
        
        fun newIntent(context: Context, memberId: String? = null): Intent {
            return Intent(context, FamilyTreeActivity::class.java).apply {
                if (memberId != null) {
                    putExtra(EXTRA_MEMBER_ID, memberId)
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFamilyTreeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val focusMemberId = intent.getStringExtra(EXTRA_MEMBER_ID)
        
        // 设置返回按钮
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        setupTabs(focusMemberId)
        
        // 设置搜索按钮
        binding.cardSearch.setOnClickListener {
            // TODO: 显示搜索界面
        }
        
        // 设置添加成员按钮
        binding.fabAdd.setOnClickListener {
            // TODO: 跳转到添加成员界面
        }
    }
    
    private fun setupTabs(focusMemberId: String?) {
        // 创建标签页
        val fragments = listOf<Fragment>(
            TreeViewFragment.newInstance(focusMemberId),
            ListViewFragment.newInstance(),
            GenerationViewFragment.newInstance(),
            FilterViewFragment.newInstance()
        )

        tabAdapter = FamilyTreeTabAdapter(this, fragments)
        binding.viewPager.adapter = tabAdapter
        
        // 连接TabLayout和ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_tree_view)
                1 -> getString(R.string.tab_list_view)
                2 -> getString(R.string.tab_generation_view)
                3 -> getString(R.string.tab_filter_view)
                else -> null
            }
        }.attach()
    }
} 