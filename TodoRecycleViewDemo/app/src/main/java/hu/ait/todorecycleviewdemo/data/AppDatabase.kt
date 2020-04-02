package hu.ait.todorecycleviewdemo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Todo::class), version = 1) //change to version =2 when add a new colun in database
abstract class AppDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase::class.java, "todo.db")
                    .fallbackToDestructiveMigration() //always have it there just in case
                    .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}