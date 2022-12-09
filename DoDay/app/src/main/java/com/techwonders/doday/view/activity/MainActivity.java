package com.techwonders.doday.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techwonders.doday.databinding.ActivityMainBinding;
import com.techwonders.doday.databinding.AlertCategoryAddBinding;
import com.techwonders.doday.listeners.OnCategoryClickListener;
import com.techwonders.doday.listeners.OnNoteClickListener;
import com.techwonders.doday.model.Category;
import com.techwonders.doday.model.Note;
import com.techwonders.doday.view.adapter.CategoryAdapter;
import com.techwonders.doday.view.adapter.NotesAdapter;
import com.techwonders.doday.viewModel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnNoteClickListener {

    private ActivityMainBinding binding;
    private NoteViewModel noteViewModel;
    private NotesAdapter notesAdapter;
    private CategoryAdapter categoryAdapter;

    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        setTitle("DoDay - ToDo List");

        noteList = new ArrayList<>();

        noteViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(NoteViewModel.class);

        binding.categoryAdd.setOnClickListener(v -> showCategoryAddAlert(null));

        binding.fabAdd.setOnClickListener(v -> {
            Intent newDataIntent = new Intent(this, NoteEntryActivity.class);
            startActivityForResult(newDataIntent, 1);
        });


        categoryAdapter = new CategoryAdapter(this, new OnCategoryClickListener() {
            @Override
            public void onCategoryClick(int pos) {
                Category category = categoryAdapter.getCategory(pos);
                noteViewModel.getNotesByCategory(category.getId()).observe(MainActivity.this, notes -> {
                    Log.d("NOTES", "onCategoryClick observed");
                    notesAdapter.updateNotesList(notes);
                    noteList = notes;
                });
                categoryAdapter.updateSelection(pos);
            }

            @Override
            public void onCategoryLongClick(int pos) {
                Category category = categoryAdapter.getCategory(pos);
                showCategoryAddAlert(category);
            }
        });
        binding.rvCategory.setAdapter(categoryAdapter);
        noteViewModel.getAllCategory().observe(this, categories -> {
            if (categories != null && categories.size() > 0) {
                noteViewModel.getNotesByCategory(categories.get(0).getId()).observe(MainActivity.this, notes -> {
                    Log.d("NOTES", "getAllCategory observed");
                    notesAdapter.updateNotesList(notes);
                    noteList = notes;
                });
            }
            categoryAdapter.updateCategoryList(categories);
        });

        notesAdapter = new NotesAdapter(this);
        binding.rvNotes.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNotes.setAdapter(notesAdapter);
        /*noteViewModel.getAllNotes().observe(MainActivity.this, notes -> {
            notesAdapter.updateNotesList(notes);
            noteList = notes;
        });*/

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Category category = categoryAdapter.getCategory(viewHolder.getAdapterPosition());
                if (category.getId() > 0) {
                    noteViewModel.delete(category);
                    Toast.makeText(MainActivity.this, category.getTitle() + " Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(binding.rvCategory);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(notesAdapter.getNote(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(binding.rvNotes);
    }

    private void showCategoryAddAlert(Category category) {
        Dialog dialog = new Dialog(this);
        AlertCategoryAddBinding alertBinding = AlertCategoryAddBinding.inflate(getLayoutInflater());
        dialog.setContentView(alertBinding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        if (category != null) {
            alertBinding.etName.setText(category.getTitle());
        }

        alertBinding.btnSave.setOnClickListener(v -> {
            String name = alertBinding.etName.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Fill the category name", Toast.LENGTH_SHORT).show();
                return;
            }

            Category newCategory = new Category(name);
            if (category != null) {
                newCategory.setId(category.getId());
                noteViewModel.update(newCategory);
            } else {
                noteViewModel.insert(newCategory);
            }
            dialog.dismiss();
        });
        dialog.show();
    }


    private void doFilter(int cat_id) {
        Log.d("NOTES", "cat_id: " + cat_id);
        Log.d("NOTES", "noteList: " + noteList.size());

        List<Note> filteredList = new ArrayList<>();
        if (cat_id > 0) {
            for (Note note : noteList) {
                if (note.getCategory_id() == cat_id) {
                    filteredList.add(note);
                }
            }
        } else {
            filteredList = noteList;
        }
        Log.d("NOTES", "filteredList: " + filteredList.size());

        notesAdapter.updateNotesList(filteredList);
    }

    @Override
    public void onNoteClick(int pos) {
        Note note = notesAdapter.getNote(pos);

        Intent updateIntent = new Intent(this, NoteEntryActivity.class);
        updateIntent.putExtra("type", "update");
        updateIntent.putExtra("title", note.getTitle());
        updateIntent.putExtra("category_id", note.getCategory_id());
        updateIntent.putExtra("id", note.getId());
        startActivityForResult(updateIntent, 2);
    }

    @Override
    public void onCheckboxChange(int pos, boolean isChecked) {
        Note note = notesAdapter.getNote(pos);
        Log.d("NOTES", "Note title: " + note.getTitle() + ", isChecked: " + isChecked);
        note.setCompleted(isChecked);
        noteViewModel.update(note);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String title = data.getStringExtra("title");
            int category_id = data.getIntExtra("category_id", -1);

            Note note = new Note(title, category_id);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note Added", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            String title = data.getStringExtra("title");
            int category_id = data.getIntExtra("category_id", -1);
            int id = data.getIntExtra("id", -1);

            Note note = new Note(title, category_id);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        }
    }

}
