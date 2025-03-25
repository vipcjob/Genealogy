package com.example.familytree.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 家族故事实体
 */
@Entity(tableName = "family_stories")
data class FamilyStory(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val author: String,
    val publishDate: Date,
    val relatedMemberIds: List<String> = listOf(), // 相关成员ID
    val storyType: StoryType,
    val imageUrls: List<String> = listOf(), // 相关图片
    val viewCount: Int = 0, // 查看次数
    val tags: List<String> = listOf() // 故事标签
)

enum class StoryType {
    FAMILY, // 家族故事
    HISTORY, // 家族历史
    BIOGRAPHY, // 人物传记
    EVENT // 活动记录
} 