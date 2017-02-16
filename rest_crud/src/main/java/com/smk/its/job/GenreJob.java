package com.smk.its.job;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.smk.its.AppApplication;
import com.smk.its.entity.Genre;
import com.smk.its.service.GenreService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Response;

/**
 * Created by smkpc9 on 22/12/16.
 */

public class GenreJob extends Job {

    public static final int POST = 100;
    public static final int  DELETE = 200;
    public static final int PUT = 300;
    public static final int GET_ALL = 400;

    private int id;
    private int status;
    private Genre genre;


    public GenreJob(int id, int status, Genre genre) {
        super(new Params(1));
        this.id = id;
        this.status = status;
        this.genre = genre;

        if (genre != null) {
            Log.d(getClass().getSimpleName(), "news title " + genre.getName());

        }
    }
    @Override
    public void onAdded() {
        Log.d(getClass().getSimpleName(), "Job added");
        EventBus.getDefault().post(new EventMessage(id, EventMessage.ADD, null));
    }

    @Override
    public void onRun() throws Throwable {
        Log.d(getClass().getSimpleName(), "Job running");
        Log.d(getClass().getSimpleName(), "job genre running" + status);
        GenreService service = AppApplication.getInstance()
                .getRetrofit()
                .create(GenreService.class);
        switch (status) {
            case GET_ALL:
                Log.d(getClass().getSimpleName(), "job news get all");
                Response<List<Genre>> responseGetAll = service.getCategory().execute();
                if (responseGetAll.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "job news get all success");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCESS, responseGetAll.body()));
                }else {
                    Log.d(getClass().getSimpleName(), "job news get all failed");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responseGetAll.body()));
                }

                break;
            case POST:
                Log.d(getClass().getSimpleName(), "job news post");
                Response<Genre> responsePost = service.postCategory(genre).execute();
                if (responsePost.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "job news post success");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCESS, responsePost.body()));
                } else {
                    Log.d(getClass().getSimpleName(), "job news post failed");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responsePost.body()));
                }

                break;
            case PUT:
                Log.d(getClass().getSimpleName(), "job news update");
                Response<Genre> responsePut = service.putCategory(genre.getId(), genre).execute();
                if (responsePut.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "job news update success");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCESS, responsePut.body()));
                } else {
                    Log.d(getClass().getSimpleName(), "job news update failed");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responsePut.body()));
                }
                break;
            case DELETE:
                Log.d(getClass().getSimpleName(), "job news delete");
                Response<Genre> responsedelete = service.deleteCategory(genre.getId()).execute();
                if (responsedelete.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "job news post delete");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCESS, responsedelete.body()));
                }else {
                    Log.d(getClass().getSimpleName(), "job news post delete");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responsedelete.body()));
                }

                break;
        }
    }

    @Override
    protected void onCancel() {
        Log.d(getClass().getSimpleName(), "job genre cancelled");
        EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, null));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
