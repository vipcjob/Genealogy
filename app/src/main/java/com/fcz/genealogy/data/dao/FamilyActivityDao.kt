package com.fcz.genealogy.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fcz.genealogy.data.model.ActivityStatus
import com.fcz.genealogy.data.model.ActivityType
import com.fcz.genealogy.data.model.FamilyActivity
import java.util.Date

@Dao
interface FamilyActivityDao {
    @Query("SELECT * FROM family_activities ORDER BY startDate DESC")
    fun getAllActivities(): LiveData<List<FamilyActivity>>
    
    @Query("SELECT * FROM family_activities WHERE id = :id")
    suspend fun getActivityById(id: String): FamilyActivity?
    
    @Query("SELECT * FROM family_activities WHERE activityType = :type ORDER BY startDate DESC")
    fun getActivitiesByType(type: ActivityType): LiveData<List<FamilyActivity>>
    
    @Query("SELECT * FROM family_activities WHERE status = :status ORDER BY startDate")
    fun getActivitiesByStatus(status: ActivityStatus): LiveData<List<FamilyActivity>>
    
    @Query("SELECT * FROM family_activities WHERE startDate >= :date ORDER BY startDate")
    fun getUpcomingActivities(date: Date): LiveData<List<FamilyActivity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: FamilyActivity)
    
    @Update
    suspend fun updateActivity(activity: FamilyActivity)
    
    @Delete
    suspend fun deleteActivity(activity: FamilyActivity)
} 