package com.fcz.genealogy.data.model

import androidx.room.*

/**
 * 家族关系实体，表示两个成员之间的关系
 */
@Entity(
    tableName = "family_relations",
    foreignKeys = [
        ForeignKey(
            entity = FamilyMember::class,
            parentColumns = ["id"],
            childColumns = ["memberId1"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FamilyMember::class,
            parentColumns = ["id"],
            childColumns = ["memberId2"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("memberId1"),  // 为 memberId1 创建索引
        Index("memberId2")   // 为 memberId2 创建索引
    ]
)
data class FamilyRelation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val memberId1: Long,
    val memberId2: Long,
    
    @ColumnInfo(name = "relation_type")
    val relationType: RelationType,
    val relationDetails: String? // 可用于存储额外关系信息
)

enum class RelationType {
    PARENT,     // 父母关系
    SPOUSE,     // 配偶关系
    CHILD       // 子女关系
} 