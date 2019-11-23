package com.example.sqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        public void onBindViewHolder(@NonNull MainHolder holder, int position) {
            final String files = fileList.get(position);
            holder.fileName.setText(files);
            holder.fileName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopupMenu(v);
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



    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.inflate(R.menu.menu_main);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                Toast.makeText(mContext,
                                        "Вы выбрали PopupMenu 1",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(mContext, "onDismiss",
                        Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }
    }

