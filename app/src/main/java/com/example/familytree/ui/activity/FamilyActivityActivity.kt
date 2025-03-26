package com.example.familytree.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familytree.databinding.FragmentFamilyActivityBinding

class FamilyActivityActivity : AppCompatActivity() {

    private lateinit var binding: FragmentFamilyActivityBinding
    private lateinit var upcomingAdapter: UpcomingActivityAdapter
    private lateinit var ongoingAdapter: OngoingActivityAdapter
    private lateinit var pastAdapter: PastActivityAdapter

    private val viewModel: FamilyActivityViewModel by viewModels {
        FamilyActivityViewModelFactory(application)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, FamilyActivityActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFamilyActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 设置返回按钮
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        setupRecyclerViews()
        observeViewModel()

        // 设置添加按钮
        binding.fabAdd.setOnClickListener {
            // TODO: 跳转到添加活动界面
        }
    }

    private fun setupRecyclerViews() {
        // 即将开始的活动
        upcomingAdapter = UpcomingActivityAdapter { activity ->
            startActivity(ActivityDetailActivity.newIntent(this, activity.id))
        }
        binding.recyclerViewUpcoming.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewUpcoming.adapter = upcomingAdapter

        // 正在进行的活动
        ongoingAdapter = OngoingActivityAdapter { activity ->
            startActivity(ActivityDetailActivity.newIntent(this, activity.id))
        }
        binding.recyclerViewOngoing.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOngoing.adapter = ongoingAdapter

        // 已完成的活动
        pastAdapter = PastActivityAdapter { activity ->
            startActivity(ActivityDetailActivity.newIntent(this, activity.id))
        }
        binding.recyclerViewPast.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewPast.adapter = pastAdapter
    }

    private fun observeViewModel() {
        // 观察即将开始的活动
        viewModel.upcomingActivities.observe(this) { activities ->
            upcomingAdapter.submitList(activities)
            
            if (activities.isEmpty()) {
                binding.layoutUpcomingEmpty.visibility = android.view.View.VISIBLE
            } else {
                binding.layoutUpcomingEmpty.visibility = android.view.View.GONE
            }
        }

        // 观察正在进行的活动
        viewModel.ongoingActivities.observe(this) { activities ->
            ongoingAdapter.submitList(activities)
            
            if (activities.isEmpty()) {
                binding.layoutOngoingEmpty.visibility = android.view.View.VISIBLE
            } else {
                binding.layoutOngoingEmpty.visibility = android.view.View.GONE
            }
        }

        // 观察已完成的活动
        viewModel.pastActivities.observe(this) { activities ->
            pastAdapter.submitList(activities)
            
            if (activities.isEmpty()) {
                binding.layoutPastEmpty.visibility = android.view.View.VISIBLE
            } else {
                binding.layoutPastEmpty.visibility = android.view.View.GONE
            }
        }
    }
} 