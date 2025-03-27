package com.fcz.genealogy

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fcz.genealogy.databinding.ActivityMainBinding
import com.fcz.genealogy.data.db.FamilyTreeDatabase
import com.fcz.genealogy.ui.home.HomeFragment
import com.fcz.genealogy.ui.familytree.FamilyTreeFragment
import com.fcz.genealogy.ui.story.FamilyStoryFragment
import com.fcz.genealogy.ui.profile.ProfileFragment
import com.fcz.genealogy.util.DataGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    
    // 页面索引常量
    companion object {
        private const val HOME_PAGE_INDEX = 0
        private const val FAMILY_TREE_PAGE_INDEX = 1
        private const val STORY_PAGE_INDEX = 2
        private const val PROFILE_PAGE_INDEX = 3
        private const val PAGE_COUNT = 4
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupStatusBar()
        setupViewPager()
        setupBottomNavigation()
        
        // 初始化示例数据（仅开发使用）
        initSampleData()
    }
    
    private fun setupStatusBar() {
        // 设置状态栏颜色与主题主颜色一致
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary)
        
        // 根据颜色的亮度决定状态栏图标是否使用深色
        // 如果使用浅色主题，可以设置为true，表示状态栏使用深色图标
        // window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
    
    private fun setupViewPager() {
        viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.apply {
            adapter = viewPagerAdapter
            isUserInputEnabled = false // 禁止滑动切换，只通过底部导航切换
        }
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> binding.viewPager.currentItem = HOME_PAGE_INDEX
                R.id.navigation_family_tree -> binding.viewPager.currentItem = FAMILY_TREE_PAGE_INDEX
                R.id.navigation_family_story -> binding.viewPager.currentItem = STORY_PAGE_INDEX
                R.id.navigation_profile -> binding.viewPager.currentItem = PROFILE_PAGE_INDEX
            }
            true
        }
    }
    
    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        
        override fun getItemCount(): Int = PAGE_COUNT
        
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                HOME_PAGE_INDEX -> HomeFragment()
                FAMILY_TREE_PAGE_INDEX -> FamilyTreeFragment()
                STORY_PAGE_INDEX -> FamilyStoryFragment()
                PROFILE_PAGE_INDEX -> ProfileFragment()
                else -> throw IllegalArgumentException("无效页面索引: $position")
            }
        }
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