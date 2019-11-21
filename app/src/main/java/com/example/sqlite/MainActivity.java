package com.example.sqlite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase sb;
    private ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sb = new NotesDBHelper(this).getWritableDatabase();
        cv = new ContentValues();

        initFab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor c = sb.query("mytable",
                null,
                null,
                null,
                null,
                null,
                null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");

            do {
                String note = c.getString(nameColIndex);
            } while (c.moveToNext());
            {
            } else
            c.close();
        }
    }



    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText editText = new EditText(this);
        builder.setView(editText);
        builder.setTitle(R.string.alert_dialog_title);
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String insertText = editText.getText().toString();
                cv.put("name", insertText);
                long rowID = sb.insert("mytable", null, cv);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }




}
