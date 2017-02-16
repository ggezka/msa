package com.smk.its.job;

/**
 * Created by smkpc9 on 22/12/16.
 */

public class EventMessage {

    public static final int ADD = 200;
    public static final int SUCCESS = 100;
    public static final int ERROR = 300;

    private int id;
    private int status;
    private Object object;

    public EventMessage(int id, int status, Object object) {
        this.id = id;
        this.status = status;
        this.object = object;

    }




    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public Object getObject() {
        return object;

    }
}
