package com.techwonders.doday.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.techwonders.doday.model.Category;
import com.techwonders.doday.model.Note;
import com.techwonders.doday.repository.NoteRepo;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepo noteRepo;
    private LiveData<List<Note>> noteList;
    private LiveData<List<Category>> categoryList;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepo = new NoteRepo(application);
        noteList = noteRepo.getAllNotes();
        categoryList = noteRepo.getAllCategory();
    }

    // Notes
    public void insert(Note note) {
        noteRepo.insertData(note);
    }

    public void update(Note note) {
        noteRepo.updateData(note);
    }

    public void delete(Note note) {
        noteRepo.deleteData(note);
    }

    public void deleteAllNotes() {
        noteRepo.deleteAllNotes();
    }

    public LiveData<List<Note>> getNotesByCategory(int cat_id) {
        return noteRepo.getNotesByCategory(cat_id);
    }

    public LiveData<List<Note>> getAllNotes() {
        return noteList;
    }


    // Category
    public void insert(Category category) {
        noteRepo.insertData(category);
    }

    public void update(Category category) {
        noteRepo.updateData(category);
    }

    public void delete(Category category) {
        noteRepo.deleteData(category);
        noteRepo.deleteAllData(category.getId());
    }

    public LiveData<List<Category>> getAllCategory() {
        return categoryList;
    }

}
