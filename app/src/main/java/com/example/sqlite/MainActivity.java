package com.example.sqlite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.BaseColumns;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase sb;
    private ContentValues cv;
    private RecyclerView recycler;
    private List<String> titles = new ArrayList<>();
    private List<Integer> checkList = new ArrayList<>();
    private MainAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler = findViewById(R.id.main_recycler);
        sb = new NotesDbHelper(MainActivity.this).getWritableDatabase();

        updateView();
        initFab();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void updateView() {
        titles.clear();
        SQLiteDatabase db = new NotesDbHelper(this).getWritableDatabase();

        String[] projection = {
               BaseColumns._ID,
                NotesDbSchema.NotesTable.Cols.NOTE,
                NotesDbSchema.NotesTable.Cols.CHECK,

        };

        Cursor cursor = db.query(
                NotesDbSchema.NotesTable.NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        try {
            while (cursor.moveToNext()) {
                String title = cursor.getString(
                        cursor.getColumnIndex(NotesDbSchema.NotesTable.Cols.NOTE));
                int check = cursor.getInt(cursor.getColumnIndex(NotesDbSchema.NotesTable.Cols.CHECK));
                titles.add(title);
                checkList.add(check);
            }
        } finally {
            cursor.close();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        adapter = new MainAdapter();
        adapter.setItems(titles, this, checkList);
        recycler.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(),
                layoutManager.getOrientation());
        recycler.addItemDecoration(dividerItemDecoration);
        adapter.notifyDataSetChanged();

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
                cv = new ContentValues();
                cv.put(NotesDbSchema.NotesTable.Cols.NOTE, insertText);
                long rowID = sb.insert(NotesDbSchema.NotesTable.NAME, null, cv);
                updateView();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }




}
