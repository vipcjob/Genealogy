package com.example.familytree.ui.familytree.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.familytree.R
import com.example.familytree.data.model.Gender
import com.example.familytree.databinding.FragmentFilterViewBinding
import com.example.familytree.ui.familytree.FamilyTreeViewModel
import com.example.familytree.ui.familytree.FamilyTreeViewModelFactory
import com.google.android.material.chip.Chip

class FilterViewFragment : Fragment() {
    
    private var _binding: FragmentFilterViewBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FamilyTreeViewModel by activityViewModels { 
        FamilyTreeViewModelFactory(requireActivity().application) 
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
        
        setupQuickFilterChips()
        setupGenerationChips()
        setupBranchChips()
        setupEraChips()
        setupButtons()
    }
    
    private fun setupQuickFilterChips() {
        binding.chipAll.setOnClickListener {
            viewModel.setFilterIsAlive(null)
            viewModel.setFilterGender(null)
        }
        
        binding.chipAlive.setOnClickListener {
            viewModel.setFilterIsAlive(true)
            viewModel.setFilterGender(null)
        }
        
        binding.chipDeceased.setOnClickListener {
            viewModel.setFilterIsAlive(false)
            viewModel.setFilterGender(null)
        }
        
        binding.chipMale.setOnClickListener {
            viewModel.setFilterGender(Gender.MALE)
            viewModel.setFilterIsAlive(null)
        }
        
        binding.chipFemale.setOnClickListener {
            viewModel.setFilterGender(Gender.FEMALE)
            viewModel.setFilterIsAlive(null)
        }
    }
    
    private fun setupGenerationChips() {
        binding.chipGenerationAll.setOnClickListener {
            viewModel.setFilterGeneration(null)
        }
        
        // 为每个世代Chip设置监听器
        for (i in 1..6) {
            val chipId = resources.getIdentifier("chip_generation_$i", "id", requireContext().packageName)
            view?.findViewById<Chip>(chipId)?.setOnClickListener {
                viewModel.setFilterGeneration(i)
            }
        }
    }
    
    private fun setupBranchChips() {
        binding.chipBranchAll.setOnClickListener {
            viewModel.setFilterBranch(null)
        }
        
        binding.chipBranchMingde.setOnClickListener {
            viewModel.setFilterBranch("明德支")
        }
        
        binding.chipBranchMingli.setOnClickListener {
            viewModel.setFilterBranch("明礼支")
        }
        
        binding.chipBranchMingxin.setOnClickListener {
            viewModel.setFilterBranch("明信支")
        }
    }
    
    private fun setupEraChips() {
        binding.chipEraAll.setOnClickListener {
            // TODO: 实现按年代筛选逻辑
        }
        
        binding.chipEraQing.setOnClickListener {
            // TODO: 实现按清朝年代筛选逻辑
        }
        
        binding.chipEraRepublic.setOnClickListener {
            // TODO: 实现按民国年代筛选逻辑
        }
        
        binding.chipEraPrc.setOnClickListener {
            // TODO: 实现按新中国年代筛选逻辑
        }
    }
    
    private fun setupButtons() {
        binding.buttonReset.setOnClickListener {
            // 重置所有筛选
            viewModel.resetFilters()
            
            // 重置所有Chip状态
            binding.chipAll.isChecked = true
            binding.chipGenerationAll.isChecked = true
            binding.chipBranchAll.isChecked = true
            binding.chipEraAll.isChecked = true
        }
        
        binding.buttonApply.setOnClickListener {
            // 切换到列表视图
            viewModel.setCurrentTab(1)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 