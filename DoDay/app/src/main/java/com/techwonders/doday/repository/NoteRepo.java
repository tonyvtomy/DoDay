package com.techwonders.doday.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.techwonders.doday.db.NoteDao;
import com.techwonders.doday.db.NoteDatabase;
import com.techwonders.doday.model.Category;
import com.techwonders.doday.model.Note;

import java.util.List;

public class NoteRepo {

    private final NoteDao noteDao;
    private LiveData<List<Note>> noteList;
    private final LiveData<List<Category>> categoryList;


    public NoteRepo(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDao = noteDatabase.noteDao();
        noteList = noteDao.getAllNotes();
        categoryList = noteDao.getAllCategory();
    }

    /// Note
    public void insertData(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() -> noteDao.insert(note));
    }

    public void updateData(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() -> noteDao.update(note));
    }

    public void deleteData(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() -> noteDao.delete(note));
    }

    public void deleteAllData(int cat_id) {
        NoteDatabase.databaseWriteExecutor.execute(() -> noteDao.deleteNotesByCategory(cat_id));
    }

    public void deleteAllNotes() {
        NoteDatabase.databaseWriteExecutor.execute(noteDao::deleteAllNotes);
    }

    public LiveData<List<Note>> getAllNotes() {
        return noteList;
    }

    public LiveData<List<Note>> getNotesByCategory(int cat_id) {
        return noteDao.getNotesByCategory(cat_id);
    }


    // Category
    public void insertData(Category category) {
        NoteDatabase.databaseWriteExecutor.execute(() -> noteDao.insert(category));
    }

    public void updateData(Category category) {
        NoteDatabase.databaseWriteExecutor.execute(() -> noteDao.update(category));
    }

    public void deleteData(Category category) {
        NoteDatabase.databaseWriteExecutor.execute(() -> noteDao.delete(category));
    }

    public void updateCategoryById(int id) {
        Category category = noteDao.getCategory(id);
        NoteDatabase.databaseWriteExecutor.execute(() -> noteDao.update(category));
    }

    public LiveData<List<Category>> getAllCategory() {
        return categoryList;
    }

}
