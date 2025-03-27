package com.fcz.genealogy.ui.story

import android.app.Application
import androidx.lifecycle.*
import com.fcz.genealogy.data.db.FamilyTreeDatabase
import com.fcz.genealogy.data.model.FamilyStory
import com.fcz.genealogy.data.model.StoryType
import com.fcz.genealogy.data.repository.FamilyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class FamilyStoryViewModel(application: Application) : AndroidViewModel(application) {

    // 1. 首先初始化 repository
    private val repository: FamilyRepository = FamilyTreeDatabase.getDatabase(application).let { db ->
        FamilyRepository(
            db.familyMemberDao(),
            db.familyStoryDao(),
            db.familyActivityDao()
        )
    }

    // 2. 其他属性定义
    val allStories: LiveData<List<FamilyStory>> = repository.getAllStories()

    private val storyTypeFilter = MutableStateFlow<StoryType?>(null)

    private val _filteredStories = MutableLiveData<List<StoryUiModel>>()
    val filteredStories: LiveData<List<StoryUiModel>> = _filteredStories

    private val _stories = MutableLiveData<List<FamilyStory>>()
    val stories: LiveData<List<FamilyStory>> = _stories.map { storyList ->
        storyList.map { story -> story }
    }

    init {
        // 4. 设置观察者
        allStories.asFlow().combine(storyTypeFilter) { stories, filterType ->
            if (filterType == null) {
                stories
            } else {
                stories.filter { story ->
                    story.storyType == filterType
                }
            }
        }.asLiveData().observeForever { filtered ->
            viewModelScope.launch {
                val uiModels = filtered.map { story ->
                    val memberNames = story.relatedMemberIds.mapNotNull { memberId ->
                        repository.getMemberById(memberId)?.name
                    }
                    StoryUiModel(
                        id = story.id,
                        title = story.title,
                        content = story.content,
                        author = story.author,
                        publishDate = story.publishDate,
                        storyType = story.storyType,
                        relatedMemberNames = memberNames,
                        viewCount = story.viewCount
                    )
                }
                _filteredStories.postValue(uiModels)
            }
        }
    }

    fun setStoryTypeFilter(type: StoryType?) {
        storyTypeFilter.value = type
    }

    private suspend fun getStoryUiModel(story: FamilyStory): StoryUiModel {
        val memberNames = story.relatedMemberIds.mapNotNull { memberId ->
            repository.getMemberById(memberId)?.name
        }
        return StoryUiModel(
            id = story.id,
            title = story.title,
            content = story.content,
            author = story.author,
            publishDate = story.publishDate,
            storyType = story.storyType,
            relatedMemberNames = memberNames,
            viewCount = story.viewCount
        )
    }
}

class FamilyStoryViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FamilyStoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FamilyStoryViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 