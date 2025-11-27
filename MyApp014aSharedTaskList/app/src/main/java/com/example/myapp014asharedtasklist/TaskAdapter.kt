package com.example.myapp014asharedtasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp014asharedtasklist.databinding.ItemTaskBinding

// Adapter obsluhuje zobrazení seznamu úkolů v RecyclerView.
// Používá ListAdapter + DiffUtil pro efektivní aktualizace.
class TaskAdapter(
    private val onChecked: (String) -> Unit,  // zavolá se při kliknutí na checkbox (předáme id)
    private val onDelete: (String) -> Unit,    // zavolá se při kliknutí na ikonu smazání (předáme id)
    private val onEditTitle: (String, String) -> Unit // zavolá se při kliknutí na název (id, currentTitle)
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    // ViewHolder drží jeden řádek seznamu (item_task.xml) a jeho view binding.
    inner class TaskViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Vytvoření nového ViewHolderu – vytvoří se layout jednoho řádku.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TaskViewHolder(binding)
    }

    // Naplnění dat pro jeden řádek – nastavíme text, checkbox a tlačítko delete.
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)

        // Nastaví text úkolu
        holder.binding.textTitle.text = task.title

        // Nastavíme click listener na název pro editaci
        holder.binding.textTitle.setOnClickListener {
            if (task.id.isNotEmpty()) {
                onEditTitle(task.id, task.title)
            }
        }

        // Odstraníme starý listener před nastavením isChecked (prevence při recycling)
        holder.binding.checkCompleted.setOnCheckedChangeListener(null)

        // Nastaví zaškrtnutí checkboxu podle hodnoty v objektu
        holder.binding.checkCompleted.isChecked = task.completed

        // Listener pro změnu stavu checkboxu
        // Při změně stavu zavoláme callback s id úkolu
        holder.binding.checkCompleted.setOnCheckedChangeListener { _, _ ->
            // Pokud id je prázdné, nic nedělej
            if (task.id.isNotEmpty()) {
                onChecked(task.id)
            }
        }

        // Listener pro smazání úkolu (předáme id)
        holder.binding.imageDelete.setOnClickListener {
            if (task.id.isNotEmpty()) {
                onDelete(task.id)
            }
        }
    }

    // Počet položek je řízen ListAdapter

    // Odstraňujeme vlastní submitList - používáme ListAdapter.submitList()
}

private class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}
