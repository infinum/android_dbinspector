package com.infinum.dbinspector.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinum.dbinspector.sample.data.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val databaseProvider: DatabaseProvider
) : ViewModel() {

    private var job: Job? = null

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }

    fun copy() {
        job = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                databaseProvider.copy()
            }
        }
    }
}