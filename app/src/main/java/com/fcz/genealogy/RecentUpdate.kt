package com.fcz.genealogy.data.model

import java.util.Date

/**
 * 最近动态实体，用于首页显示
 */
data class RecentUpdate(
    val id: String,
    val title: String,
    val timestamp: Date,
    val avatarType: String, // member, notification, system
    val avatarText: String, // 显示在头像上的文字
    val avatarColor: String, // 头像背景颜色的十六进制值
    val relatedId: String? = null // 关联的成员ID、故事ID或活动ID
) 