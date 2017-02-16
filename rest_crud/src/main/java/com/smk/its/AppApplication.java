package com.smk.its;

import android.app.Application;
import android.util.Log;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by smkpc9 on 22/12/16.
 */

public class AppApplication extends Application {

    private String BASE_URL = "http://192.168.1.69:8080/";
    private static AppApplication instance;

    public JobManager getJobManager() {
        return jobManager;
    }

    public Retrofit getRetrofit() {

        return retrofit;
    }

    private Retrofit retrofit;
    private JobManager jobManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        Configuration configuration = new Configuration.Builder(this)
                .minConsumerCount(1)
                .maxConsumerCount(3)
                .customLogger(new CustomLogger() {
                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d("JOB => ", String.format(text, args));

                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e("JOB => ",String.format(text, args)+t.getMessage());

                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e("JOB => ",String.format(text, args));

                    }
                })
                .build();
        jobManager = new JobManager(this, configuration);
    }

    public static AppApplication getInstance() {
        return instance;
    }
}
