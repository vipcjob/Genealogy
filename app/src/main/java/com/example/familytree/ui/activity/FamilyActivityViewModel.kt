package com.example.familytree.ui.activity

import android.app.Application
import androidx.lifecycle.*
import com.example.familytree.data.db.FamilyTreeDatabase
import com.example.familytree.data.model.FamilyActivity
import com.example.familytree.data.repository.FamilyRepository
import kotlinx.coroutines.launch
import java.util.*

class FamilyActivityViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: FamilyRepository
    
    // 所有活动
    val allActivities: LiveData<List<FamilyActivity>>
    
    // 即将开始的活动
    private val _upcomingActivities = MutableLiveData<List<FamilyActivity>>()
    val upcomingActivities: LiveData<List<FamilyActivity>> = _upcomingActivities
    
    // 正在进行的活动
    private val _ongoingActivities = MutableLiveData<List<FamilyActivity>>()
    val ongoingActivities: LiveData<List<FamilyActivity>> = _ongoingActivities
    
    // 已结束的活动
    private val _pastActivities = MutableLiveData<List<FamilyActivity>>()
    val pastActivities: LiveData<List<FamilyActivity>> = _pastActivities
    
    init {
        // 初始化仓库
        val database = FamilyTreeDatabase.getDatabase(application)
        repository = FamilyRepository(
            database.familyMemberDao(),
            database.familyStoryDao(),
            database.familyActivityDao()
        )
        
        // 获取所有活动
        allActivities = repository.getAllActivities()
        
        // 观察所有活动变化并分类
        allActivities.observeForever { activities ->
            if (activities != null) {
                categorizeActivities(activities)
            }
        }
    }
    
    private fun categorizeActivities(activities: List<FamilyActivity>) {
        val today = Calendar.getInstance().time
        
        val upcoming = mutableListOf<FamilyActivity>()
        val ongoing = mutableListOf<FamilyActivity>()
        val past = mutableListOf<FamilyActivity>()
        
        for (activity in activities) {
            when {
                // 开始时间在今天之后的是即将开始的活动
                activity.startDate?.after(today) == true -> {
                    upcoming.add(activity)
                }
                // 结束时间在今天之前的是已结束的活动
                activity.endDate?.before(today) == true -> {
                    past.add(activity)
                }
                // 其他情况是正在进行的活动（开始时间在今天之前，结束时间在今天之后）
                else -> {
                    ongoing.add(activity)
                }
            }
        }
        
        // 按时间排序
        upcoming.sortBy { it.startDate }
        ongoing.sortBy { it.endDate }
        past.sortByDescending { it.endDate }
        
        _upcomingActivities.value = upcoming
        _ongoingActivities.value = ongoing
        _pastActivities.value = past
    }
    
    fun addActivity(activity: FamilyActivity) {
        viewModelScope.launch {
            repository.insertActivity(activity)
        }
    }
    
    fun updateActivity(activity: FamilyActivity) {
        viewModelScope.launch {
            repository.updateActivity(activity)
        }
    }
    
    fun deleteActivity(activity: FamilyActivity) {
        viewModelScope.launch {
            repository.deleteActivity(activity)
        }
    }
}

class FamilyActivityViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FamilyActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FamilyActivityViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 