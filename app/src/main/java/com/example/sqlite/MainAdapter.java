package com.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

        private List<String> fileList;
        private Context mContext;
        private ContentValues cv;
        private SQLiteDatabase db;
        private List<Integer> checkList;



        public void setItems(List<String> fileNames, Context context, List<Integer> checkList) {
            fileList = fileNames;
            mContext = context;
            this.checkList = checkList;
            db = new NotesDbHelper(mContext).getWritableDatabase();
            notifyDataSetChanged();
        }



        @NonNull
        @Override
        public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
            return new MainHolder(view);
        }



    @Override
        public void onBindViewHolder(@NonNull MainHolder holder, final int position) {
            String files = fileList.get(position);
            holder.fileName.setText(files);
            if (!checkList.isEmpty()) {
            Integer check = checkList.get(position);
            if (check == 1) {
                holder.checkBox.setChecked(true);
            }}
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cv = new ContentValues();
                    if (buttonView.isChecked()) {
                    cv.put(NotesDbSchema.NotesTable.Cols.CHECK, 1);
                        String[] selectionArgs = {String.valueOf(position)};
                        long rowID = db.update(NotesDbSchema.NotesTable.NAME, cv, "_id = ?", selectionArgs);
                       }
                    if (!buttonView.isChecked()) {
                        cv.put(NotesDbSchema.NotesTable.Cols.CHECK, 0);
                        String[] selectionArgs = {String.valueOf(position)};
                        long rowID = db.update(NotesDbSchema.NotesTable.NAME, cv, "_id = ?", selectionArgs);
                       }

                    }

            });
            holder.fileName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popup = new PopupMenu(mContext, v);
                    popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            String selection = NotesDbSchema.NotesTable.Cols.NOTE + " = ?";
                            String[] selectionArgs = { fileList.get(position) };
                            int count = db.delete(
                                    NotesDbSchema.NotesTable.NAME,
                                    selection,
                                    selectionArgs);
                            fileList.clear();
                            checkList.clear();
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
                                    fileList.add(title);
                                    checkList.add(check);
                                }
                            } finally {
                                cursor.close();
                            }

                            setItems(fileList, mContext, checkList);
                            return true;
                        }
                    });
                    popup.show();
                    return true;
                }
            });



        }

        @Override
        public int getItemCount() {
            return fileList.size();
        }



        static class MainHolder extends RecyclerView.ViewHolder {

            private TextView fileName;
            private CheckBox checkBox;


            public MainHolder(@NonNull View itemView) {
                super(itemView);
                fileName = itemView.findViewById(R.id.file_name_tv);
                checkBox = itemView.findViewById(R.id.checkBox);
            }
        }


    }

