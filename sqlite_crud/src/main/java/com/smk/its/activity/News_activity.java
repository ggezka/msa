package com.smk.its.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.smk.its.R;
import com.smk.its.adapter.NewsAdapter;
import com.smk.its.content.database.adapter.NewsDatabaseAdapter;
import com.smk.its.entity.News;

import java.util.Date;
import java.util.List;


/**
 * Created by smkpc9 on 19/12/16.
 */

public class News_activity extends AppCompatActivity {

    private RecyclerView recyclerNews;
    private EditText editTitle;
    private EditText editContent;

    private NewsAdapter newsAdapter;
    private NewsDatabaseAdapter newsDatabaseAdapter;
    private News news = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recyclerNews = (RecyclerView) findViewById(R.id.recycler_news);
        editTitle = (EditText) findViewById(R.id.edit_Title);
        editContent = (EditText) findViewById(R.id.edit_Content);

        newsAdapter = new NewsAdapter(this);
        newsDatabaseAdapter = new NewsDatabaseAdapter(this);

        recyclerNews.setLayoutManager(new LinearLayoutManager(this));
        recyclerNews.setItemAnimator(new DefaultItemAnimator());
        recyclerNews.setAdapter(newsAdapter);

        newsAdapter.addnews(newsList());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                newsAdapter.clearNews();
                newsAdapter.addnews(newsDatabaseAdapter.findNewsByTitle(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newsAdapter.clearNews();
                newsAdapter.addnews(newsDatabaseAdapter.findNewsByTitle(newText));
                return false;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_search:
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_refresh:

                newsAdapter.clearNews();
                newsAdapter.addnews(newsList());
                break;
            case R.id.menu_save:
                save();

                newsAdapter.clearNews();
                newsAdapter.addnews(newsList());
                break;
        }

        return true;
    }

    private void save() {
        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();

        if (!title.equals("") && !content.equals("")) {
            news = new News();
            news.setTitle(title);
            news.setContent(content);
            news.setCreateDate(new Date().getTime());

            newsDatabaseAdapter.save(news);
            Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();

            news = null;
            clearform();

        } else {
            news.setTitle(title);
            news.setContent(content);
            news.setCreateDate(new Date().getTime());

            newsDatabaseAdapter.save(news);
            Toast.makeText(this, "Data update", Toast.LENGTH_SHORT).show();

            news = null;
            clearform();
        }

    }

    public void edit(News news) {
        this.news = news;

        editTitle.setText(news.getTitle());
        editContent.setText(news.getContent());

    }

    public void delete(News news) {
        newsDatabaseAdapter.deleteNews(news);

        newsAdapter.clearNews();
        newsAdapter.addnews(newsList());
    }

    private void clearform() {
        editTitle.getText().clear();
        editContent.getText().clear();
    }

    private List<News> newsList() {
        return newsDatabaseAdapter.findNewsAll();

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
