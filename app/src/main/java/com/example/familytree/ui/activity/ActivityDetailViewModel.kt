package com.example.familytree.ui.activity

import android.app.Application
import androidx.lifecycle.*
import com.example.familytree.data.db.FamilyTreeDatabase
import com.example.familytree.data.model.FamilyActivity
import com.example.familytree.data.model.FamilyMember
import com.example.familytree.data.repository.FamilyRepository
import kotlinx.coroutines.launch

class ActivityDetailViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: FamilyRepository
    
    // 活动数据
    private val _activity = MutableLiveData<FamilyActivity>()
    val activity: LiveData<FamilyActivity> = _activity
    
    // 参与者
    private val _participants = MutableLiveData<List<FamilyMember>>()
    val participants: LiveData<List<FamilyMember>> = _participants
    
    init {
        val database = FamilyTreeDatabase.getDatabase(application)
        repository = FamilyRepository(
            database.familyMemberDao(),
            database.familyStoryDao(),
            database.familyActivityDao()
        )
    }
    
    fun loadActivity(activityId: String) {
        viewModelScope.launch {
            val activity = repository.getActivityById(activityId)
            activity?.let {
                _activity.postValue(it)
                loadParticipants(it.participantIds)
            }
        }
    }
    
    private fun loadParticipants(participantIds: List<String>) {
        viewModelScope.launch {
            val members = participantIds.mapNotNull { memberId ->
                repository.getMemberById(memberId)
            }
            _participants.postValue(members)
        }
    }
}

class ActivityDetailViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActivityDetailViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 