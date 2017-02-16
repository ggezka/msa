package com.smk.its.job;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.smk.its.AppApplication;
import com.smk.its.entity.Musik;
import com.smk.its.service.MusikService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Response;

/**
 * Created by smkpc9 on 22/12/16.
 */

public class MusikJob extends Job {

    public static final int POST = 100;
    public static final int GET_ALL = 200;
    public static final int PUT = 300;
    public static final int DELETE = 400;

    private int id;
    private int status;
    private Musik musik;

    public MusikJob(int id, int status, Musik musik) {
        super(new Params(1));
        this.id = id;
        this.status = status;
        this.musik = musik;

        if (musik != null) {
            Log.d(getClass().getSimpleName(), "musik title " + musik.getTitle());

        }
    }

    @Override
    public void onAdded() {
        Log.d(getClass().getSimpleName(), "job musik added");
        EventBus.getDefault().post(new EventMessage(id, EventMessage.ADD,null));

    }

    @Override
    public void onRun() throws Throwable {
        Log.d(getClass().getSimpleName(), "job musik running");
        Log.d(getClass().getSimpleName(), "job musik running" + status);
        MusikService service = AppApplication.getInstance()
                .getRetrofit()
                .create(MusikService.class);

        switch (status){
            case GET_ALL:
                Log.d(getClass().getSimpleName(), "job musik get all");
                Response<List<Musik>> responseGetAll = service.getMusikses().execute();
                if (responseGetAll.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "job musik get all success");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCESS, responseGetAll.body()));
                }else {
                    Log.d(getClass().getSimpleName(), "job musik get all failed");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responseGetAll.body()));
                }

                break;
            case POST:
                Log.d(getClass().getSimpleName(), "job musik post");
                Response<Musik> responsePost = service.postMusik(musik).execute();
                if (responsePost.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "job musik post success");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCESS, responsePost.body()));
                }else {
                    Log.d(getClass().getSimpleName(), "job musik post failed");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responsePost.body()));
                }

                break;
            case PUT:
                Log.d(getClass().getSimpleName(), "job musik update");
                Response<Musik> responsePut = service.putMusik( musik.getId(),musik).execute();
                if (responsePut.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "job musik update success");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCESS, responsePut.body()));
                }else {
                    Log.d(getClass().getSimpleName(), "job musik update failed");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responsePut.body()));
                }

                break;
            case DELETE:
                Log.d(getClass().getSimpleName(), "job musik delete");
                Response<Musik> responsedelete = service.deleteMusik(musik.getId()).execute();
                if (responsedelete.isSuccessful()) {
                    Log.d(getClass().getSimpleName(), "job musik post delete");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.SUCCESS, responsedelete.body()));
                }else {
                    Log.d(getClass().getSimpleName(), "job musik post delete");
                    EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, responsedelete.body()));
                }

                break;


        }

    }

    @Override
    protected void onCancel() {
        Log.d(getClass().getSimpleName(), "job musik cancelled");
        EventBus.getDefault().post(new EventMessage(id, EventMessage.ERROR, null));

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
