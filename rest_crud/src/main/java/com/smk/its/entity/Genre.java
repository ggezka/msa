package com.smk.its.entity;

/**
 * Created by smkpc9 on 22/12/16.
 */

public class Genre {
    private int id;
    private String name;
    private String subGenre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubGenre() {
        return subGenre;
    }

    public void setSubGenre(String subGenre) {
        this.subGenre = subGenre;
    }

    @Override
    public String toString() {
        return name;

    }

}
