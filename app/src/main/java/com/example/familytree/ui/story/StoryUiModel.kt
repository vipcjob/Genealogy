package com.example.familytree.ui.story

import com.example.familytree.data.model.StoryType
import java.util.Date

data class StoryUiModel(
    val id: String,
    val title: String,
    val content: String,
    val author: String,
    val publishDate: Date,
    val storyType: StoryType,
    val relatedMemberNames: List<String>,
    val viewCount: Int
) 