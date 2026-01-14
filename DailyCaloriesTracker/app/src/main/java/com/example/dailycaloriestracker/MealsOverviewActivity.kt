package com.example.dailycaloriestracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailycaloriestracker.databinding.ActivityAddMealBinding
import com.example.dailycaloriestracker.databinding.ActivityMealsOverviewBinding
import com.example.dailycaloriestracker.databinding.ItemMealBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MealsOverviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealsOverviewBinding

    private var filterFrom: Long? = null
    private var filterTo: Long? = null

    private var userId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealsOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent?.getLongExtra("USER_ID", 0L) ?: 0L

        binding.recyclerMeals.layoutManager = LinearLayoutManager(this)

        binding.btnFilterFrom.setOnClickListener { pickDate(true) }
        binding.btnFilterTo.setOnClickListener { pickDate(false) }
        binding.btnClearFilter.setOnClickListener {
            filterFrom = null
            filterTo = null
            updateFilterButtons()
            loadMeals()
        }

        updateFilterButtons()
        loadMeals()
    }

    private fun pickDate(isFrom: Boolean) {
        val c = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth, 0, 0, 0)
            val time = cal.timeInMillis
            if (isFrom) filterFrom = time else filterTo = time + (24L * 60 * 60 * 1000 - 1)
            updateFilterButtons()
            loadMeals()
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateFilterButtons() {
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.btnFilterFrom.text = filterFrom?.let { fmt.format(Date(it)) } ?: getString(R.string.filter_from)
        binding.btnFilterTo.text = filterTo?.let { fmt.format(Date(it)) } ?: getString(R.string.filter_to)
    }

    private fun loadMeals() {
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(applicationContext)
            val dao = db.mealDao()
            val meals = withContext(Dispatchers.IO) {
                if (filterFrom != null && filterTo != null) {
                    dao.getMealsForUserBetween(userId, filterFrom!!, filterTo!!)
                } else {
                    dao.getMealsForUser(userId)
                }
            }
            binding.recyclerMeals.adapter = MealsAdapter(meals.toMutableList(), ::onEditClicked, ::onDeleteClicked)
        }
    }

    private fun onEditClicked(meal: Meal) {
        val dialogBinding = ActivityAddMealBinding.inflate(layoutInflater)

        dialogBinding.etName.setText(meal.name)
        dialogBinding.etCalories.setText(meal.calories.toString())
        dialogBinding.etCarbs.setText(meal.carbs.toString())
        dialogBinding.etProtein.setText(meal.protein.toString())
        dialogBinding.etFat.setText(meal.fat.toString())

        val types = MealType.entries.map { it.name }
        dialogBinding.spinnerType.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        dialogBinding.spinnerType.setSelection(types.indexOf(meal.type.name).coerceAtLeast(0))

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        alertDialog.show()

        dialogBinding.btnSave.setOnClickListener {
            val name = dialogBinding.etName.text?.toString()?.trim().orEmpty()
            if (name.isEmpty()) return@setOnClickListener
            val calories = dialogBinding.etCalories.text?.toString()?.toIntOrNull() ?: 0
            val carbs = dialogBinding.etCarbs.text?.toString()?.toIntOrNull() ?: 0
            val protein = dialogBinding.etProtein.text?.toString()?.toIntOrNull() ?: 0
            val fat = dialogBinding.etFat.text?.toString()?.toIntOrNull() ?: 0
            val selectedType = MealType.entries.find { it.name == dialogBinding.spinnerType.selectedItem as String } ?: MealType.OTHER
            lifecycleScope.launch {
                val db = AppDatabase.getInstance(applicationContext)
                val dao = db.mealDao()
                val updated = meal.copy(name = name, calories = calories, carbs = carbs, protein = protein, fat = fat, type = selectedType)
                withContext(Dispatchers.IO) { dao.updateMeal(updated) }
                loadMeals()

                alertDialog.dismiss()
            }
        }

        dialogBinding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun onDeleteClicked(meal: Meal) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm_delete))
            .setMessage(getString(R.string.confirm_delete_message))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                lifecycleScope.launch {
                    val db = AppDatabase.getInstance(applicationContext)
                    val dao = db.mealDao()
                    withContext(Dispatchers.IO) { dao.deleteMeal(meal) }
                    loadMeals()
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    class MealsAdapter(
        private val items: MutableList<Meal>,
        private val onEdit: (Meal) -> Unit,
        private val onDelete: (Meal) -> Unit
    ) : RecyclerView.Adapter<MealsAdapter.ViewHolder>() {

        class ViewHolder(val binding: ItemMealBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val meal = items[position]
            val b = holder.binding
            b.tvMealName.text = meal.name
            b.tvMealDetails.text = "${meal.calories} kcal · ${meal.carbs}g · ${meal.protein}g · ${meal.fat}g · ${meal.type.name} · ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(meal.eatenAt))}"
            b.btnEdit.setOnClickListener { onEdit(meal) }
            b.btnDelete.setOnClickListener { onDelete(meal) }
        }

        override fun getItemCount(): Int = items.size
    }
}
