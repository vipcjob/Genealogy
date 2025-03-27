package com.fcz.genealogy.ui.story

import android.app.Application
import androidx.lifecycle.*
import com.fcz.genealogy.data.db.FamilyTreeDatabase
import com.fcz.genealogy.data.model.FamilyMember
import com.fcz.genealogy.data.model.FamilyStory
import com.fcz.genealogy.data.repository.FamilyRepository
import kotlinx.coroutines.launch

class StoryDetailViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: FamilyRepository
    
    // 故事数据
    private val _story = MutableLiveData<FamilyStory>()
    val story: LiveData<FamilyStory> = _story
    
    // 相关成员
    private val _relatedMembers = MutableLiveData<List<FamilyMember>>()
    val relatedMembers: LiveData<List<FamilyMember>> = _relatedMembers
    
    init {
        val database = FamilyTreeDatabase.getDatabase(application)
        repository = FamilyRepository(
            database.familyMemberDao(),
            database.familyStoryDao(),
            database.familyActivityDao()
        )
    }
    
    fun loadStory(storyId: String) {
        viewModelScope.launch {
            val story = repository.getStoryById(storyId)
            story?.let {
                _story.postValue(it)
                loadRelatedMembers(it.relatedMemberIds)
            }
        }
    }
    
    private fun loadRelatedMembers(memberIds: List<String>) {
        viewModelScope.launch {
            val members = memberIds.mapNotNull { memberId ->
                repository.getMemberById(memberId)
            }
            _relatedMembers.postValue(members)
        }
    }
}

class StoryDetailViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryDetailViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 