package com.example.noteapp.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.noteapp.data.entity.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM note ORDER BY judul ASC")
    fun getAllNote(): List<NoteEntity>

    @Query("SELECT * FROM note ORDER BY judul DESC")
    fun getAllNoteDesc() : List<NoteEntity>

    @Insert(onConflict = REPLACE)
    fun insertNote(noteEntity: NoteEntity): Long

    @Update
    fun updateNote(noteEntity: NoteEntity): Int

    @Delete
    fun deleteNote(noteEntity: NoteEntity): Int
}