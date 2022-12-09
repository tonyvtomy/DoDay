package com.techwonders.doday.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.techwonders.doday.model.Category;
import com.techwonders.doday.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM my_notes")
    void deleteAllNotes();

    @Query("DELETE FROM my_notes WHERE category_id=:cat_id")
    void deleteNotesByCategory(int cat_id);

    @Query("SELECT * FROM my_notes ORDER BY id ASC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM my_notes WHERE category_id=:cat_id ORDER BY id ASC")
    LiveData<List<Note>> getNotesByCategory(int cat_id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category category);

    @Update
    void update(Category note);

    @Delete
    void delete(Category note);

    @Query("SELECT * FROM my_category")
    LiveData<List<Category>> getAllCategory();

    @Query("SELECT * FROM my_category WHERE id=:id")
    Category getCategory(int id);

}
