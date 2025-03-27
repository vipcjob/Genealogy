package com.fcz.genealogy.ui.activity

import android.app.Application
import androidx.lifecycle.*
import com.fcz.genealogy.data.db.FamilyTreeDatabase
import com.fcz.genealogy.data.model.ActivityStatus
import com.fcz.genealogy.data.model.FamilyActivity
import com.fcz.genealogy.data.repository.FamilyRepository
import kotlinx.coroutines.launch
import java.util.*

class FamilyActivityViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: FamilyRepository
    
    // 即将开始的活动
    val upcomingActivities: LiveData<List<FamilyActivity>>
    
    // 正在进行的活动
    val ongoingActivities: LiveData<List<FamilyActivity>>
    
    // 已完成的活动
    val pastActivities: LiveData<List<FamilyActivity>>
    
    init {
        val database = FamilyTreeDatabase.getDatabase(application)
        repository = FamilyRepository(
            database.familyMemberDao(),
            database.familyStoryDao(),
            database.familyActivityDao()
        )
        
        // 获取即将开始的活动
        upcomingActivities = repository.getActivitiesByStatus(ActivityStatus.UPCOMING)
        
        // 获取正在进行的活动
        ongoingActivities = repository.getActivitiesByStatus(ActivityStatus.ONGOING)
        
        // 获取已完成的活动
        pastActivities = repository.getActivitiesByStatus(ActivityStatus.COMPLETED)
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