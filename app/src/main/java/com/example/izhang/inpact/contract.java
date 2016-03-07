package com.example.izhang.inpact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izhang on 9/12/15.
 */
public class contract {
    public String name;
    public String age;

    contract(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public List<contract> contracts;

}