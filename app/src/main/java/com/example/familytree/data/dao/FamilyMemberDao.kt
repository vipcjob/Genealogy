package com.example.familytree.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.familytree.data.model.FamilyMember
import com.example.familytree.data.model.Gender

@Dao
interface FamilyMemberDao {
    @Query("SELECT * FROM family_members ORDER BY generation, name")
    fun getAllMembers(): LiveData<List<FamilyMember>>
    
    @Query("SELECT * FROM family_members WHERE id = :id")
    suspend fun getMemberById(id: String): FamilyMember
    
    @Query("SELECT * FROM family_members WHERE generation = :generation ORDER BY name")
    fun getMembersByGeneration(generation: Int): LiveData<List<FamilyMember>>
    
    @Query("SELECT * FROM family_members WHERE isAlive = 1 ORDER BY generation, name")
    fun getLivingMembers(): LiveData<List<FamilyMember>>
    
    @Query("SELECT * FROM family_members WHERE gender = :gender ORDER BY generation, name")
    fun getMembersByGender(gender: Gender): LiveData<List<FamilyMember>>
    
    @Query("SELECT * FROM family_members WHERE parentId = :parentId ORDER BY birthDate")
    suspend fun getChildrenByParentId(parentId: String): List<FamilyMember>
    
    @Query("SELECT COUNT(*) FROM family_members")
    suspend fun getMemberCount(): Int
    
    @Query("SELECT MAX(generation) FROM family_members")
    suspend fun getMaxGeneration(): Int
    
    @Query("SELECT COUNT(*) FROM family_members WHERE isAlive = 1")
    suspend fun getLivingMemberCount(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: FamilyMember)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<FamilyMember>)
    
    @Update
    suspend fun updateMember(member: FamilyMember)
    
    @Delete
    suspend fun deleteMember(member: FamilyMember)
    
    @Query("DELETE FROM family_members")
    suspend fun deleteAllMembers()
    
    @Query("SELECT * FROM family_members WHERE name LIKE '%' || :query || '%' ORDER BY generation, name")
    fun searchMembers(query: String): LiveData<List<FamilyMember>>
} 