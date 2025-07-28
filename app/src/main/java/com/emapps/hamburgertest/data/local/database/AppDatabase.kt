package com.emapps.hamburgertest.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emapps.hamburgertest.data.local.dao.MealDao
import com.emapps.hamburgertest.data.local.entity.MealEntity

const val DATABASE_VERSION = 1

@Database(
    entities = [MealEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "MealsDatabase"

        fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }

    abstract fun mealDao(): MealDao
}