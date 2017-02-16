package com.smk.its.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.path.android.jobqueue.JobManager;
import com.smk.its.AppApplication;
import com.smk.its.R;
import com.smk.its.adapter.MusikAdapter;
import com.smk.its.entity.Genre;
import com.smk.its.entity.Musik;
import com.smk.its.job.GenreJob;
import com.smk.its.job.EventMessage;
import com.smk.its.job.MusikJob;
import com.smk.its.service.MusikService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by smkpc9 on 19/12/16.
 */

public class Musik_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static int PROCCESID_CATEGORY_GET_ALL = 1;
    private static int PROCCESID_NEWS_GET_ALL = 2;
    private static int PROCCESID_NEWS_POST = 3;
    private static int PROCCESID_NEWS_PUT = 4;
    private static int PROCCESID_NEWS_DELETE = 5;

    private RecyclerView recyclerNews;
    private EditText editTitle;
    private EditText editTahun;
    private EditText editDesc;
    private Spinner spinnCategory;
    private MusikService musikService;


    private MusikAdapter musikAdapter;
    private Musik musik = null;
    private JobManager jobManager;
    private ProgressDialog progressDialog;

    private List<Genre> categories = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_musik);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerNews = (RecyclerView) findViewById(R.id.recycler_news);
        editTitle = (EditText) findViewById(R.id.edit_Title);
        editTahun = (EditText) findViewById(R.id.edit_Tahun);
        editDesc = (EditText) findViewById(R.id.edit_Desc);
        spinnCategory = (Spinner) findViewById(R.id.spin_category);

        musikAdapter = new MusikAdapter(this);

        jobManager = AppApplication.getInstance().getJobManager();

        recyclerNews.setLayoutManager(new LinearLayoutManager(this));
        recyclerNews.setItemAnimator(new DefaultItemAnimator());
        recyclerNews.setAdapter(musikAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.app_name);
        progressDialog.setMessage("please wait ....");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationview = (NavigationView) findViewById(R.id.nav_view);
        navigationview.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionmenu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_refresh:
                jobManager.addJobInBackground(new GenreJob(PROCCESID_CATEGORY_GET_ALL, GenreJob.GET_ALL, null));
                jobManager.addJobInBackground(new MusikJob(PROCCESID_NEWS_GET_ALL, MusikJob.GET_ALL, null));
                break;
            case R.id.menu_save:
                save();
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        jobManager.addJobInBackground(new GenreJob(PROCCESID_CATEGORY_GET_ALL, GenreJob.GET_ALL, null));
        jobManager.addJobInBackground(new MusikJob(PROCCESID_NEWS_GET_ALL, MusikJob.GET_ALL, null));
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage message){

        switch (message.getStatus()) {
            case EventMessage.ADD:
                if (message.getId() == PROCCESID_NEWS_GET_ALL) {
                    progressDialog.setMessage("please wait");
                    progressDialog.show();
                } else if (message.getId() == PROCCESID_NEWS_POST) {
                    progressDialog.setMessage("posting musik");
                    progressDialog.show();
                } else if (message.getId() == PROCCESID_NEWS_PUT) {
                    progressDialog.setMessage("update musik");
                    progressDialog.show();
                } else if (message.getId() == PROCCESID_NEWS_DELETE) {
                    progressDialog.setMessage("delete musik");
                    progressDialog.show();
                }
                break;
            case EventMessage.SUCCESS:
                Log.d(getClass().getSimpleName(), "receive success event" + message.getId());
                if (message.getId() == PROCCESID_NEWS_GET_ALL) {
                    progressDialog.dismiss();
                    List<Musik> newses = (List<Musik>) message.getObject();
                    Log.d(getClass().getSimpleName(), "receive musik get " + newses.size());

                    musikAdapter.clearNews();
                    musikAdapter.addnews(newses);
                } else if (message.getId() == PROCCESID_NEWS_POST) {
                    progressDialog.dismiss();

                    jobManager.addJobInBackground(new MusikJob(PROCCESID_NEWS_GET_ALL, MusikJob.GET_ALL, null));
                } else if (message.getId() == PROCCESID_NEWS_PUT) {
                    progressDialog.dismiss();

                    jobManager.addJobInBackground(new MusikJob(PROCCESID_NEWS_GET_ALL, MusikJob.GET_ALL, null));
                } else if (message.getId() == PROCCESID_NEWS_DELETE) {
                    progressDialog.dismiss();

                    jobManager.addJobInBackground(new MusikJob(PROCCESID_NEWS_GET_ALL, MusikJob.GET_ALL, null));
                }

                if (message.getId() == PROCCESID_CATEGORY_GET_ALL) {

                    categories = (List<Genre>) message.getObject();

                    ArrayAdapter<Genre> spinAdapter = new ArrayAdapter<Genre>(this, android.R.layout.simple_dropdown_item_1line, categories);
                    spinnCategory.setAdapter(spinAdapter);
                }

                break;
            case EventMessage.ERROR:
                if (message.getId() == PROCCESID_NEWS_GET_ALL) {
                    progressDialog.dismiss();
                }
                break;
        }

    }

    private void save() {
        String title = editTitle.getText().toString();
        String tahun = editTahun.getText().toString();
        String desc = editDesc.getText().toString();

        if (!title.equals("") && !tahun.equals("") && !desc.equals("")) {

            if (musik == null) {

                Log.d(getClass().getSimpleName(), "proccess save");

                Genre genre = categories.get(spinnCategory.getSelectedItemPosition());

                musik = new Musik();
                musik.setTitle(title);
                musik.setTahun(tahun);
                musik.setDeskripsi(desc);
                musik.setGenre(genre);

                jobManager.addJobInBackground(new MusikJob(PROCCESID_NEWS_POST, MusikJob.POST, musik));

                musik = null;
                clearform();

            } else {

                Log.d(getClass().getSimpleName(), "proccess update");

                Genre genre = categories.get(spinnCategory.getSelectedItemPosition());

                musik.setTitle(title);
                musik.setTahun(tahun);
                musik.setDeskripsi(desc);
                musik.setGenre(genre);

                jobManager.addJobInBackground(new MusikJob(PROCCESID_NEWS_PUT, MusikJob.PUT, musik));

                musik = null;
                clearform();
            }

        }
    }

    public void edit(Musik musik) {
        this.musik = musik;

        editTitle.setText(musik.getTitle());
        editTahun.setText(musik.getTahun());
        editDesc.setText(musik.getDeskripsi());

    }

    public void delete(Musik musik) {
        jobManager.addJobInBackground(new MusikJob(PROCCESID_NEWS_DELETE, MusikJob.DELETE, musik));
    }

    private void clearform() {
        editTitle.getText().clear();
        editTahun.getText().clear();
        editDesc.getText().clear();
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.news) {
            Intent i = new Intent(getApplicationContext(), Musik_activity.class);
            startActivity(i);
            finish();
        }else if (id == R.id.genre) {
            Intent i = new Intent(getApplicationContext(), Genre_activity.class);
            startActivity(i);
            finish();
        }else if (id == R.id.exit) {
            progressDialog.setMessage("Keluar");
            progressDialog.show();
            System.exit(0);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }


}
