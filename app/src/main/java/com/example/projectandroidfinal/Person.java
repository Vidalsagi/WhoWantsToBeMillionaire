package com.example.projectandroidfinal;

public class Person implements Comparable<Person> {
    private String prize;
    private String name;
    private String diff;
    private String time;

    public Person(String prize, String name, String diff, String time) {
        this.prize = prize;
        this.name = name;
        this.diff = diff;
        this.time = time;
    }

    public String getPrize() {
        return prize;
    }

    public String getDiff() {
        return diff;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Person o) {
        if (this.prize == null || o.prize == null) {
            return 0;
        }
        return o.prize.compareTo(this.prize);
    }
    public String getTime() {
        return time;
    }

}