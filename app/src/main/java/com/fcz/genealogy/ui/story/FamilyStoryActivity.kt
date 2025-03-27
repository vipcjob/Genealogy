package com.fcz.genealogy.ui.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fcz.genealogy.data.model.StoryType
import com.fcz.genealogy.databinding.FragmentFamilyStoryBinding

class FamilyStoryActivity : AppCompatActivity() {

    private lateinit var binding: FragmentFamilyStoryBinding
    private lateinit var adapter: StoryAdapter

    private val viewModel: FamilyStoryViewModel by viewModels {
        FamilyStoryViewModelFactory(application)
    }

    companion object {
        private const val EXTRA_MEMBER_ID = "extra_member_id"
        
        fun newIntent(context: Context, memberId: String? = null): Intent {
            return Intent(context, FamilyStoryActivity::class.java).apply {
                if (memberId != null) {
                    putExtra(EXTRA_MEMBER_ID, memberId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFamilyStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val memberId = intent.getStringExtra(EXTRA_MEMBER_ID)
        
        // 设置返回按钮
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

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
            startActivity(StoryDetailActivity.newIntent(this, storyUiModel.id))
        }

        binding.recyclerViewStories.layoutManager = LinearLayoutManager(this)
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
        viewModel.filteredStories.observe(this) { storyUiModels ->
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
} 