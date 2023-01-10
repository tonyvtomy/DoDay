package com.techwonders.doday.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.techwonders.doday.db.DoDayDatabase;
import com.techwonders.doday.db.NoteDao;
import com.techwonders.doday.model.Category;
import com.techwonders.doday.model.Note;

import java.util.List;

public class NoteRepo {

    private final NoteDao noteDao;
    private LiveData<List<Note>> noteList;
    private final LiveData<List<Category>> categoryList;


    public NoteRepo(Application application) {
        DoDayDatabase doDayDatabase = DoDayDatabase.getInstance(application);
        noteDao = doDayDatabase.noteDao();
        noteList = noteDao.getAllNotes();
        categoryList = noteDao.getAllCategory();
    }

    /// Note
    public void insertData(Note note) {
        DoDayDatabase.databaseWriteExecutor.execute(() -> noteDao.insert(note));
    }

    public void updateData(Note note) {
        DoDayDatabase.databaseWriteExecutor.execute(() -> noteDao.update(note));
    }

    public void deleteData(Note note) {
        DoDayDatabase.databaseWriteExecutor.execute(() -> noteDao.delete(note));
    }

    public void deleteAllData(int cat_id) {
        DoDayDatabase.databaseWriteExecutor.execute(() -> noteDao.deleteNotesByCategory(cat_id));
    }

    public void deleteAllNotes() {
        DoDayDatabase.databaseWriteExecutor.execute(noteDao::deleteAllNotes);
    }

    public LiveData<List<Note>> getAllNotes() {
        return noteList;
    }

    public LiveData<List<Note>> getNotesByCategory(int cat_id) {
        return noteDao.getNotesByCategory(cat_id);
    }


    // Category
    public void insertData(Category category) {
        DoDayDatabase.databaseWriteExecutor.execute(() -> noteDao.insert(category));
    }

    public void updateData(Category category) {
        DoDayDatabase.databaseWriteExecutor.execute(() -> noteDao.update(category));
    }

    public void deleteData(Category category) {
        DoDayDatabase.databaseWriteExecutor.execute(() -> noteDao.delete(category));
    }

    public void updateCategoryById(int id) {
        Category category = noteDao.getCategory(id);
        DoDayDatabase.databaseWriteExecutor.execute(() -> noteDao.update(category));
    }

    public LiveData<List<Category>> getAllCategory() {
        return categoryList;
    }

}
