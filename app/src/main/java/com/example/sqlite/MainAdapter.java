package com.example.sqlite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

        private List<String> fileList;
        private TextView mTv;


        public void setItems(List<String> fileNames, TextView tv) {
            mTv = tv;
            fileList = fileNames;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main, parent, false);
            return new MainHolder(view);
        }



    @Override
        public void onBindViewHolder(@NonNull MainHolder holder, int position) {
            final String files = fileList.get(position);
            holder.fileName.setText(files);

        }

        @Override
        public int getItemCount() {
            return fileList.size();
        }



        static class MainHolder extends RecyclerView.ViewHolder {

            private final TextView fileName;


            public MainHolder(@NonNull View itemView) {
                super(itemView);
                fileName = itemView.findViewById(R.id.main_recycler);
            }
        }
    }

