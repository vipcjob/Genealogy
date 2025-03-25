package com.example.familytree.ui.member

import android.app.Application
import androidx.lifecycle.*
import com.example.familytree.data.db.FamilyTreeDatabase
import com.example.familytree.data.model.FamilyMember
import com.example.familytree.data.repository.FamilyRepository
import kotlinx.coroutines.launch

class MemberDetailViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: FamilyRepository
    
    // 当前成员信息
    private val _member = MutableLiveData<FamilyMember>()
    val member: LiveData<FamilyMember> = _member
    
    // 父母信息
    private val _parent = MutableLiveData<FamilyMember>()
    val parent: LiveData<FamilyMember> = _parent
    
    // 配偶信息 - 修改为List
    private val _spouses = MutableLiveData<List<FamilyMember>>(emptyList())
    val spouses: LiveData<List<FamilyMember>> = _spouses
    
    // 子女信息
    private val _children = MutableLiveData<List<FamilyMember>>(emptyList())
    val children: LiveData<List<FamilyMember>> = _children
    
    init {
        // 初始化仓库
        val database = FamilyTreeDatabase.getDatabase(application)
        repository = FamilyRepository(
            database.familyMemberDao(),
            database.familyStoryDao(),
            database.familyActivityDao()
        )
    }
    
    fun loadMember(memberId: String) {
        viewModelScope.launch {
            val member = repository.getMemberById(memberId)
            if (member != null) {
                _member.postValue(member)
                
                // 加载父母信息
                member.parentId?.let { parentId ->
                    val parent = repository.getMemberById(parentId)
                    _parent.postValue(parent)
                }
                
                // 加载配偶信息 - 修改为处理多个配偶
                if (member.spouseIds.isNotEmpty()) {
                    val spousesList = member.spouseIds.mapNotNull { spouseId ->
                        repository.getMemberById(spouseId)
                    }
                    _spouses.postValue(spousesList)
                }
                
                // 加载子女信息
                val childrenList = member.childrenIds.mapNotNull { childId ->
                    repository.getMemberById(childId)
                }
                _children.postValue(childrenList)
            }
        }
    }
}

class MemberDetailViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemberDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MemberDetailViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 