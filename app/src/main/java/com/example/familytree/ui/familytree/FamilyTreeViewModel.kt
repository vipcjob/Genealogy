package com.example.familytree.ui.familytree

import android.app.Application
import androidx.lifecycle.*
import com.example.familytree.data.db.FamilyTreeDatabase
import com.example.familytree.data.model.FamilyMember
import com.example.familytree.data.model.Gender
import com.example.familytree.data.repository.FamilyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class FamilyTreeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FamilyRepository

    // 当前选中的Tab页
    private val _currentTab = MutableLiveData<Int>(0)
    val currentTab: LiveData<Int> = _currentTab

    // 当前焦点成员ID
    private val _focusMemberId = MutableLiveData<String>("zhang_yongkang")
    val focusMemberId: LiveData<String> = _focusMemberId

    // 当前焦点成员
    private val _focusMember = MutableLiveData<FamilyMember>()
    val focusMember: LiveData<FamilyMember> = _focusMember

    // 当前焦点成员的祖先
    private val _ancestors = MutableLiveData<List<FamilyMember>>(emptyList())
    val ancestors: LiveData<List<FamilyMember>> = _ancestors

    // 当前焦点成员的后代
    private val _descendants = MutableLiveData<List<FamilyMember>>(emptyList())
    val descendants: LiveData<List<FamilyMember>> = _descendants

    // 所有成员
    val allMembers: LiveData<List<FamilyMember>>

    // 筛选条件
    private val filterIsAlive = MutableStateFlow<Boolean?>(null)
    private val filterGender = MutableStateFlow<Gender?>(null)
    private val filterGeneration = MutableStateFlow<Int?>(null)
    private val filterBranch = MutableStateFlow<String?>(null)

    // 经过筛选的成员
    private val _filteredMembers = MutableLiveData<List<FamilyMember>>()
    val filteredMembers: LiveData<List<FamilyMember>> = _filteredMembers

    init {
        // 初始化仓库
        val database = FamilyTreeDatabase.getDatabase(application)
        repository = FamilyRepository(
            database.familyMemberDao(),
            database.familyStoryDao(),
            database.familyActivityDao()
        )

        // 获取所有成员
        allMembers = repository.getAllMembers()

        // 加载当前焦点成员
        loadFocusMember()

        // 观察焦点成员变化
        focusMemberId.observeForever { id ->
            loadFocusMember(id)
        }

        // 应用筛选
        viewModelScope.launch {
            combine(
                filterIsAlive,
                filterGender,
                filterGeneration,
                filterBranch
            ) { isAlive, gender, generation, branch ->
                allMembers.value?.filter { member ->
                    (isAlive == null || member.isAlive == isAlive) &&
                        (gender == null || member.gender == gender) &&
                        (generation == null || member.generation == generation) &&
                        (branch == null || member.branch == branch)
                } ?: emptyList()
            }.collect { filtered ->
                _filteredMembers.postValue(filtered)
            }
        }
    }

    fun setCurrentTab(position: Int) {
        _currentTab.value = position
    }

    fun setFocusMember(memberId: String) {
        _focusMemberId.value = memberId
    }

    private fun loadFocusMember(memberId: String = _focusMemberId.value ?: "zhang_yongkang") {
        viewModelScope.launch {
            val member = repository.getMemberById(memberId)
            if (member != null) {
                _focusMember.postValue(member)
                loadAncestors(member)
                loadDescendants(member)
            }
        }
    }

    private fun loadAncestors(member: FamilyMember) {
        viewModelScope.launch {
            val ancestors = mutableListOf<FamilyMember>()
            var currentMember = member

            // 向上追溯祖先
            while (currentMember.parentId != null) {
                val parent = repository.getMemberById(currentMember.parentId!!)
                if (parent != null) {
                    ancestors.add(parent)
                    currentMember = parent
                } else {
                    break
                }
            }

            _ancestors.postValue(ancestors)
        }
    }

    private fun loadDescendants(member: FamilyMember) {
        viewModelScope.launch {
            val descendants = mutableListOf<FamilyMember>()

            // 递归获取所有后代
            suspend fun getDescendants(currentMember: FamilyMember) {
                val children = repository.getChildrenByParentId(currentMember.id)
                descendants.addAll(children)

                for (child in children) {
                    getDescendants(child)
                }
            }

            getDescendants(member)
            _descendants.postValue(descendants)
        }
    }

    // 筛选方法
    fun setFilterIsAlive(isAlive: Boolean?) {
        filterIsAlive.value = isAlive
    }

    fun setFilterGender(gender: Gender?) {
        filterGender.value = gender
    }

    fun setFilterGeneration(generation: Int?) {
        filterGeneration.value = generation
    }

    fun setFilterBranch(branch: String?) {
        filterBranch.value = branch
    }

    fun resetFilters() {
        filterIsAlive.value = null
        filterGender.value = null
        filterGeneration.value = null
        filterBranch.value = null
    }
}

class FamilyTreeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FamilyTreeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FamilyTreeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 