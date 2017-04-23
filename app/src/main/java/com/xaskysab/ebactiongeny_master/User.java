package com.xaskysab.ebactiongeny_master;

import java.io.Serializable;

/**
 * Created by XaskYSab on 2017/4/23 0023.
 */

public class User implements Serializable{

    private String name ;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
