package com.internshala.bookapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities=[BookEntity::class],version = 1)
abstract class BookDatabase : RoomDatabase(){

    abstract fun bookDao():BookDao //doorway for all DAO operations and as this func is abstract there will be no default implementation

}