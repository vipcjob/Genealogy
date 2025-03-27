package com.fcz.genealogy.ui.familytree

import android.app.Application
import androidx.lifecycle.*
import com.fcz.genealogy.data.db.FamilyTreeDatabase
import com.fcz.genealogy.data.model.FamilyMember
import com.fcz.genealogy.data.model.Gender
import com.fcz.genealogy.data.repository.FamilyRepository
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
    private val filterEra = MutableStateFlow<String?>(null)
    private val searchQuery = MutableStateFlow<String?>(null)

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

        // 筛选逻辑
        viewModelScope.launch {
            filterIsAlive.combine(filterGender) { isAlive, gender ->
                Pair(isAlive, gender)
            }.combine(filterGeneration) { pair, generation ->
                Triple(pair.first, pair.second, generation)
            }.combine(filterBranch) { triple, branch ->
                Quadruple(triple.first, triple.second, triple.third, branch)
            }.combine(filterEra) { quad, era ->
                Quintuple(quad.first, quad.second, quad.third, quad.fourth, era)
            }.combine(searchQuery) { quint, query ->
                // 应用所有筛选条件
                allMembers.value?.filter { member ->
                    (quint.first == null || member.isAlive == quint.first) &&
                    (quint.second == null || member.gender == quint.second) &&
                    (quint.third == null || member.generation == quint.third) &&
                    (quint.fourth == null || member.branch == quint.fourth) &&
                    (quint.fifth == null || isInEra(member, quint.fifth)) &&
                    (query == null || matchesSearch(member, query))
                } ?: emptyList()
            }.collect { filtered ->
                _filteredMembers.postValue(filtered)
            }
        }
    }

    // 自定义数据类，用于组合多个筛选条件
    private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
    private data class Quintuple<A, B, C, D, E>(val first: A, val second: B, val third: C, val fourth: D, val fifth: E)

    // 根据年代筛选成员
    private fun isInEra(member: FamilyMember, era: String): Boolean {
        return when (era) {
            "清朝" -> member.birthYear in 1644..1911
            "民国时期" -> member.birthYear in 1912..1949
            "新中国成立后" -> member.birthYear ?: 0 >= 1950
            else -> false
        }
    }

    // 根据搜索词匹配成员
    private fun matchesSearch(member: FamilyMember, query: String): Boolean {
        if (query.isEmpty()) return true
        val lowercaseQuery = query.lowercase()
        return member.name.lowercase().contains(lowercaseQuery) ||
               member.occupation?.lowercase()?.contains(lowercaseQuery) == true ||
               member.branch?.lowercase()?.contains(lowercaseQuery) == true
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
        filterEra.value = null
        searchQuery.value = null
    }

    // 为FilterViewFragment添加的方法
    fun setGenderFilter(gender: Gender) {
        filterGender.value = gender
    }

    fun setAliveFilter(isAlive: Boolean) {
        filterIsAlive.value = isAlive
    }

    fun setGenerationFilter(generation: Int) {
        filterGeneration.value = generation
    }

    fun setBranchFilter(branch: String) {
        filterBranch.value = branch
    }

    fun setEraFilter(era: String) {
        filterEra.value = era
    }

    fun clearGenderFilter() {
        filterGender.value = null
    }

    fun clearGenerationFilter() {
        filterGeneration.value = null
    }

    fun clearBranchFilter() {
        filterBranch.value = null
    }

    fun clearEraFilter() {
        filterEra.value = null
    }

    fun resetAllFilters() {
        resetFilters()
    }

    fun applyFilters() {
        // 筛选已经自动应用，这个方法只是为了与UI交互
    }

    // 为GenerationViewFragment添加的方法
    fun filterByGeneration(generation: Int) {
        filterGeneration.value = generation
    }

    // 为ListViewFragment添加的方法
    fun resetGenerationFilter() {
        filterGeneration.value = null
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