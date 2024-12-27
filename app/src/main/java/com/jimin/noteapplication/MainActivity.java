package com.jimin.noteapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jimin.noteapplication.databinding.ActivityMainBinding;
import java.util.ArrayList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private ArrayList<Note> noteList;
    private NoteDatabaseHelper noteDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteDatabaseHelper = new NoteDatabaseHelper(this);

        noteList = new ArrayList<>();
        noteDatabaseHelper.addNote("2025년", "새해 복 많이 받으세요 :) 좋은 일만 가득하세요! 감사합니다!");

        noteAdapter = new NoteAdapter(noteList, new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                Log.d("MainActivity", "Note clicked: " + note.getTitle());

                NoteDetailFragment detailFragment = NoteDetailFragment.newInstance(note);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        }, new NoteAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Note note) {
                deleteNote(note);
            }
        });

        recyclerView.setAdapter(noteAdapter);
        loadNotes();

        FloatingActionButton fabAddNote = binding.fabAddNote;
        fabAddNote.setOnClickListener(v -> {

            NoteFormFragment noteFormFragment = new NoteFormFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, noteFormFragment)
                    .addToBackStack(null)
                    .commit();
        });

        binding.searchButton.setOnClickListener(view -> {
            String query = binding.searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                ArrayList<Note> filteredList = new ArrayList<>(noteDatabaseHelper.searchByTitle(query));
                noteAdapter.updateData(filteredList);
            } else {
                loadNotes();
            }
        });

    }

    private void deleteNote(Note note) {
        noteDatabaseHelper.deleteNote(note.getId());
        loadNotes();
    }

    public void loadNotes() {
        noteList.clear();
        noteList.addAll(noteDatabaseHelper.getAllNotes());
        noteAdapter.notifyDataSetChanged();
    }
}

