package com.abhishek.smartexpensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.abhishek.smartexpensetracker.data.local.room.AppDatabase
import com.abhishek.smartexpensetracker.data.repository.ExpenseRepository
import com.abhishek.smartexpensetracker.ui.navigation.NavGraph
import com.abhishek.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme
import com.abhishek.smartexpensetracker.utils.DataStoreManager
import com.abhishek.smartexpensetracker.utils.SyncManager
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val db = AppDatabase.getInstance(applicationContext)
        val repo = ExpenseRepository.getInstance(db.expenseDao())
        val dataStoreManager = DataStoreManager.create(applicationContext)
        val syncManager = SyncManager(repo) // Initialize SyncManager

        lifecycleScope.launch {
            syncManager.syncOnce()
        }

        setContent {
            SmartExpenseTrackerTheme(dataStore = dataStoreManager) {
                NavGraph(repository = repo)
            }
        }
    }
}
