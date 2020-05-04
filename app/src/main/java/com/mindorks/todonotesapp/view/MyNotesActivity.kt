package com.mindorks.todonotesapp.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mindorks.todonotesapp.NotesApp
import com.mindorks.todonotesapp.R
import com.mindorks.todonotesapp.adapter.NotesAdapter
import com.mindorks.todonotesapp.clicklisteners.ItemClickListener
import com.mindorks.todonotesapp.db.Notes
import com.mindorks.todonotesapp.utils.AppConstant
import com.mindorks.todonotesapp.utils.PrefConstant
import kotlinx.android.synthetic.main.activity_my_notes.*
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.mindorks.todonotesapp.workmanager.MyWorker
import java.sql.Time
import java.util.concurrent.TimeUnit

public class MyNotesActivity : AppCompatActivity() {
    var fullName: String? = null
    val ADD_NOTES_CODE = 100
    lateinit var fabAddNotes:FloatingActionButton
    val TAG = "MyNotesActivity"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var recyclerView: RecyclerView
    var notesList = ArrayList<Notes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_notes)
        bindView()
        setupSharedPreferences()
        getDataFromDatabase()
        getIntentData()
        setupRecyclerView()
        setUpWorkManager()

        supportActionBar?.title = fullName


        fabAddNotes.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                //setupDialogBox()
                val intent = Intent(this@MyNotesActivity,AddNotesActivity::class.java)
                startActivityForResult(intent,ADD_NOTES_CODE)
            }

        })

    }
    private fun setUpWorkManager() {
        val constrain = Constraints.Builder()
                .build()
        val request = PeriodicWorkRequest.Builder(MyWorker::class.java,1,TimeUnit.MINUTES).setConstraints(constrain).build()
        WorkManager.getInstance().enqueue(request)
    }

    private fun getDataFromDatabase() {
        val notesApp = applicationContext as NotesApp
        val notesDao = notesApp.getNotesDb().notesDao()
        notesList.addAll(notesDao.getAll())
    }

    private fun setupDialogBox() {
        val view = LayoutInflater.from(this@MyNotesActivity).inflate(R.layout.add_notes_dialog_layout,null)
        val editTextTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val editTextDescription = view.findViewById<EditText>(R.id.editTextDescription)
        val buttonSubmit = view.findViewById<Button>(R.id.buttonSubmit)
        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create()
        buttonSubmit.setOnClickListener( object : View.OnClickListener{
            override fun onClick(v: View?) {
                val title = editTextTitle.text.toString()
                val description = editTextDescription.text.toString()
                if (title.isNotEmpty() && description.isNotEmpty()) {
                    val notes = Notes(title=title,description =  description)
                    notesList.add(notes)
                    addNotesToDb(notes)
                }else{
                    Toast.makeText(this@MyNotesActivity,"Title or Description can't be empty",Toast.LENGTH_SHORT).show()
                }
                dialog.hide()
            }

        })
        dialog.show()
    }

    private fun addNotesToDb(notes: Notes) {
        //insert notes in db
        val notesApp = applicationContext as NotesApp
        val notesDao = notesApp.getNotesDb().notesDao()
        notesDao.insert(notes)

    }

    private fun setupRecyclerView() {
        val itemClickListener = object :ItemClickListener{
            override fun onClick(notes: Notes) {
                val intent = Intent(this@MyNotesActivity, DetailActivity::class.java)
                intent.putExtra(AppConstant.TITLE,notes.title)
                intent.putExtra(AppConstant.DESCRIPTION,notes.description)
                startActivity(intent)

            }

            override fun onUpdate(notes: Notes) {
                val notesApp = applicationContext as NotesApp
                val notesDao = notesApp.getNotesDb().notesDao()
                notesDao.updateNotes(notes)

            }

        }
        val notesAdapter = NotesAdapter(notesList,itemClickListener)
        val linearLayoutManager = LinearLayoutManager(this@MyNotesActivity)
        linearLayoutManager.orientation=RecyclerView.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter=notesAdapter

    }

    private fun getIntentData() {
        val intent = intent
        if (intent.hasExtra(AppConstant.FULL_NAME)) {
            fullName = intent.getStringExtra(AppConstant.FULL_NAME)?:""
        }
        if (fullName==null){
            fullName = sharedPreferences.getString(PrefConstant.FULL_NAME, "")?:""
        }
    }

    private fun setupSharedPreferences() {
        sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private fun bindView() {
        fabAddNotes = findViewById(R.id.fabAddNotes)
        recyclerView = findViewById(R.id.recyclerViewNotes)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==ADD_NOTES_CODE){
            val title = data?.getStringExtra(AppConstant.TITLE)
            val description = data?.getStringExtra(AppConstant.DESCRIPTION)
            val imagePath = data?.getStringExtra(AppConstant.IMAGE_PATH)
            val notes = Notes(title = title!!,description = description!!, imagePath = imagePath!!, isTaskCompleted = false)
            addNotesToDb(notes)
            notesList.add(notes)
            recyclerViewNotes.adapter?.notifyItemChanged(notesList.size-1)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.blog){
            Log.d(TAG,"clicked")
            val  intent = Intent(this@MyNotesActivity , BlogActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}