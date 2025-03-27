package com.fcz.genealogy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 家族活动实体
 */
@Entity(tableName = "family_activities")
data class FamilyActivity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val startDate: Date,
    val endDate: Date?,
    val location: String,
    val organizer: String,
    val activityType: ActivityType,
    val participantIds: List<String> = listOf(), // 参与者ID
    val status: ActivityStatus,
    val imageUrls: List<String> = listOf()
)

enum class ActivityType {
    WORSHIP, // 祭祖活动
    REUNION, // 宗亲聚会
    CULTURAL, // 文化活动
    SOCIAL,  // 社交活动
    OTHER // 其他
}

enum class ActivityStatus {
    UPCOMING, // 即将举行
    ONGOING, // 正在进行
    COMPLETED // 已结束
} 