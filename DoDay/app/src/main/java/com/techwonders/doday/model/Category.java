package com.techwonders.doday.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_category")
public class Category {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    @Ignore
    private boolean isSelected = false;

    public Category() {
    }

    public Category(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Category(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
