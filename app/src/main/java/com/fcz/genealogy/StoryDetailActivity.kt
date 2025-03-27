package com.fcz.genealogy.ui.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fcz.genealogy.R
import com.fcz.genealogy.databinding.ActivityStoryDetailBinding
import com.fcz.genealogy.ui.member.MemberDetailActivity
import com.fcz.genealogy.util.DateUtils

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding
    
    private val viewModel: StoryDetailViewModel by viewModels {
        StoryDetailViewModelFactory(application)
    }
    
    companion object {
        private const val EXTRA_STORY_ID = "extra_story_id"
        
        fun newIntent(context: Context, storyId: String): Intent {
            return Intent(context, StoryDetailActivity::class.java).apply {
                putExtra(EXTRA_STORY_ID, storyId)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val storyId = intent.getStringExtra(EXTRA_STORY_ID) ?: return
        
        // 设置返回按钮
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // 加载故事详情
        viewModel.loadStory(storyId)
        
        // 观察故事数据
        viewModel.story.observe(this) { story ->
            if (story != null) {
                // 设置标题和内容
                binding.toolbar.title = story.title
                binding.textTitle.text = story.title
                binding.textContent.text = story.content
                
                // 设置作者和日期
                binding.textAuthor.text = getString(R.string.story_author_format, story.author)
                binding.textDate.text = DateUtils.formatDate(story.publishDate)
                
                // 设置类型
                binding.textType.text = when(story.storyType) {
                    com.fcz.genealogy.data.model.StoryType.FAMILY -> "家族故事"
                    com.fcz.genealogy.data.model.StoryType.HISTORY -> "家族历史"
                    com.fcz.genealogy.data.model.StoryType.BIOGRAPHY -> "人物传记"
                    com.fcz.genealogy.data.model.StoryType.EVENT -> "活动记录"
                }
                
                // 设置标签
                binding.chipGroupTags.removeAllViews()
                for (tag in story.tags) {
                    val chip = com.google.android.material.chip.Chip(this)
                    chip.text = tag
                    binding.chipGroupTags.addView(chip)
                }
                
                // 设置相关成员
                viewModel.relatedMembers.observe(this) { members ->
                    binding.layoutRelatedMembers.removeAllViews()
                    
                    for (member in members) {
                        val memberView = layoutInflater.inflate(
                            R.layout.item_related_member,
                            binding.layoutRelatedMembers,
                            false
                        )
                        
                        val textName = memberView.findViewById<android.widget.TextView>(R.id.text_name)
                        textName.text = member.name
                        
                        memberView.setOnClickListener {
                            startActivity(MemberDetailActivity.newIntent(this, member.id))
                        }
                        
                        binding.layoutRelatedMembers.addView(memberView)
                    }
                }
            }
        }
    }
} 