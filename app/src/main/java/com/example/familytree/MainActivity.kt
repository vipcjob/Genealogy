package com.example.familytree

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.familytree.databinding.ActivityMainBinding
import com.example.familytree.data.db.FamilyTreeDatabase
import com.example.familytree.util.DataGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupNavigation()
        
        // 初始化示例数据（仅开发使用）
        initSampleData()
    }
    
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        
        // 设置底部导航
        binding.bottomNavigation.setupWithNavController(navController)
    }
    
    private fun initSampleData() {
        val database = FamilyTreeDatabase.getDatabase(this)
        val memberDao = database.familyMemberDao()
        val storyDao = database.familyStoryDao()
        val activityDao = database.familyActivityDao()
        
        CoroutineScope(Dispatchers.IO).launch {
            // 检查数据库是否为空
            if (memberDao.getMemberCount() == 0) {
                // 生成示例数据
                val sampleMembers = DataGenerator.generateSampleFamilyMembers()
                val sampleStories = DataGenerator.generateSampleFamilyStories()
                val sampleActivities = DataGenerator.generateSampleFamilyActivities()
                
                // 插入数据
                memberDao.insertMembers(sampleMembers)
                sampleStories.forEach { storyDao.insertStory(it) }
                sampleActivities.forEach { activityDao.insertActivity(it) }
            }
        }
    }
} 