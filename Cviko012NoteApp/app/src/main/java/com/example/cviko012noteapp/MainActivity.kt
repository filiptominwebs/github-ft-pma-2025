package com.example.cviko012noteapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cviko012noteapp.AddNoteActivity
import com.example.cviko012noteapp.data.Note
import com.example.cviko012noteapp.data.NoteDao
import com.example.cviko012noteapp.data.NoteHubDatabaseInstance
import com.example.cviko012noteapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteDao: NoteDao
    private lateinit var adapter: NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // value pro vyhledávání
        val searchQuery = MutableStateFlow("")

        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        // DAO
        noteDao = NoteHubDatabaseInstance.getDatabase(applicationContext).noteDao()

        // Adapter s callbacky
        adapter = NoteAdapter(
            onEditClick = { note ->
                val intent = Intent(this, EditNoteActivity::class.java)
                intent.putExtra("note_id", note.id)
                startActivity(intent)
            },
            onDeleteClick = { note ->
                deleteNote(note)
            }
        )

        // RecyclerView
        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewNotes.adapter = adapter

        // Flow
        lifecycleScope.launch {
            noteDao.getAllNotes().collectLatest { notes ->
                adapter.submitList(notes)
            }
        }

        // --- Vyhledávání v poznámkách ---
        // Listener SearchView: změna textu → změní hodnotu Flow
        binding.searchViewNotes.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery.value = newText ?: ""
                return true
            }
        })

        // Jeden společný collector, který reaguje na změnu textu
        lifecycleScope.launch {
            searchQuery.collectLatest { text ->
                if (text.isEmpty()) {
                    // Zobrazit všechny poznámky
                    noteDao.getAllNotes().collectLatest { notes ->
                        adapter.submitList(notes)
                    }
                } else {
                    // Zobrazit filtrované poznámky
                    noteDao.searchNotes(text).collectLatest { notes ->
                        adapter.submitList(notes)
                    }
                }
            }
        }

    }

    private fun deleteNote(note: Note) {
        // spustí se asynchronní úkol na vlákně určeném pro práci s databází
        // a ukončí se automaticky, když se aktivita zničí
        lifecycleScope.launch(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }
}