package com.example.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

        private List<String> fileList;
        private Context mContext;



        public void setItems(List<String> fileNames, Context context) {
            fileList = fileNames;
            mContext = context;
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
            final String files = fileList.get(position);
            holder.fileName.setText(files);
            holder.fileName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popup = new PopupMenu(mContext, v);
                    popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            SQLiteDatabase db = new NotesDbHelper(mContext).getWritableDatabase();
                            String selection = NotesDbSchema.NotesTable.Cols.NOTE + " = ?";
                            String[] selectionArgs = { fileList.get(position) };
                            int count = db.delete(
                                    NotesDbSchema.NotesTable.NAME,
                                    selection,
                                    selectionArgs);
                            fileList.clear();
                            String[] projection = {
                                    BaseColumns._ID,
                                    NotesDbSchema.NotesTable.Cols.NOTE,
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
                                    fileList.add(title);
                                }
                            } finally {
                                cursor.close();
                            }

                            setItems(fileList, mContext);
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

            private final TextView fileName;


            public MainHolder(@NonNull View itemView) {
                super(itemView);
                fileName = itemView.findViewById(R.id.file_name_tv);
            }
        }


    }

