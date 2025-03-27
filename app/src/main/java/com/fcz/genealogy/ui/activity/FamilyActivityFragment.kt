package com.fcz.genealogy.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fcz.genealogy.databinding.FragmentFamilyActivityBinding
import com.fcz.genealogy.ui.activity.adapter.UpcomingActivityAdapter
import com.fcz.genealogy.ui.activity.adapter.OngoingActivityAdapter
import com.fcz.genealogy.ui.activity.adapter.PastActivityAdapter

class FamilyActivityFragment : Fragment() {
    
    private var _binding: FragmentFamilyActivityBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FamilyActivityViewModel by viewModels { 
        FamilyActivityViewModelFactory(requireActivity().application) 
    }
    
    private lateinit var upcomingAdapter: UpcomingActivityAdapter
    private lateinit var ongoingAdapter: OngoingActivityAdapter
    private lateinit var pastAdapter: PastActivityAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamilyActivityBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUpcomingActivities()
        setupOngoingActivities()
        setupPastActivities()
        
        // 设置添加按钮
        binding.fabAdd.setOnClickListener {
            // TODO: 添加新活动的实现
        }
        
        observeViewModel()
    }
    
    private fun setupUpcomingActivities() {
        upcomingAdapter = UpcomingActivityAdapter { activity ->
            // 跳转到活动详情页
            // TODO: 实现活动详情页导航
        }
        
        binding.recyclerViewUpcoming.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUpcoming.adapter = upcomingAdapter
    }
    
    private fun setupOngoingActivities() {
        ongoingAdapter = OngoingActivityAdapter { activity ->
            // 跳转到活动详情页
            // TODO: 实现活动详情页导航
        }
        
        binding.recyclerViewOngoing.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewOngoing.adapter = ongoingAdapter
    }
    
    private fun setupPastActivities() {
        pastAdapter = PastActivityAdapter { activity ->
            // 跳转到活动详情页
            // TODO: 实现活动详情页导航
        }
        
        binding.recyclerViewPast.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPast.adapter = pastAdapter
    }
    
    private fun observeViewModel() {
        viewModel.upcomingActivities.observe(viewLifecycleOwner) { activities ->
            upcomingAdapter.submitList(activities)
            
            if (activities.isEmpty()) {
                binding.layoutUpcomingEmpty.visibility = View.VISIBLE
                binding.recyclerViewUpcoming.visibility = View.GONE
            } else {
                binding.layoutUpcomingEmpty.visibility = View.GONE
                binding.recyclerViewUpcoming.visibility = View.VISIBLE
            }
        }
        
        viewModel.ongoingActivities.observe(viewLifecycleOwner) { activities ->
            ongoingAdapter.submitList(activities)
            
            if (activities.isEmpty()) {
                binding.layoutOngoingEmpty.visibility = View.VISIBLE
                binding.recyclerViewOngoing.visibility = View.GONE
            } else {
                binding.layoutOngoingEmpty.visibility = View.GONE
                binding.recyclerViewOngoing.visibility = View.VISIBLE
            }
        }
        
        viewModel.pastActivities.observe(viewLifecycleOwner) { activities ->
            pastAdapter.submitList(activities)
            
            if (activities.isEmpty()) {
                binding.layoutPastEmpty.visibility = View.VISIBLE
                binding.recyclerViewPast.visibility = View.GONE
            } else {
                binding.layoutPastEmpty.visibility = View.GONE
                binding.recyclerViewPast.visibility = View.VISIBLE
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 