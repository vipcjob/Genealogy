package com.fcz.genealogy.util

import com.fcz.genealogy.data.model.*
import java.util.Date

/**
 * 示例数据生成器
 */
class DataGenerator {
    companion object {
        /**
         * 生成示例家族成员数据
         */
        fun generateSampleFamilyMembers(): List<FamilyMember> {
            return listOf(
                // 第一代
                FamilyMember(
                    id = "1",
                    name = "张大年",
                    generation = 1,
                    gender = Gender.MALE,
                    birthDate = Date(1920 - 1900, 2, 15), // 1920-03-15
                    deathDate = Date(1990 - 1900, 7, 20), // 1990-08-20
                    isAlive = false,
                    birthPlace = "河南省周口市",
                    deathPlace = "河南省周口市",
                    occupation = "农民",
                    education = "私塾教育",
                    achievements = "为家族打下基础",
                    biography = "家族第一代族长，为家族打下了坚实的基础。",
                    parentId = null,
                    motherId = null,
                    spouseIds = listOf("2"),
                    childrenIds = listOf("3", "4"),
                    branch = "长房",
                    avatarUrl = null,
                    notes = null
                ),
                FamilyMember(
                    id = "2",
                    name = "李秀珍",
                    generation = 1,
                    gender = Gender.FEMALE,
                    birthDate = Date(1922 - 1900, 5, 10),
                    deathDate = Date(1995 - 1900, 3, 15),
                    isAlive = false,
                    birthPlace = "河南省周口市",
                    deathPlace = "河南省周口市",
                    occupation = "农民",
                    education = null,
                    achievements = null,
                    biography = "族长夫人，持家有方，教育子女有方。",
                    parentId = null,
                    motherId = null,
                    spouseIds = listOf("1"),
                    childrenIds = listOf("3", "4"),
                    branch = "长房",
                    avatarUrl = null,
                    notes = null
                ),

                // 第二代
                FamilyMember(
                    id = "3",
                    name = "张文华",
                    generation = 2,
                    gender = Gender.MALE,
                    birthDate = Date(1945 - 1900, 8, 20), // 1945-09-20
                    deathDate = null,
                    isAlive = true,
                    birthPlace = "河南省周口市",
                    deathPlace = null,
                    occupation = "教师",
                    education = null,
                    achievements = "现任族长，致力于家族文化传承。",
                    biography = "现任族长，致力于家族文化传承。",
                    parentId = "1",
                    motherId = "2",
                    spouseIds = listOf("5"),
                    childrenIds = listOf("5"),
                    branch = "长房",
                    avatarUrl = null,
                    notes = null
                ),
                FamilyMember(
                    id = "4",
                    name = "张大明",
                    generation = 2,
                    gender = Gender.MALE,
                    birthDate = Date(1948 - 1900, 11, 5), // 1948-12-05
                    deathDate = null,
                    isAlive = true,
                    birthPlace = "河南省周口市",
                    deathPlace = null,
                    occupation = "医生",
                    education = null,
                    achievements = "家族中第一位医生，在当地医院工作多年。",
                    biography = "家族中第一位医生，在当地医院工作多年。",
                    parentId = "1",
                    motherId = "2",
                    spouseIds = listOf("6"),
                    childrenIds = listOf("6"),
                    branch = "长房",
                    avatarUrl = null,
                    notes = null
                ),

                // 第三代
                FamilyMember(
                    id = "5",
                    name = "张丽君",
                    generation = 3,
                    gender = Gender.FEMALE,
                    birthDate = Date(1970 - 1900, 3, 15), // 1970-04-15
                    deathDate = null,
                    isAlive = true,
                    birthPlace = "河南省郑州市",
                    deathPlace = null,
                    occupation = "企业家",
                    education = null,
                    achievements = "成功创业者，多次组织家族聚会活动。",
                    biography = "成功创业者，多次组织家族聚会活动。",
                    parentId = "3",
                    motherId = "2",
                    spouseIds = listOf("3"),
                    childrenIds = listOf("5"),
                    branch = "长房",
                    avatarUrl = null,
                    notes = null
                ),
                FamilyMember(
                    id = "6",
                    name = "张明阳",
                    generation = 3,
                    gender = Gender.MALE,
                    birthDate = Date(1975 - 1900, 7, 8), // 1975-08-08
                    deathDate = null,
                    isAlive = true,
                    birthPlace = "河南省周口市",
                    deathPlace = null,
                    occupation = "工程师",
                    education = null,
                    achievements = "在科技公司担任高级工程师。",
                    biography = "在科技公司担任高级工程师。",
                    parentId = "4",
                    motherId = "2",
                    spouseIds = listOf("4"),
                    childrenIds = listOf("6"),
                    branch = "长房",
                    avatarUrl = null,
                    notes = null
                )
            )
        }

        /**
         * 生成示例家族活动数据
         */
        fun generateSampleFamilyActivities(): List<FamilyActivity> {
            return listOf(
                FamilyActivity(
                    id = "1",
                    title = "清明节祭祖活动",
                    description = "在祖坟举行传统的清明节扫墓仪式，缅怀先祖，传承家族文化。",
                    startDate = Date(2024 - 1900, 3, 5), // 2024-04-05
                    endDate = Date(2024 - 1900, 3, 5),
                    location = "河南省周口市祖坟",
                    organizer = "张文华",
                    activityType = ActivityType.WORSHIP,
                    participantIds = listOf("3", "4", "5", "6"),
                    status = ActivityStatus.UPCOMING,
                    imageUrls = listOf()
                ),
                FamilyActivity(
                    id = "2",
                    title = "张氏族谱整理工作",
                    description = "对家族族谱进行数字化整理，收集整理历史资料。",
                    startDate = Date(2024 - 1900, 2, 1), // 2024-03-01
                    endDate = Date(2024 - 1900, 4, 31), // 2024-05-31
                    location = "线上及线下同步进行",
                    organizer = "张文华",
                    activityType = ActivityType.CULTURAL,
                    participantIds = listOf("3", "5"),
                    status = ActivityStatus.ONGOING,
                    imageUrls = listOf()
                ),

                // 已完成的活动
                FamilyActivity(
                    id = "3",
                    title = "2024年春节宗亲聚会",
                    description = "举行新春团拜活动，长辈给晚辈发红包，举行家族新春茶话会，分享过去一年的经历和故事。",
                    startDate = Date(2024 - 1900, 1, 10), // 2024-02-10
                    endDate = Date(2024 - 1900, 1, 10),
                    location = "郑州市某酒店",
                    organizer = "张大明",
                    activityType = ActivityType.SOCIAL,
                    participantIds = listOf("3", "4", "5", "6"),
                    status = ActivityStatus.COMPLETED,
                    imageUrls = listOf()
                ),
                FamilyActivity(
                    id = "4",
                    title = "《张氏家训》朗读会",
                    description = "组织家族成员学习、朗读家训，传承家族价值观和传统美德。特别邀请老一辈成员讲述家训背后的故事。",
                    startDate = Date(2023 - 1900, 11, 18), // 2023-12-18
                    endDate = Date(2023 - 1900, 11, 18),
                    location = "线上会议",
                    organizer = "张文华",
                    activityType = ActivityType.CULTURAL,
                    participantIds = listOf("3", "5"),
                    status = ActivityStatus.COMPLETED,
                    imageUrls = listOf()
                ),
                FamilyActivity(
                    id = "5",
                    title = "2023年中秋家族聚会",
                    description = "举行传统中秋团圆活动，品茶赏月，分享月饼。同时表彰过去一年中取得优秀成就的家族成员。",
                    startDate = Date(2023 - 1900, 8, 29), // 2023-09-29
                    endDate = Date(2023 - 1900, 8, 29),
                    location = "北京市张丽君私宅",
                    organizer = "张丽君",
                    activityType = ActivityType.SOCIAL,
                    participantIds = listOf("5"),
                    status = ActivityStatus.COMPLETED,
                    imageUrls = listOf()
                )
            )
        }

        /**
         * 生成示例家族故事数据
         */
        fun generateSampleFamilyStories(): List<FamilyStory> {
            return listOf(
                FamilyStory(
                    id = "1",
                    title = "创业艰辛",
                    content = "张大年族长年轻时以务农为生，经历了无数艰辛。他常常说：'一粒粮食一滴汗，半块田地半条命。'这句话一直被后人传颂。",
                    author = "张文华",
                    publishDate = Date(2023 - 1900, 0, 15), // 2023-01-15
                    relatedMemberIds = listOf("1"),
                    storyType = StoryType.BIOGRAPHY,
                    imageUrls = listOf(),
                    viewCount = 15,
                    tags = listOf("创业", "奋斗", "家训")
                ),
                FamilyStory(
                    id = "2",
                    title = "求学之路",
                    content = "张文华在50年代考上了师范学院，成为村里第一个大学生。他后来把自己的求学经历整理成文，激励后辈勤奋读书。",
                    author = "张丽君",
                    publishDate = Date(2023 - 1900, 2, 20), // 2023-03-20
                    relatedMemberIds = listOf("3"),
                    storyType = StoryType.BIOGRAPHY,
                    imageUrls = listOf(),
                    viewCount = 12,
                    tags = listOf("教育", "奋斗", "榜样")
                ),
                FamilyStory(
                    id = "3",
                    title = "家族第一位医生",
                    content = "张大明在70年代成为了家族第一位医生。他经常义务为村民看病，深受村民爱戴。这段历史见证了家族服务社会的传统。",
                    author = "张明阳",
                    publishDate = Date(2023 - 1900, 0, 15), // 2023-01-15
                    relatedMemberIds = listOf("4"),
                    storyType = StoryType.BIOGRAPHY,
                    imageUrls = listOf(),
                    viewCount = 10,
                    tags = listOf("医疗", "服务", "贡献")
                )
            )
        }
    }
} 