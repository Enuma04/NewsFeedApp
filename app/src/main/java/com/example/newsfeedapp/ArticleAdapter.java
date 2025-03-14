package com.example.newsfeedapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {

    Context context;
    private List<String> newsList;
    private List<String> descriptionList;
    private OnItemClickListener listener;

    // Constructor
    public ArticleAdapter(Context context, List<String> newsList, List<String> descriptionList, OnItemClickListener listener) {
        this.context = context;
        this.newsList = newsList;
        this.descriptionList = descriptionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_recycler, parent, false);
        // View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);

        return new ArticleAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String title = newsList.get(position);
        String description = descriptionList.get(position);
        holder.news.setText(title);
        holder.description.setText(description);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    // ViewHolder class
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView news;
        TextView description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            news = itemView.findViewById(R.id.news);
            description = itemView.findViewById(R.id.description);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}