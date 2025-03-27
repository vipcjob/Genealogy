package com.fcz.genealogy.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fcz.genealogy.data.dao.FamilyActivityDao
import com.fcz.genealogy.data.dao.FamilyMemberDao
import com.fcz.genealogy.data.dao.FamilyStoryDao
import com.fcz.genealogy.data.model.FamilyActivity
import com.fcz.genealogy.data.model.FamilyMember
import com.fcz.genealogy.data.model.FamilyRelation
import com.fcz.genealogy.data.model.FamilyStory

@Database(
    entities = [
        FamilyMember::class,
        FamilyRelation::class,
        FamilyStory::class,
        FamilyActivity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FamilyTreeDatabase : RoomDatabase() {
    abstract fun familyMemberDao(): FamilyMemberDao
    abstract fun familyStoryDao(): FamilyStoryDao
    abstract fun familyActivityDao(): FamilyActivityDao
    
    companion object {
        @Volatile
        private var INSTANCE: FamilyTreeDatabase? = null
        
        fun getDatabase(context: Context): FamilyTreeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FamilyTreeDatabase::class.java,
                    "family_tree_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 