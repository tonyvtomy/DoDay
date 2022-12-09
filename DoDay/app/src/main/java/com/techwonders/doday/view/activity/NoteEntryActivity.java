package com.techwonders.doday.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.techwonders.doday.databinding.ActivityNoteEntryBinding;
import com.techwonders.doday.listeners.OnCategoryClickListener;
import com.techwonders.doday.view.adapter.CategoryAdapter;
import com.techwonders.doday.viewModel.NoteViewModel;

import java.util.Objects;

public class NoteEntryActivity extends BaseActivity implements OnCategoryClickListener {

    ActivityNoteEntryBinding binding;
    private CategoryAdapter categoryAdapter;
    private NoteViewModel noteViewModel;
    private int category_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        noteViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(NoteViewModel.class);

        String type = getIntent().hasExtra("type") ? getIntent().getStringExtra("type") : "";
        if (type.equals("update")) {
            setTitle("Update Note");
            binding.btnSave.setText("Update");

            int id = getIntent().getIntExtra("id", -1);
            category_id = getIntent().getIntExtra("category_id", -1);
            binding.etTitle.setText(getIntent().getStringExtra("title"));

            binding.btnSave.setOnClickListener(v -> {
                saveNote(id, v);
            });
        } else {
            setTitle("New Note");
            binding.btnSave.setText("Save");

            binding.btnSave.setOnClickListener(v -> {
                saveNote(-1, v);
            });
        }
        addObserver();
    }

    private void saveNote(int id, View v) {
        hideKeyboard(v);
        String title = binding.etTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Plz enter the note", Toast.LENGTH_SHORT).show();
            return;
        }

        if (categoryAdapter.getAllCategory().size() <= 0) {
            Toast.makeText(this, "Plz add category first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (category_id <= 0) {
            Toast.makeText(this, "Plz select category", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent dataIntent = new Intent();
        dataIntent.putExtra("title", title);
        dataIntent.putExtra("category_id", category_id);
        if (id > 0) dataIntent.putExtra("id", id);
        setResult(RESULT_OK, dataIntent);
        finish();
    }

    private void addObserver() {
        categoryAdapter = new CategoryAdapter(this, this);
        binding.rvCategory.setAdapter(categoryAdapter);
        noteViewModel.getAllCategory().observe(this, categories -> {
            categoryAdapter.updateCategoryList(categories);
            categoryAdapter.isFirstTime = false;
            categoryAdapter.updateSelectionAt(category_id);
        });
    }

    @Override
    public void onCategoryClick(int pos) {
        category_id = categoryAdapter.getCategory(pos).getId();
        categoryAdapter.updateSelection(pos);
    }

    @Override
    public void onCategoryLongClick(int pos) {

    }
}
