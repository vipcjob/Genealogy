package com.fcz.genealogy.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fcz.genealogy.R
import com.fcz.genealogy.databinding.ActivityFamilyActivityDetailBinding
import com.fcz.genealogy.util.DateUtils

class ActivityDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFamilyActivityDetailBinding
    
    private val viewModel: ActivityDetailViewModel by viewModels {
        ActivityDetailViewModelFactory(application)
    }
    
    companion object {
        private const val EXTRA_ACTIVITY_ID = "extra_activity_id"
        
        fun newIntent(context: Context, activityId: String): Intent {
            return Intent(context, ActivityDetailActivity::class.java).apply {
                putExtra(EXTRA_ACTIVITY_ID, activityId)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFamilyActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val activityId = intent.getStringExtra(EXTRA_ACTIVITY_ID) ?: return
        
        // 设置返回按钮
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // 加载活动详情
        viewModel.loadActivity(activityId)
        
        // 观察活动数据
        viewModel.activity.observe(this) { activity ->
            if (activity != null) {
                // 设置标题
                binding.toolbar.title = activity.title
                binding.textTitle.text = activity.title
                
                // 设置描述
                binding.textDescription.text = activity.description
                
                // 设置时间
                val timeText = if (activity.endDate != null) {
                    "${DateUtils.formatDate(activity.startDate)} - ${DateUtils.formatDate(activity.endDate)}"
                } else {
                    DateUtils.formatDate(activity.startDate)
                }
                binding.textTime.text = timeText
                
                // 设置地点
                binding.textLocation.text = activity.location
                
                // 设置组织者
                binding.textOrganizer.text = activity.organizer
                
                // 设置活动类型
                binding.textType.text = when (activity.activityType) {
                    com.fcz.genealogy.data.model.ActivityType.WORSHIP -> "祭祖活动"
                    com.fcz.genealogy.data.model.ActivityType.REUNION -> "宗亲聚会"
                    com.fcz.genealogy.data.model.ActivityType.CULTURAL -> "文化活动"
                    else -> "其他"
                }
                
                // 设置活动状态
                val statusText = when (activity.status) {
                    com.fcz.genealogy.data.model.ActivityStatus.UPCOMING -> "即将开始"
                    com.fcz.genealogy.data.model.ActivityStatus.ONGOING -> "正在进行"
                    com.fcz.genealogy.data.model.ActivityStatus.COMPLETED -> "已结束"
                }
                binding.textStatus.text = statusText
                
                // 设置参与者
                viewModel.participants.observe(this) { participants ->
                    binding.textParticipants.text = participants.joinToString(", ") { it.name }
                }
                
                // 设置图片
                if (activity.imageUrls.isNotEmpty()) {
                    // TODO: 设置图片轮播
                } else {
                    binding.imageViewPlaceholder.setImageResource(R.drawable.ic_family_activity)
                }
            }
        }
    }
} 