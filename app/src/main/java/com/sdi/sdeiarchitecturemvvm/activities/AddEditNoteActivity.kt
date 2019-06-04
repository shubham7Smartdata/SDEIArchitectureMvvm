package com.sdi.sdeiarchitecturemvvm.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.sdi.sdeiarchitecturemvvm.R
import com.sdi.sdeiarchitecturemvvm.viewModels.NoteViewModel
import kotlinx.android.synthetic.main.activity_add_note.*

class AddEditNoteActivity : BaseActivity<NoteViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_add_note

    override val viewModel: NoteViewModel
        get() = ViewModelProviders.of(this).get(NoteViewModel::class.java)

    override val context: Context
        get() = this@AddEditNoteActivity

    override fun onCreate() {

        number_picker_priority!!.minValue = 1
        number_picker_priority!!.maxValue = 10

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)
        val intent = intent

        if (intent.hasExtra(EXTRA_ID)) {
            title = getString(R.string.edit_note)
            edit_text_title!!.setText(intent.getStringExtra(EXTRA_TITLE))
            edit_text_description!!.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            number_picker_priority!!.value = intent.getIntExtra(EXTRA_PRIORITY, 1)
        } else {
            title = getString(R.string.add_note)
        }
    }

    override fun initListeners() {

    }

    private fun saveNote() {
        val title = edit_text_title!!.text.toString()
        val description = edit_text_description!!.text.toString()
        val priority = number_picker_priority!!.value

        if (title.trim { it <= ' ' }.isEmpty() || description.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_insert_a_title_and_discription), Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, description)
        data.putExtra(EXTRA_PRIORITY, priority)

        if (intent.getIntExtra(EXTRA_ID, -1) != -1) {
            data.putExtra(
                EXTRA_ID, intent.getIntExtra(
                    EXTRA_ID, -1
                )
            )
        }
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        val EXTRA_TITLE = "EXTRA_TITLE"
        val EXTRA_ID = "EXTRA_ID"
        val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        val EXTRA_PRIORITY = "EXTRA_PRIORITY"
    }
}
