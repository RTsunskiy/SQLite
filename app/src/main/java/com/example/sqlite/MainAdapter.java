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

    private void setItemsAfterDelete(List<String> fileNames) {
        fileList = fileNames;
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
                    showPopupMenu(v, position);
                    return false;
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



    private void showPopupMenu(final View v, final int position) {
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.inflate(R.menu.menu_main);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                SQLiteDatabase db = new NotesDbHelper(mContext).getWritableDatabase();
                                String selection = BaseColumns._ID + " = ?";
                                String id = String.valueOf(position);
                                String[] selectionArgs = { id };
                                int count = db.delete(
                                        NotesDbSchema.NotesTable.NAME,
                                        selection,
                                        selectionArgs);
                                updateView();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(mContext, "Ничего не выбрано",
                        Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }

    private void updateView() {
        fileList.clear();
        SQLiteDatabase db = new NotesDbHelper(mContext).getWritableDatabase();

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
        setItemsAfterDelete(fileList);
    }
    }

