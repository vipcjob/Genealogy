package com.example.familytree.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.familytree.data.model.FamilyStory
import com.example.familytree.data.model.StoryType
import kotlinx.coroutines.flow.Flow

@Dao
interface FamilyStoryDao {
    @Query("SELECT * FROM family_stories ORDER BY publishDate DESC")
    fun getAllStories(): LiveData<List<FamilyStory>>
    
    @Query("SELECT * FROM family_stories WHERE id = :id")
    suspend fun getStoryById(id: String): FamilyStory?
    
    @Query("SELECT * FROM family_stories WHERE storyType = :type ORDER BY publishDate DESC")
    fun getStoriesByType(type: StoryType): LiveData<List<FamilyStory>>
    
    @Query("SELECT * FROM family_stories WHERE :memberId IN (relatedMemberIds) ORDER BY publishDate DESC")
    fun getStoriesForMember(memberId: String): LiveData<List<FamilyStory>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: FamilyStory)
    
    @Update
    suspend fun updateStory(story: FamilyStory)
    
    @Delete
    suspend fun deleteStory(story: FamilyStory)
    
    @Query("SELECT * FROM family_stories WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY publishDate DESC")
    fun searchStories(query: String): LiveData<List<FamilyStory>>

    @Query("SELECT * FROM family_stories ORDER BY publishDate DESC")
    fun getAllStoriesFlow(): Flow<List<FamilyStory>>
} 