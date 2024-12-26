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

        // 수정된 부분: 중괄호 위치 수정
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
                deleteNote(note);  // 삭제 버튼 클릭 시 해당 노트 삭제
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
    }

    private void deleteNote(Note note) {
        // 데이터베이스에서 해당 노트 삭제
        noteDatabaseHelper.deleteNote(note.getId());
        // 삭제 후 RecyclerView 갱신
        loadNotes();
    }

    public void loadNotes() {
        noteList.clear();
        noteList.addAll(noteDatabaseHelper.getAllNotes());
        noteAdapter.notifyDataSetChanged();
    }
}

