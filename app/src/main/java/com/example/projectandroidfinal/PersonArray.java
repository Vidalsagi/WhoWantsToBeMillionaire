package com.example.projectandroidfinal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PersonArray implements Serializable {
    List<Person> list;

    public PersonArray() {
        list = new ArrayList<Person>();
    }

    public void add(Person p){
        list.add(p);
    }

    public List<Person> getList(){
        return list;
    }

    public String toString(){
        String temp="";
        for(Person p:list)
            temp += p.getName() + "," + p.getPrize() + "/n";
        return temp;
    }

}
