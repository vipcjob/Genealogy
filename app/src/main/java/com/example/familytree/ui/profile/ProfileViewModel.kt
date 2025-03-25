package com.example.familytree.ui.profile

import android.app.Application
import androidx.lifecycle.*
import com.example.familytree.data.db.FamilyTreeDatabase
import com.example.familytree.data.model.FamilyMember
import com.example.familytree.data.repository.FamilyRepository
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: FamilyRepository
    
    // 当前用户信息
    private val _userInfo = MutableLiveData<FamilyMember>()
    val userInfo: LiveData<FamilyMember> = _userInfo
    
    // 用户贡献统计
    private val _contributionStats = MutableLiveData<ContributionStats>()
    val contributionStats: LiveData<ContributionStats> = _contributionStats
    
    init {
        // 初始化仓库
        val database = FamilyTreeDatabase.getDatabase(application)
        repository = FamilyRepository(
            database.familyMemberDao(),
            database.familyStoryDao(),
            database.familyActivityDao()
        )
        
        // 加载当前用户信息
        loadCurrentUser()
        
        // 加载贡献统计
        loadContributionStats()
    }
    
    private fun loadCurrentUser() {
        viewModelScope.launch {
            // 假设当前登录的用户ID为zhang_minghui
            val user = repository.getMemberById("zhang_minghui")
            _userInfo.postValue(user)
        }
    }
    
    private fun loadContributionStats() {
        viewModelScope.launch {
            // 假设的贡献统计数据
            _contributionStats.postValue(
                ContributionStats(
                    addedMembers = 5,
                    addedStories = 3,
                    addedPhotos = 12,
                    participatedActivities = 7
                )
            )
        }
    }
    
    data class ContributionStats(
        val addedMembers: Int,
        val addedStories: Int,
        val addedPhotos: Int,
        val participatedActivities: Int
    )
}

class ProfileViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 