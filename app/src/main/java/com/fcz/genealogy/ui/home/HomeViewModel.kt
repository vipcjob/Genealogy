package com.fcz.genealogy.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.fcz.genealogy.data.db.FamilyTreeDatabase
import com.fcz.genealogy.data.model.RecentUpdate
import com.fcz.genealogy.data.repository.FamilyRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: FamilyRepository
    
    private val _familyOverview = MutableLiveData<FamilyOverview>()
    val familyOverview: LiveData<FamilyOverview> = _familyOverview
    
    private val _recentUpdates = MutableLiveData<List<RecentUpdate>>()
    val recentUpdates: LiveData<List<RecentUpdate>> = _recentUpdates
    
    init {
        // 初始化仓库
        val database = FamilyTreeDatabase.getDatabase(application)
        repository = FamilyRepository(
            database.familyMemberDao(),
            database.familyStoryDao(),
            database.familyActivityDao()
        )
        
        // 加载数据
        loadFamilyOverview()
        loadRecentUpdates()
    }
    
    private fun loadFamilyOverview() {
        viewModelScope.launch {
            // 获取家族概况数据
            val memberCount = repository.getMemberCount()
            val livingCount = repository.getLivingMemberCount()
            val maxGeneration = repository.getMaxGeneration()
            
            // 获取始祖信息 (ID为张世德的成员)
            val ancestor = repository.getMemberById("zhang_shide")
            
            val lastUpdated = "2023年6月" // 在实际应用中这应该从数据库获取
            
            _familyOverview.postValue(
                FamilyOverview(
                    ancestorName = ancestor?.name ?: "张世德",
                    ancestorLifespan = "1824-1898",
                    generationCount = maxGeneration,
                    totalMembers = memberCount,
                    livingMembers = livingCount,
                    lastUpdated = lastUpdated
                )
            )
        }
    }
    
    private fun loadRecentUpdates() {
        // 在实际应用中，这些数据应该从数据库获取
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        
        val updates = listOf(
            RecentUpdate(
                id = "1",
                title = "张大明添加了新家庭成员",
                timestamp = sdf.parse("2023-06-15 10:30")!!,
                avatarType = "member",
                avatarText = "张",
                avatarColor = "#3F87F5"
            ),
            RecentUpdate(
                id = "2",
                title = "张文华发布了《张氏家训》解读",
                timestamp = sdf.parse("2023-06-12 15:45")!!,
                avatarType = "member",
                avatarText = "张",
                avatarColor = "#3F87F5"
            ),
            RecentUpdate(
                id = "3",
                title = "宗亲会通知：2023年祭祖活动安排",
                timestamp = sdf.parse("2023-06-10 08:20")!!,
                avatarType = "notification",
                avatarText = "通",
                avatarColor = "#FF9800"
            )
        )
        
        _recentUpdates.postValue(updates)
    }
}

// 家族概况数据类
data class FamilyOverview(
    val ancestorName: String,
    val ancestorLifespan: String,
    val generationCount: Int,
    val totalMembers: Int,
    val livingMembers: Int,
    val lastUpdated: String
)

// ViewModel工厂
class HomeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 