package com.internshala.bookapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {

    @Insert
    fun insertBook(bookEntity: BookEntity)

    @Delete
    fun deletebook(bookEntity: BookEntity)

    @Query("SELECT * FROM books")
    fun getallbooks():List<BookEntity>

    @Query("SELECT * FROM books WHERE book_id= :bookId")
    fun getbookbyid(bookId:String): BookEntity

}