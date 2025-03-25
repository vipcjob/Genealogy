package com.example.familytree.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.familytree.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ProfileViewModel by viewModels { 
        ProfileViewModelFactory(requireActivity().application) 
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupProfile()
        setupMenuItems()
    }
    
    private fun setupProfile() {
        // 设置用户名
        binding.textUsername.text = "张明辉"
        
        // 设置身份标识
        binding.textIdentity.text = "第五代 · 明德支"
        
        // 观察用户信息
        viewModel.userInfo.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.textUsername.text = user.name
                binding.textIdentity.text = "第${user.generation}代 · ${user.branch}支"
            }
        }
    }
    
    private fun setupMenuItems() {
        // 我的资料
        binding.menuProfile.setOnClickListener {
            // TODO: 跳转到个人资料页面
        }
        
        // 我的家族
        binding.menuFamily.setOnClickListener {
            // TODO: 跳转到我的家族页面
        }
        
        // 我的收藏
        binding.menuFavorites.setOnClickListener {
            // TODO: 跳转到我的收藏页面
        }
        
        // 我的贡献
        binding.menuContributions.setOnClickListener {
            // TODO: 跳转到我的贡献页面
        }
        
        // 设置
        binding.menuSettings.setOnClickListener {
            // TODO: 跳转到设置页面
        }
        
        // 帮助与反馈
        binding.menuHelp.setOnClickListener {
            // TODO: 跳转到帮助与反馈页面
        }
        
        // 关于
        binding.menuAbout.setOnClickListener {
            // TODO: 跳转到关于页面
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 