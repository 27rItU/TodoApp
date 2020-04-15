package com.mindorks.todonotesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyNotesActivity extends AppCompatActivity {
    String fullName;
    FloatingActionButton fabAddNotes;
    TextView textViewTitle, textViewDescription;
    SharedPreferences sharedPreferences;
    String TAG = "MyNotesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);
        bindView();
        setupSharedPreference();
        getIntentData();

        fabAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupDialogBox();
                //Log.d("MyNotesActivity","on click performed");

            }
        });

        getSupportActionBar().setTitle(fullName);

        }

    private void setupSharedPreference() {
        sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREFERENCE_NAME,MODE_PRIVATE);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        fullName = intent.getStringExtra(AppConstant.FULL_NAME);
        if (TextUtils.isEmpty(fullName)){
            fullName = sharedPreferences.getString(PrefConstant.FULL_NAME,"");
        }
    }

    private void bindView() {
        fabAddNotes = findViewById(R.id.fabAddNotes);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDescription = findViewById(R.id.textViewDescription);
    }

    private void setupDialogBox() {
        View view = LayoutInflater.from(MyNotesActivity.this).inflate(R.layout.add_notes_dialog_layout,null);
        final EditText editTextTitle = view.findViewById(R.id.editTextTitle);
        final EditText editTextDescription = view.findViewById(R.id.editTextDescription);
        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);
        //Dialog
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String desc = editTextDescription.getText().toString();
                //Log.d("MyNotesActivity",title);
                //Log.d("MyNotesActivity",desc);
                textViewTitle.setText(editTextTitle.getText().toString());
                textViewDescription.setText(editTextDescription.getText().toString());
                dialog.hide();

            }
        });
        dialog.show();
    }

}

