package com.example.familytree.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.familytree.data.dao.FamilyActivityDao
import com.example.familytree.data.dao.FamilyMemberDao
import com.example.familytree.data.dao.FamilyStoryDao
import com.example.familytree.data.model.FamilyActivity
import com.example.familytree.data.model.FamilyMember
import com.example.familytree.data.model.FamilyRelation
import com.example.familytree.data.model.FamilyStory

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