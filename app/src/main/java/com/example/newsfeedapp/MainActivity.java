package com.example.newsfeedapp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener, MyAdapter.OnLongItemClickListener{

    List<String> newsList = new ArrayList<>();
    List<String> descriptionList = new ArrayList<>();
    SQLiteDatabase myDatabase;
    MyAdapter adapter;
    RecyclerView recyclerView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        int itemId = item.getItemId();
        if (itemId == R.id.art) {
            Intent intent = new Intent(this, ArticleActivity.class);
            startActivity(intent);
            return true;
        }
        return false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);

        myDatabase = this.openOrCreateDatabase("Articles", MODE_PRIVATE, null);
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Articles(title VARCHAR UNIQUE, description VARCHAR, url VARCHAR)");

        refreshList();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set Adapter
        adapter = new MyAdapter(this, newsList, descriptionList, this, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, WebActivity.class);
        String name = newsList.get(position);

        try{
            Cursor c = myDatabase.rawQuery("SELECT url FROM Articles WHERE title = ?", new String[]{name});

            if (c.moveToFirst()) {
                String url = c.getString(0); // Get the first column (url)
                intent.putExtra("url", url);
                Log.d("Selected URL", url);
            }
        }catch(CursorIndexOutOfBoundsException ignore){

        }
        startActivity(intent);
    }

    @Override
    public boolean onLongItemClick(int position){
        String title = newsList.get(position);
        String sql = "DELETE FROM Articles WHERE title = ?";
        SQLiteStatement statement = myDatabase.compileStatement(sql);
        statement.bindString(1, title);
        statement.execute();
        refreshList();
        adapter.notifyDataSetChanged();
        return true;
    }

    public void refreshList(){
        try {
            newsList.clear();
            descriptionList.clear();
            Cursor c = myDatabase.rawQuery("SELECT * FROM Articles", null);
            int titleIndex = c.getColumnIndex("title");
            int descriptionIndex = c.getColumnIndex("description");
            int urlIndex = c.getColumnIndex("url");
            c.moveToFirst();
            while (c != null) {
                newsList.add(c.getString(titleIndex));
                descriptionList.add(c.getString(descriptionIndex));
//                Log.i("title", c.getString(titleIndex));
//                Log.i("description", c.getString(descriptionIndex));
//                Log.i("url", c.getString(urlIndex));
                c.moveToNext();
            }
        }catch(CursorIndexOutOfBoundsException ignore){

        }
    }

}