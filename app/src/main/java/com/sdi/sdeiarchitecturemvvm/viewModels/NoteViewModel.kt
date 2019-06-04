package com.sdi.sdeiarchitecturemvvm.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sdi.sdeiarchitecturemvvm.data.Note
import com.sdi.sdeiarchitecturemvvm.repositories.NoteRepository

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    val allNotes: LiveData<List<Note>>

    init {
        repository = NoteRepository(application)
        allNotes = repository.allNotes
    }

    fun insert(note: Note) {
        repository.insert(note)
    }

    fun update(note: Note) {
        repository.update(note)
    }

    fun delete(note: Note) {
        repository.delete(note)
    }

    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }
}