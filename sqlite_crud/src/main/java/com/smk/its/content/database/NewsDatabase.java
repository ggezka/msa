package com.smk.its.content.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smk.its.content.database.model.NewsDatabaseModel;

/**
 * Created by smkpc9 on 20/12/16.
 */

public class NewsDatabase extends SQLiteOpenHelper{

    public static final String DATABASE = "its_smk";
    private static  final int VERSION = 1;
    public static final String NEWS_TBL = "news_tbl";

    protected Context context;

    public NewsDatabase(Context context) {
        super(context, DATABASE, null,VERSION);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+NEWS_TBL+"("
                + NewsDatabaseModel.ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + NewsDatabaseModel.TITLE+" TEXT,"
                + NewsDatabaseModel.CONTENT+" TEXT,"
                + NewsDatabaseModel.STATUS+" INTEGER,"
                + NewsDatabaseModel.CREATE_DATE+" INTEGER )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i < i1){

        }

    }
}
