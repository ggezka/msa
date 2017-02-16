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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.path.android.jobqueue.JobManager;
import com.smk.its.AppApplication;
import com.smk.its.R;
import com.smk.its.entity.Genre;
import com.smk.its.job.GenreJob;
import com.smk.its.job.EventMessage;
import com.smk.its.service.GenreService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by smkpc9 on 19/12/16.
 */

public class Genre_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static int PROCCESID_CATEGORY_POST = 2;

    private EditText editName;
    private EditText editDescription;
    private GenreService genreService;
    private Button menusave;

    private Genre genre = null;
    private JobManager jobManager;
    private ProgressDialog progressDialog;

    private List<Genre> categories = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editName = (EditText) findViewById(R.id.edit_Name);
        editDescription = (EditText) findViewById(R.id.edit_Description);
        menusave = (Button) findViewById(R.id.menu_save);

        menusave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        jobManager = AppApplication.getInstance().getJobManager();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("posting genre");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        editName.setText("");
        editDescription.setText("");
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
               if (message.getId() == PROCCESID_CATEGORY_POST) {

                    progressDialog.show();
                }
                break;
            case EventMessage.SUCCESS:
                if (message.getId() == PROCCESID_CATEGORY_POST) {

                    progressDialog.dismiss();
                }
                break;
            case EventMessage.ERROR:
                if (message.getId() == PROCCESID_CATEGORY_POST) {

                    progressDialog.dismiss();
                }
                break;
        }
    }

    private void save() {
        String name = editName.getText().toString();
        String description = editDescription.getText().toString();

        if (!name.equals("") && !description.equals("")) {

            if (genre == null) {

                Log.d(getClass().getSimpleName(), "proccess save");

                genre = new Genre();
                genre.setName(name);
                genre.setSubGenre(description);
                Toast.makeText(Genre_activity.this, "Genre Saved", Toast.LENGTH_SHORT).show();
                jobManager.addJobInBackground(new GenreJob(PROCCESID_CATEGORY_POST, GenreJob.POST, genre));

                genre = null;
                clearform();

            }

        }
    }
    public void edit(Genre genre) {
        this.genre = genre;

        editName.setText(genre.getName());
        editDescription.setText(genre.getSubGenre());
    }


    private void clearform() {
        editName.getText().clear();
        editDescription.getText().clear();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.news) {
            Intent i = new Intent(getApplicationContext(), Musik_activity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.genre) {
            Intent i = new Intent(getApplicationContext(), Genre_activity.class);
            startActivity(i);
            finish();
        } else  if (id == R.id.exit){
            System.exit(0);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;

    }
}
