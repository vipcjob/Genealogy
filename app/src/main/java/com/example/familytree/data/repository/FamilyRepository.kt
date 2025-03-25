package com.example.familytree.data.repository

import androidx.lifecycle.LiveData
import com.example.familytree.data.dao.FamilyActivityDao
import com.example.familytree.data.dao.FamilyMemberDao
import com.example.familytree.data.dao.FamilyStoryDao
import com.example.familytree.data.model.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class FamilyRepository @Inject constructor(
    private val memberDao: FamilyMemberDao,
    private val storyDao: FamilyStoryDao,
    private val activityDao: FamilyActivityDao
) {
    // 家族成员相关方法
    fun getAllMembers(): LiveData<List<FamilyMember>> = memberDao.getAllMembers()
    
    fun getMembersByGeneration(generation: Int): LiveData<List<FamilyMember>> = 
        memberDao.getMembersByGeneration(generation)
        
    fun getLivingMembers(): LiveData<List<FamilyMember>> = memberDao.getLivingMembers()
    
    fun getMembersByGender(gender: Gender): LiveData<List<FamilyMember>> = 
        memberDao.getMembersByGender(gender)
        
    suspend fun getMemberById(id: String): FamilyMember = memberDao.getMemberById(id)
    
    suspend fun getChildrenByParentId(parentId: String): List<FamilyMember> = 
        memberDao.getChildrenByParentId(parentId)
        
    suspend fun getMemberCount(): Int = memberDao.getMemberCount()
    
    suspend fun getMaxGeneration(): Int = memberDao.getMaxGeneration()
    
    suspend fun getLivingMemberCount(): Int = memberDao.getLivingMemberCount()
    
    fun searchMembers(query: String): LiveData<List<FamilyMember>> = 
        memberDao.searchMembers(query)
        
    suspend fun insertMember(member: FamilyMember) = memberDao.insertMember(member)
    
    suspend fun updateMember(member: FamilyMember) = memberDao.updateMember(member)
    
    suspend fun deleteMember(member: FamilyMember) = memberDao.deleteMember(member)
    
    // 家族故事相关方法
    fun getAllStories(): LiveData<List<FamilyStory>> = storyDao.getAllStories()
    
    fun getStoriesByType(type: StoryType): LiveData<List<FamilyStory>> = 
        storyDao.getStoriesByType(type)
        
    fun getStoriesForMember(memberId: String): LiveData<List<FamilyStory>> = 
        storyDao.getStoriesForMember(memberId)
        
    suspend fun getStoryById(id: String): FamilyStory? = storyDao.getStoryById(id)
    
    fun searchStories(query: String): LiveData<List<FamilyStory>> = 
        storyDao.searchStories(query)
        
    suspend fun insertStory(story: FamilyStory) = storyDao.insertStory(story)
    
    suspend fun updateStory(story: FamilyStory) = storyDao.updateStory(story)
    
    suspend fun deleteStory(story: FamilyStory) = storyDao.deleteStory(story)
    
    // 添加 Flow 方法
    fun getAllStoriesFlow(): Flow<List<FamilyStory>> = storyDao.getAllStoriesFlow()
    
    // 家族活动相关方法
    fun getAllActivities(): LiveData<List<FamilyActivity>> = activityDao.getAllActivities()
    
    fun getActivitiesByType(type: ActivityType): LiveData<List<FamilyActivity>> = 
        activityDao.getActivitiesByType(type)
        
    fun getActivitiesByStatus(status: ActivityStatus): LiveData<List<FamilyActivity>> = 
        activityDao.getActivitiesByStatus(status)
        
    fun getUpcomingActivities(date: Date = Date()): LiveData<List<FamilyActivity>> = 
        activityDao.getUpcomingActivities(date)
        
    suspend fun getActivityById(id: String): FamilyActivity? = 
        activityDao.getActivityById(id)
        
    suspend fun insertActivity(activity: FamilyActivity) = 
        activityDao.insertActivity(activity)
        
    suspend fun updateActivity(activity: FamilyActivity) = 
        activityDao.updateActivity(activity)
        
    suspend fun deleteActivity(activity: FamilyActivity) = 
        activityDao.deleteActivity(activity)
} 