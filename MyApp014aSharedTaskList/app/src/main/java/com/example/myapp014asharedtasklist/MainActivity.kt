package com.example.myapp014asharedtasklist

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp014asharedtasklist.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore

    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore

        // Nastavení adapteru – nyní používá id (String) místo předávání celého Tasku
        adapter = TaskAdapter(
            onChecked = { id -> toggleCompleted(id) },
            onDelete = { id -> deleteTask(id) },
            onEditTitle = { id, currentTitle -> editTaskTitle(id, currentTitle) }
        )

        binding.recyclerViewTasks.adapter = adapter
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        // Přidání úkolu
        binding.buttonAdd.setOnClickListener {
            val title = binding.inputTask.text.toString()
            if (title.isNotEmpty()) {
                addTask(title)
                binding.inputTask.text.clear()
            }
        }

        // Realtime sledování Firestore
        listenForTasks()
    }

    private fun addTask(title: String) {
        println("DEBUG: addTask called with title = $title")
        // Přidáme dokument bez pole id; Firestore vygeneruje id a v listenForTasks ho doplníme do Task objektu.
        val data = mapOf("title" to title, "completed" to false)
        db.collection("tasks").add(data)
    }

    private fun toggleCompleted(taskId: String) {
        val docRef = db.collection("tasks").document(taskId)
        // Načteme aktuální hodnotu a invertujeme ji
        docRef.get().addOnSuccessListener { snapshot ->
            if (snapshot != null && snapshot.exists()) {
                val current = snapshot.getBoolean("completed") ?: false
                docRef.update("completed", !current)
            }
        }
    }

    private fun deleteTask(taskId: String) {
        db.collection("tasks").document(taskId).delete()
    }

    // Otevře dialog pro editaci názvu a uloží změnu do Firestore
    private fun editTaskTitle(taskId: String, currentTitle: String) {
        val editText = EditText(this)
        editText.setText(currentTitle)
        editText.setSelection(currentTitle.length)

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Upravit úkol")
            .setView(editText)
            .setPositiveButton("Uložit") { _, _ ->
                val newTitle = editText.text.toString().trim()
                if (newTitle.isNotEmpty() && newTitle != currentTitle) {
                    db.collection("tasks").document(taskId).update("title", newTitle)
                }
            }
            .setNegativeButton("Zrušit", null)
            .show()
    }

    private fun listenForTasks() {
        db.collection("tasks")
            // Sleduje kolekci tasks v reálném čase
            .addSnapshotListener { snapshots, _ ->
                // Převede dokumenty z Firestore na seznam objektů Task a doplní do nich id dokumentu
                val taskList = snapshots?.documents?.map { doc ->
                    val t = doc.toObject(Task::class.java) ?: Task()
                    t.copy(id = doc.id)
                } ?: emptyList()

                // Aktualizuje RecyclerView novým seznamem úkolů
                adapter.submitList(taskList)
            }
    }
}