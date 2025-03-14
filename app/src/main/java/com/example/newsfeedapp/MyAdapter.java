package com.example.newsfeedapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    private List<String> newsList;
    private List<String> descriptionList;
    private OnItemClickListener listener;
    private OnLongItemClickListener longListener;

    String API_TOKEN = "QJKIOonDVGoA0cV03XaDUjnIz0dTCOqu589EjKF1";

    // Constructor
    public MyAdapter(Context context, List<String> examList, List<String> descriptionList, OnItemClickListener listener, OnLongItemClickListener longListener) {
        this.context = context;
        this.newsList = examList;
        this.descriptionList = descriptionList;
        this.listener = listener;
        this.longListener = longListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler, parent, false);
       // View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String title = newsList.get(position);
        String description = descriptionList.get(position);
        holder.news.setText(title);
        holder.description.setText(description);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
        holder.itemView.setOnLongClickListener(v -> longListener.onLongItemClick(position));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    // ViewHolder class
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView news, description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            news = itemView.findViewById(R.id.news);
            description = itemView.findViewById(R.id.description);

        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public interface OnLongItemClickListener{
        boolean onLongItemClick(int position);
    }
}
