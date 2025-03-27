package com.fcz.genealogy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date

/**
 * 家族成员实体
 */
@Entity(tableName = "family_members")
data class FamilyMember(
    @PrimaryKey val id: String,
    val name: String,
    val generation: Int, // 世代（一世、二世等）
    val gender: Gender,
    val birthDate: Date?,
    val deathDate: Date?,
    val isAlive: Boolean,
    val birthPlace: String?,
    val deathPlace: String?,
    val occupation: String?,
    val education: String?,
    val achievements: String?,
    val biography: String?,
    val parentId: String?, // 父亲ID
    val motherId: String?, // 母亲ID
    val spouseIds: List<String> = listOf(), // 配偶ID列表
    val childrenIds: List<String> = listOf(), // 子女ID列表
    val branch: String?, // 支系，例如"明德支"
    val avatarUrl: String?,
    val notes: String?
) {
    // 获取出生年份
    val birthYear: Int?
        get() = birthDate?.let {
            val calendar = Calendar.getInstance().apply { time = it }
            calendar.get(Calendar.YEAR)
        }
    
    // 获取去世年份
    val deathYear: Int?
        get() = deathDate?.let {
            val calendar = Calendar.getInstance().apply { time = it }
            calendar.get(Calendar.YEAR)
        }
}

enum class Gender {
    MALE, FEMALE, OTHER
} 