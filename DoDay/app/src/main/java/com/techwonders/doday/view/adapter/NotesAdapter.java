package com.techwonders.doday.view.adapter;

import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techwonders.doday.R;
import com.techwonders.doday.databinding.SingleDodayItemBinding;
import com.techwonders.doday.listeners.OnNoteClickListener;
import com.techwonders.doday.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> noteList;
    private OnNoteClickListener onNoteClickListener;

    public NotesAdapter(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
        noteList = new ArrayList<>();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_doday_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = getNote(holder.getAdapterPosition());
        holder.binding.checkBox.setChecked(note.isCompleted());

        if (note.isCompleted()) {
            StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
            holder.binding.tvTitle.setText(note.getTitle(), TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable) holder.binding.tvTitle.getText();
            spannable.setSpan(STRIKE_THROUGH_SPAN, 0, note.getTitle().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            holder.binding.tvTitle.setText(note.getTitle());
        }

        /*holder.binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (onNoteClickListener != null)
                onNoteClickListener.onCheckboxChange(position, isChecked);
        });*/

        holder.binding.checkBox.setOnClickListener(v -> {
            if (onNoteClickListener != null)
                onNoteClickListener.onCheckboxChange(position, holder.binding.checkBox.isChecked());
        });

        holder.binding.getRoot().setOnClickListener(v -> {
            if (onNoteClickListener != null)
                onNoteClickListener.onNoteClick(position);
        });

    }

    public void updateNotesList(final List<Note> newNotesList) {
        this.noteList.clear();
        this.noteList = newNotesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public Note getNote(int pos) {
        return noteList.get(pos);
    }

    public List<Note> getAllNotes() {
        return noteList;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        SingleDodayItemBinding binding;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SingleDodayItemBinding.bind(itemView);
        }
    }

}
