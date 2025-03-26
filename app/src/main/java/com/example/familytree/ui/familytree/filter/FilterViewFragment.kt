package com.example.familytree.ui.familytree.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.familytree.data.model.Gender
import com.example.familytree.databinding.FragmentFilterViewBinding
import com.example.familytree.ui.familytree.FamilyTreeViewModel
import com.example.familytree.ui.familytree.FamilyTreeViewModelFactory

class FilterViewFragment : Fragment() {

    private var _binding: FragmentFilterViewBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FamilyTreeViewModel by activityViewModels { 
        FamilyTreeViewModelFactory(requireActivity().application) 
    }
    
    companion object {
        fun newInstance(): FilterViewFragment {
            return FilterViewFragment()
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterViewBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFilterControls()
    }
    
    private fun setupFilterControls() {
        // 性别筛选
        binding.chipMale.setOnClickListener {
            viewModel.setGenderFilter(Gender.MALE)
        }
        
        binding.chipFemale.setOnClickListener {
            viewModel.setGenderFilter(Gender.FEMALE)
        }
        
        binding.chipAll.setOnClickListener {
            viewModel.clearGenderFilter()
        }
        
        // 生存状态筛选
        binding.chipAlive.setOnClickListener {
            viewModel.setAliveFilter(true)
        }
        
        binding.chipDeceased.setOnClickListener {
            viewModel.setAliveFilter(false)
        }
        
        // 世代筛选
        binding.chipGenerationAll.setOnClickListener {
            viewModel.clearGenerationFilter()
        }
        
        binding.chipGeneration1.setOnClickListener {
            viewModel.setGenerationFilter(1)
        }
        
        binding.chipGeneration2.setOnClickListener {
            viewModel.setGenerationFilter(2)
        }
        
        binding.chipGeneration3.setOnClickListener {
            viewModel.setGenerationFilter(3)
        }
        
        binding.chipGeneration4.setOnClickListener {
            viewModel.setGenerationFilter(4)
        }
        
        binding.chipGeneration5.setOnClickListener {
            viewModel.setGenerationFilter(5)
        }
        
        binding.chipGeneration6.setOnClickListener {
            viewModel.setGenerationFilter(6)
        }
        
        // 分支筛选
        binding.chipBranchAll.setOnClickListener {
            viewModel.clearBranchFilter()
        }
        
        binding.chipBranchMingde.setOnClickListener {
            viewModel.setBranchFilter("明德支")
        }
        
        binding.chipBranchMingli.setOnClickListener {
            viewModel.setBranchFilter("明礼支")
        }
        
        binding.chipBranchMingxin.setOnClickListener {
            viewModel.setBranchFilter("明信支")
        }
        
        // 年代筛选
        binding.chipEraAll.setOnClickListener {
            viewModel.clearEraFilter()
        }
        
        binding.chipEraQing.setOnClickListener {
            viewModel.setEraFilter("清朝")
        }
        
        binding.chipEraRepublic.setOnClickListener {
            viewModel.setEraFilter("民国时期")
        }
        
        binding.chipEraPrc.setOnClickListener {
            viewModel.setEraFilter("新中国成立后")
        }
        
        // 重置和应用按钮
        binding.buttonReset.setOnClickListener {
            viewModel.resetAllFilters()
            
            // 重置选中状态
            binding.chipAll.isChecked = true
            binding.chipGenerationAll.isChecked = true
            binding.chipBranchAll.isChecked = true
            binding.chipEraAll.isChecked = true
        }
        
        binding.buttonApply.setOnClickListener {
            viewModel.applyFilters()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 