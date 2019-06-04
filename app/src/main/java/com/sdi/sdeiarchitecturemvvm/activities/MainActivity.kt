package com.sdi.sdeiarchitecturemvvm.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdi.sdeiarchitecturemvvm.R
import com.sdi.sdeiarchitecturemvvm.adapter.NoteAdapter
import com.sdi.sdeiarchitecturemvvm.data.Note
import com.sdi.sdeiarchitecturemvvm.viewModels.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<NoteViewModel>(), NoteAdapter.OnItemClickListener {

    override val layoutId: Int
        get() = R.layout.activity_main

    override val viewModel: NoteViewModel
        get() = ViewModelProviders.of(this).get(NoteViewModel::class.java)

    override val context: Context
        get() = this@MainActivity

    val ADD_NOTE_REQUEST = 1
    val EDIT_NOTE_REQUEST = 2

    override fun onItemClick(note: Note) {
        val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
        intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.id)
        intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.title)
        intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.description)
        intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.priority)
        startActivityForResult(intent, EDIT_NOTE_REQUEST)
    }

    override fun onCreate() {
        button_add_note.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        val adapter = NoteAdapter()
        recycler_view.adapter = adapter

        adapter.setOnItemClickListener(this)

        mViewModel!!.allNotes.observe(this,
            Observer<List<Note>> { notes ->
                adapter.submitList(notes)
            })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mViewModel!!.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(this@MainActivity, getString(R.string.note_deleted), Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recycler_view)

    }

    override fun initListeners() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val title = data!!.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            val description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)

            val note = Note(title, description, priority)
            mViewModel!!.insert(note)

            Toast.makeText(this, getString(R.string.note_saved), Toast.LENGTH_SHORT).show()
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data!!.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)
            if (id == -1) {
                Toast.makeText(this, getString(R.string.Note_cant_be_updated), Toast.LENGTH_SHORT).show()
                return
            }
            val title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            val description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)

            val note = Note(title, description, priority)
            note.id = id
            mViewModel!!.update(note)

            Toast.makeText(this, getString(R.string.note_updated), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.note_not_saved), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_notes -> {
                mViewModel!!.deleteAllNotes()
                Toast.makeText(this, getString(R.string.all_notes_deleted), Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}