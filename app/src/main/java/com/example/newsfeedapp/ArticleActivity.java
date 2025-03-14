package com.example.newsfeedapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleActivity extends AppCompatActivity implements ArticleAdapter.OnItemClickListener{

    RecyclerView list;
    ArrayList<String> urls;
    List<String> news = new ArrayList<>();
    List<String> description = new ArrayList<>();
    NewsInterface apiService;
    SQLiteDatabase myDatabase;
    String apiKey = BuildConfig.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.thenewsapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(NewsInterface.class);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        list = findViewById(R.id.list);
        try{
            myDatabase = this.openOrCreateDatabase("Articles", MODE_PRIVATE, null);
        }catch (Exception ignored){

        }

    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("url", urls.get(position));
        String title = news.get(position);
        String desc = description.get(position);
        String url = urls.get(position);

        String sql = "INSERT OR IGNORE INTO Articles (title, description, url) VALUES (?,?,?)";
        SQLiteStatement statement = myDatabase.compileStatement(sql);
        statement.bindString(1, title);
        statement.bindString(2, desc);
        statement.bindString(3, url);
        statement.execute();
        startActivity(intent);
    }

    public void Refresh(View view){
        urls = new ArrayList<>();
        // Set LayoutManager
        Call<ResponseWrapper> call = apiService.getTopNews(apiKey, "us", 3);
        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                if (response.isSuccessful()) {
                    List<Data> articles = response.body().getData();
                    for (Data article : articles) {
                        news.add(article.getTitle());
                        description.add(article.getDescription());
                        urls.add(article.getUrl());
//                        Log.d("Article", article.getTitle());
//                        Log.d("url", article.getUrl());
//                        Log.d("description", article.getDescription());
                    }
                    list.setLayoutManager(new LinearLayoutManager(ArticleActivity.this));

                    // Set Adapter
                    ArticleAdapter adapter = new ArticleAdapter(ArticleActivity.this, news, description, ArticleActivity.this);
                    list.setAdapter(adapter);
                } else {
                    Log.e("Error", "Failed to load data");
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                Log.e("Failure", t.getMessage());
            }
        });
    }

}